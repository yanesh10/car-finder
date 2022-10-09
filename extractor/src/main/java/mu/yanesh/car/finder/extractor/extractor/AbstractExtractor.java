package mu.yanesh.car.finder.extractor.extractor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mu.yanesh.car.finder.extractor.service.ExtractorService;
import mu.yanesh.car.finder.models.extractor.CarData;

import java.util.List;

@AllArgsConstructor
@Slf4j
public abstract class AbstractExtractor implements IExtractor {

    private final ExtractorService extractorService;

    @Override
    public void process() {
        log.info("Clearing Collection");
        extractorService.clearCollection();
        List<CarData> carDataList = extract();
        log.info("Saving car data");
        extractorService.saveAll(carDataList);
        log.info("Save completed");
    }
}
