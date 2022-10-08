package mu.yanesh.car.finder.extractor.extractor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mu.yanesh.car.finder.extractor.config.ExpressCarConfigurationProperties;
import mu.yanesh.car.finder.models.extractor.CarData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
@Profile("express-cars")
public class ExpressCarsExtractor implements IExtractor {

    private final ExpressCarConfigurationProperties expressCarConfigs;

    @Override
    public StringBuilder generateUrl(int pageNumber) {
        StringBuilder url =  new StringBuilder(expressCarConfigs.getBaseUrl());
        if (pageNumber >= 1) {
            url.append(expressCarConfigs.getPageParam()).append(pageNumber);
        }
        url.append(QUESTION_MARK).append(expressCarConfigs.getDefaultParams())
                .append(AND).append(expressCarConfigs.getTransmissionFilter());
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

            for (int pageNum = 1; pageNum < totalPageNumber; pageNum++) {
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
        carDataList.addAll(document.getElementsByClass(" listing-list-loop stm-listing-directory-list-loop "
                + "stm-isotope-listing-item ").stream().map(element -> {
            Node image = element.childNode(1);
            String title = image.childNode(3).attr("data-title");
            String originalUrl = image.childNode(5).attr("href");
            String imageUrl = image.childNode(5).childNode(1).childNode(1).attr("src");

            Node content = element.childNode(3);
            String price = StringUtils.trimAllWhitespace(content.childNode(1).childNode(1).childNode(1).childNode(1).childNode(0).toString()).replace("Rs","");
            String fuelType =
                    Objects.nonNull(content.childNode(3).childNode(1).childNodeSize() >= 5) ?
                            StringUtils.trimAllWhitespace(content.childNode(3).childNode(1).childNode(5).childNode(3).childNode(0).toString()) : "";
            String mileage = Objects.nonNull(content.childNode(3).childNode(1).childNodeSize() >= 5) ?
                    StringUtils.trimAllWhitespace(content.childNode(3).childNode(1).childNode(1).childNode(3).childNode(0).toString()) : "";

            return CarData.builder()
                    .price(price)
                    .fuelType(fuelType)
                    .imageUrl(imageUrl)
                    .originalLink(originalUrl)
                    .title(title)
                    .mileage(mileage)
                    .build();
        }).collect(Collectors.toList()));
    }

    @Override
    public void process() {

    }

    @Override
    public int getTotalPageNumber(Document document) {
        return document.getElementsByClass("page-numbers")
                .stream().filter(el -> el.childNodeSize() == 1)
                .filter(el -> el.tagName().equals("a"))
                .filter(el -> !el.getAllElements().hasClass("next"))
                .reduce((first, second) -> second)
                .map(el -> Integer.valueOf(el.childNode(0).toString()))
                .orElse(0);
    }
}
