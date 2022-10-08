package mu.yanesh.car.finder.extractor.extractor;

import mu.yanesh.car.finder.models.extractor.CarData;
import org.jsoup.nodes.Document;

import java.util.List;

public interface IExtractor {

    String QUESTION_MARK = "?";
    String MOZILLA = "Mozilla";

    StringBuilder generateUrl(int pageNumber);
    List<CarData> extract();
    void transform(Document document, List<CarData> carDataList);
    void process();
    int getTotalPageNumber(Document document);
}
