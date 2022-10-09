package mu.yanesh.car.finder.extractor.service;

import mu.yanesh.car.finder.models.extractor.CarData;

import java.util.List;

public interface ExtractorService {

    void saveAll(List<CarData> carDataList);
    void save(CarData carData);

    void clearCollection();

}
