package mu.car.finder.extractor.controller;

import lombok.AllArgsConstructor;
import mu.car.finder.extractor.extractor.IExtractor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${api.version}/extractor")
@AllArgsConstructor
public class ExtractorController {

    private final IExtractor extractor;

    @GetMapping("/extract")
    public ResponseEntity<Boolean> extract() {
        extractor.extract();
        return ResponseEntity.ok(true);
    }
}
