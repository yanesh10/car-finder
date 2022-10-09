package mu.yanesh.car.finder.api.controller;

import lombok.AllArgsConstructor;
import mu.yanesh.car.finder.api.service.ExtractorService;
import mu.yanesh.car.finder.models.extractor.CarData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/${api.version}/carData")
@AllArgsConstructor
public class CarDataController {

    private final ExtractorService extractorService;

    @GetMapping("/")
    public ResponseEntity<List<CarData>> getAllCarData() {
        return ResponseEntity.ok(extractorService.findAll());
    }
}
