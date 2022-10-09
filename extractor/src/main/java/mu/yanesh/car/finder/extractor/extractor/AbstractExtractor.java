package mu.yanesh.car.finder.extractor.extractor;

import lombok.AllArgsConstructor;
import mu.yanesh.car.finder.extractor.service.ExtractorService;
import mu.yanesh.car.finder.models.extractor.CarData;

import java.util.List;

@AllArgsConstructor
public abstract class AbstractExtractor implements IExtractor {

    private final ExtractorService extractorService;

    @Override
    public void process() {
        List<CarData> carDataList = extract();
        extractorService.saveAll(carDataList);
    }
}
