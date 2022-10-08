package mu.yanesh.car.finder.extractor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mycar.extractor")
@Setter
@Getter
public class MyCarConfigurationProperties {

    private String baseUrl;
    private String defaultParams;
    private String transmissionFilter;
    private String fuelTypeFilter;
    private String pageParam;
}
