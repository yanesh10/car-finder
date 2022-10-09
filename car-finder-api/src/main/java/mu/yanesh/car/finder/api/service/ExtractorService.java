package mu.yanesh.car.finder.api.service;

import mu.yanesh.car.finder.models.extractor.CarData;

import java.util.List;

public interface ExtractorService {

    List<CarData> findAll();
}
