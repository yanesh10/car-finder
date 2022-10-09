package mu.yanesh.car.finder.extractor;

import mu.yanesh.car.finder.repository.CarDataRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan(basePackages = "mu.yanesh.car.finder")
@EnableMongoRepositories(basePackageClasses = CarDataRepository.class)
public class ExtractorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExtractorApplication.class, args);
    }

}
