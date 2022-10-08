package mu.car.finder.extractor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/{api.version}/extractor")
public class ExtractorController {

    @GetMapping("/extract")
    public ResponseEntity<Boolean> extract() {

        return ResponseEntity.ok(true);
    }
}
