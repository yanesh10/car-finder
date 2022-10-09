package mu.yanesh.car.finder.models.extractor;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
@EqualsAndHashCode(callSuper = false)
public class CarData extends AbstractModel {

    private String title;
    private String price;
    private String mileage;
    private String year;
    private String fuelType;
    private String originalLink;
    private String imageUrl;
    private String transmission;

}
