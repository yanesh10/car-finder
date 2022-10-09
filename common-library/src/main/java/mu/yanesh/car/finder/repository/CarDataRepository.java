package mu.yanesh.car.finder.repository;

import mu.yanesh.car.finder.models.extractor.CarData;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CarDataRepository extends BasicMongoCrudRepository<CarData> {

    public static final String COLLECTION_NAME = "car_data";

    public CarDataRepository(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }
}
