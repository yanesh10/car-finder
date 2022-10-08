package mu.yanesh.car.finder.extractor.controller;

import lombok.AllArgsConstructor;
import mu.yanesh.car.finder.extractor.extractor.IExtractor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/${api.version}/extractor")
@AllArgsConstructor
public class ExtractorController {

    private final List<IExtractor> extractors;

    @GetMapping("/extract")
    public ResponseEntity<Boolean> extract() {
        extractors.forEach(IExtractor::extract);
        return ResponseEntity.ok(true);
    }
}
