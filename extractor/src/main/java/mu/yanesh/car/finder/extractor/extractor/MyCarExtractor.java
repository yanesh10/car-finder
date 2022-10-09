package mu.yanesh.car.finder.extractor.extractor;

import lombok.extern.slf4j.Slf4j;
import mu.yanesh.car.finder.extractor.config.MyCarConfigurationProperties;
import mu.yanesh.car.finder.extractor.service.ExtractorService;
import mu.yanesh.car.finder.models.extractor.CarData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@Profile("mycar")
public class MyCarExtractor extends AbstractExtractor implements IExtractor {

    private static final String CLASS = "class";
    private final MyCarConfigurationProperties myCarConfig;

    public MyCarExtractor(ExtractorService extractorService, MyCarConfigurationProperties myCarConfig) {
        super(extractorService);
        this.myCarConfig = myCarConfig;
    }

    @Override
    public StringBuilder generateUrl(int pageNumber) {
        StringBuilder url =  new StringBuilder(myCarConfig.getBaseUrl())
                .append(QUESTION_MARK).append(myCarConfig.getDefaultParams())
                .append(AND).append(myCarConfig.getTransmissionFilter())
                .append(AND).append(myCarConfig.getFuelTypeFilter());

        if (pageNumber >= 1) {
            url.append(AND).append(myCarConfig.getPageParam()).append(pageNumber);
        }
        return url;
    }

    @Override
    public List<CarData> extract() {
        log.info("STARTING EXTRACTION FOR MYCAR");
        List<CarData> carDataList = new ArrayList<>();
        Document document;
        int totalPageNumber = 0;
        try {
            log.debug(URL_CALLED, generateUrl(totalPageNumber).toString());
            document = Jsoup.connect(generateUrl(totalPageNumber).toString())
                    .userAgent(MOZILLA)
                    .get();
            totalPageNumber = getTotalPageNumber(document);
            transform(document, carDataList);
            log.debug(CAR_DATA_LIST_SIZE, carDataList.size());

            for (int pageNum = 1; pageNum <= totalPageNumber; pageNum++) {
                log.debug(URL_CALLED, generateUrl(pageNum).toString());
                document = Jsoup.connect(generateUrl(pageNum).toString())
                        .userAgent(MOZILLA)
                        .get();
                transform(document, carDataList);
                log.debug(CAR_DATA_LIST_SIZE, carDataList.size());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        log.info("ENDED EXTRACTION FOR MYCAR");
        return carDataList;
    }

    @Override
    public void transform(Document document, List<CarData> carDataList) {

        carDataList.addAll(Objects.requireNonNull(document.getElementById("main"))
                .getElementsByClass("col-sm-6 col-md-4 d-flex")
                .stream()
                .map(row -> row.childNode(1))
                .map(offer -> {
                    Node displayImages = offer.childNode(1);
                    String originalUrl = displayImages.attr("href");
                    String imageUrl = displayImages.childNode(1).childNode(1).attr("src");

                    Node detailsBlock = offer.childNode(3);
                    Node titleNode = detailsBlock.childNode(3).childNode(1).childNode(1);
                    String title1 = titleNode.childNode(0).toString();
                    String title2 = titleNode.childNodeSize() > 1 ? titleNode.childNode(1).childNode(0).toString()
                            : "";
                    String title = title1 + title2;

                    String transmission = detailsBlock.childNode(7).childNode(1).childNode(0).childNode(0).toString();
                    String fuelType = detailsBlock.childNode(7).childNode(3).childNode(0).childNode(0).toString();
                    String year = detailsBlock.childNode(7).childNode(5).childNode(0).childNode(0).toString();
                    String price = detailsBlock.childNodes().stream()
                            .filter(node -> node.attr(CLASS).equals("price-block"))
                            .findFirst()
                            .map(value -> value.childNode(0).childNode(1).toString())
                            .orElse(null);

                    return CarData.builder()
                            .title(title)
                            .price(price)
                            .fuelType(fuelType)
                            .imageUrl(imageUrl)
                            .originalLink(originalUrl)
                            .year(year)
                            .transmission(transmission)
                            .build();
                }).collect(Collectors.toList()));
    }

    @Override
    public int getTotalPageNumber(Document document) {
        return Objects.requireNonNull(document.getElementsByClass("pagination")
                        .first())
                .childNodes().stream()
                .filter(node -> node.attributesSize() > 0)
                .filter(el -> el.childNodeSize() == 1)
                .filter(el -> !el.attr(CLASS).contains("active"))
                .filter(el -> !el.attr(CLASS).contains("disabled"))
                .reduce((first, second) -> second)
                .map(el -> el.childNode(0).childNode(0))
                .map(el -> Integer.valueOf(el.toString()))
                .orElse(1);
    }
}
