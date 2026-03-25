package ee.nimens.inbank.solution.backend.controller;

import ee.nimens.inbank.solution.backend.service.DecisionService;
import ee.nimens.inbank.solution.backend.dto.RequestDecision;
import ee.nimens.inbank.solution.backend.dto.ResponseDecision;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/decision")
@RequiredArgsConstructor
public class DecisionController {

    private final DecisionService decisionService;

    @PostMapping
    public ResponseEntity<ResponseDecision> makeDecision(@Valid @RequestBody RequestDecision request) {
        ResponseDecision response = decisionService.makeDecision(request);
        return ResponseEntity.ok().body(response);
    }

}
