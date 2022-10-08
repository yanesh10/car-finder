package mu.yanesh.car.finder.extractor.extractor;

import mu.yanesh.car.finder.models.extractor.CarData;
import org.jsoup.nodes.Document;

import java.util.List;

public interface IExtractor {

    String CAR_DATA_LIST_SIZE = "CarData list size: {}";
    String URL_CALLED = "URL called: {}";
    String QUESTION_MARK = "?";
    String AND = "&";
    String MOZILLA = "Mozilla";

    StringBuilder generateUrl(int pageNumber);
    List<CarData> extract();
    void transform(Document document, List<CarData> carDataList);
    void process();
    int getTotalPageNumber(Document document);
}
