package mu.yanesh.car.finder.extractor.extractor;

import lombok.extern.slf4j.Slf4j;
import mu.yanesh.car.finder.extractor.config.ExpressCarConfigurationProperties;
import mu.yanesh.car.finder.extractor.service.ExtractorService;
import mu.yanesh.car.finder.models.extractor.CarData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@Slf4j
@Profile("express-cars")
public class ExpressCarsExtractor extends AbstractExtractor implements IExtractor {

    private static final String BLANK = "";
    private static final String META_MIDDLE_UNIT_FONT_EXISTS_MILEAGE = "meta-middle-unit font-exists mileage";
    private static final String META_MIDDLE_UNIT_FONT_EXISTS_FUEL = "meta-middle-unit font-exists fuel";
    private static final String LISTING_LIST_LOOP_STM_LISTING_DIRECTORY_LIST_LOOP_STM_ISOTOPE_LISTING_ITEM = " listing-list-loop stm-listing-directory-list-loop stm-isotope-listing-item ";
    private final ExpressCarConfigurationProperties expressCarConfigs;

    public ExpressCarsExtractor(ExtractorService extractorService,
            ExpressCarConfigurationProperties expressCarConfigs) {
        super(extractorService);
        this.expressCarConfigs = expressCarConfigs;
    }

    @Override
    public StringBuilder generateUrl(int pageNumber) {
        StringBuilder url = new StringBuilder(expressCarConfigs.getBaseUrl());
        if (pageNumber >= 1) {
            url.append(expressCarConfigs.getPageParam()).append(pageNumber);
        }
        url.append(QUESTION_MARK).append(expressCarConfigs.getDefaultParams())
                .append(AND).append(expressCarConfigs.getTransmissionFilter());
        return url;
    }

    @Override
    public List<CarData> extract() {
        log.info("STARTING EXTRACTION FOR EXPRESS CARS");
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

            for (int pageNum = 2; pageNum < totalPageNumber; pageNum++) {
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
        log.info("ENDED EXTRACTION FOR EXPRESS CARS");
        return carDataList;
    }

    @Override
    public void transform(Document document, List<CarData> carDataList) {
        carDataList.addAll(document.getElementsByClass(
                LISTING_LIST_LOOP_STM_LISTING_DIRECTORY_LIST_LOOP_STM_ISOTOPE_LISTING_ITEM).stream().map(element -> {
            Node image = element.childNode(1);
            String title = image.childNode(3).attr("data-title");
            String originalUrl = image.childNode(5).attr("href");
            String imageUrl = image.childNode(5).childNode(1).childNode(1).attr("src");

            Node content = element.childNode(3);
            String price = StringUtils.trimAllWhitespace(
                    content.childNode(1).childNode(1).childNode(1).childNode(1).childNode(0).toString()).replace("Rs",
                    BLANK);
            String fuelType = getValue((Element) content, META_MIDDLE_UNIT_FONT_EXISTS_FUEL);
            String mileage = StringUtils.trimAllWhitespace(getValue((Element) content,
                    META_MIDDLE_UNIT_FONT_EXISTS_MILEAGE));

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

    private String getValue(Element content, String s) {
        if (content.getElementsByClass(s).isEmpty()
                || content.getElementsByClass(s).get(0).getElementsByClass("value").isEmpty()) {
            return BLANK;
        }
        return Optional.of(
                        Objects.requireNonNull(content.getElementsByClass(s)
                                        .get(0)
                                        .getElementsByClass("value")
                                        .first())
                                .childNode(0)
                                .toString())
                .orElse(BLANK);
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
