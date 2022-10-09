package mu.yanesh.car.finder.api.model;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class Filter {

    @Nullable
    private Boolean isHybrid;

    @Nullable
    private Integer minimumYear;

    @Nullable
    private Integer price;

}
