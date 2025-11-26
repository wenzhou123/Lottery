package com.lottery.controller;

import com.lottery.entity.AiPrediction;
import com.lottery.service.AiPredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiPredictionController {
    @Autowired
    private AiPredictionService aiPredictionService;

    @GetMapping("/prediction")
    public AiPrediction getPrediction(@RequestParam String code) {
        return aiPredictionService.getLatestPrediction(code);
    }
}
