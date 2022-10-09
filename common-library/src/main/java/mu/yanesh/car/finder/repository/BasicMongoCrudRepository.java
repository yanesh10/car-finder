package mu.yanesh.car.finder.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class BasicMongoCrudRepository <V> implements MongoCrudRepository<V> {

    private final MongoTemplate mongoTemplate;

    @Override
    public void save(V entity, String collectionName) {
        mongoTemplate.save(entity, collectionName);
    }

    @Override
    public Optional<V> findById(Object id, Class<V> entityType) {
        return Optional.ofNullable(mongoTemplate.findById(id, entityType));
    }

    @Override
    public Optional<V> findOne(Query query, Class<V> entityType) {
        return Optional.ofNullable(mongoTemplate.findOne(query, entityType));
    }

    @Override
    public List<V> findAll(Class<V> entityType, String collectionName) {
        return mongoTemplate.findAll(entityType, collectionName);
    }

    @Override
    public void deleteAll(String collectionName) {
        mongoTemplate.dropCollection(collectionName);
    }
}
