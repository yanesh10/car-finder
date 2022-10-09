package mu.yanesh.car.finder.api.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mu.yanesh.car.finder.models.extractor.CarData;
import mu.yanesh.car.finder.repository.CarDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ExtractorServiceImpl implements ExtractorService {

    private final CarDataRepository carDataRepository;

    @Override
    public List<CarData> findAll() {
        return carDataRepository.findAll(CarData.class, CarDataRepository.COLLECTION_NAME);
    }
}
