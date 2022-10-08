package mu.yanesh.car.finder.models.extractor;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarData {

    private String title;
    private String price;
    private String mileage;
    private String year;
    private String fuelType;
    private String originalLink;
    private String imageUrl;
    private String transmission;

}
