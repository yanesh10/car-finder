package mu.car.finder.extractor.extractor;

import lombok.AllArgsConstructor;
import mu.car.finder.extractor.config.MyCarConfigurationProperties;
import mu.yanesh.car.finder.models.extractor.CarData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MyCarExtractor implements IExtractor {

    private final MyCarConfigurationProperties myCarConfig;

    @Override
    public StringBuilder generateUrl() {
        return new StringBuilder(myCarConfig.getBaseUrl())
                .append(QUESTION_MARK).append(myCarConfig.getDefaultParams())
                .append(QUESTION_MARK).append(myCarConfig.getTransmissionFilter())
                .append(QUESTION_MARK).append(myCarConfig.getFuelTypeFilter());
    }

    @Override
    public List<CarData> extract() {
        Document doc = null;
        try {
            doc = Jsoup.connect(generateUrl().toString())
                    .userAgent(MOZILLA)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element main = doc.getElementById("main");
        List<Node> offers =
                main.getElementsByClass("col-sm-6 col-md-4 d-flex")
                        .stream()
                        .map(row -> row.childNode(1))
                        .collect(Collectors.toList());

        return offers.stream().map(offer -> {
            Node displayImages = offer.childNode(1);
            String originalUrl = displayImages.attr("href");
            String imageUrl = displayImages.childNode(1).childNode(1).attr("src");

            Node detailsBlock = offer.childNode(3);
            Node titleNode = detailsBlock.childNode(3).childNode(1).childNode(1);
            String title1 = titleNode.childNode(0).toString();
            String title2 = titleNode.childNodeSize() > 1 ? titleNode.childNode(1).childNode(0).toString()
                    : "";
            String title = new StringBuilder(title1).append(title2).toString();

            String transmission = detailsBlock.childNode(7).childNode(1).childNode(0).childNode(0).toString();
            String fuelType = detailsBlock.childNode(7).childNode(3).childNode(0).childNode(0).toString();
            String year = detailsBlock.childNode(7).childNode(5).childNode(0).childNode(0).toString();
            String price = detailsBlock.childNodes().stream()
                    .filter(node -> node.attr("class").equals("price-block"))
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
        }).collect(Collectors.toList());
    }

    @Override
    public void process() {

    }
}
