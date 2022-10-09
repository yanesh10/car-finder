package mu.yanesh.car.finder.models.extractor;

import org.springframework.data.annotation.Id;

public abstract class AbstractModel {

    @Id
    private String id;
}
