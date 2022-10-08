package mu.car.finder.extractor.extractor;

import mu.yanesh.car.finder.models.extractor.CarData;

import java.util.List;

public interface IExtractor {

    String QUESTION_MARK = "?";
    String MOZILLA = "Mozilla";

    StringBuilder generateUrl();
    List<CarData> extract();
    void process();
}
