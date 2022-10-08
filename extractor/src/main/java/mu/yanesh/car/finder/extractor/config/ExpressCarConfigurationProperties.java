package mu.yanesh.car.finder.extractor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "express-cars.extractor")
@Getter
@Setter
public class ExpressCarConfigurationProperties {

    private String baseUrl;
    private String defaultParams;
    private String transmissionFilter;
    private String pageParam;

}
