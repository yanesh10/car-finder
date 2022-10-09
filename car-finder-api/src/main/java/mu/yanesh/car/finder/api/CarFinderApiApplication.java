package mu.yanesh.car.finder.api;

import mu.yanesh.car.finder.repository.CarDataRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan("mu.yanesh.car.finder")
@EnableMongoRepositories(repositoryBaseClass = CarDataRepository.class)
public class CarFinderApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarFinderApiApplication.class, args);
    }

}
