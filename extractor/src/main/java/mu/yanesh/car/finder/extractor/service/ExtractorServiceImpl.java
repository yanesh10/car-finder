package mu.yanesh.car.finder.extractor.service;

import lombok.AllArgsConstructor;
import mu.yanesh.car.finder.models.extractor.CarData;
import mu.yanesh.car.finder.repository.CarDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ExtractorServiceImpl implements ExtractorService {

    private final CarDataRepository carDataRepository;

    @Override
    public void saveAll(List<CarData> carDataList) {
        if (CollectionUtils.isEmpty(carDataList)) {
            return;
        }
        carDataList.parallelStream().forEach(carData -> carDataRepository.save(carData,
                CarDataRepository.COLLECTION_NAME));
    }

    @Override
    public void save(CarData carData) {
        if (Objects.isNull(carData)) {
            return;
        }
        carDataRepository.save(carData, CarDataRepository.COLLECTION_NAME);
    }

    @Override
    public void clearCollection() {
        carDataRepository.deleteAll(CarDataRepository.COLLECTION_NAME);
    }
}
