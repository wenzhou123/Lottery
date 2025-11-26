package com.lottery.controller;

import com.lottery.entity.DrawResult;
import com.lottery.service.DrawResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/draw")
@CrossOrigin(origins = "*")
public class DrawResultController {
    @Autowired
    private DrawResultService drawResultService;

    @GetMapping("/latest")
    public DrawResult getLatest(@RequestParam String code) {
        return drawResultService.getLatest(code);
    }

    @GetMapping("/history")
    public List<DrawResult> getHistory(@RequestParam String code) {
        return drawResultService.getHistory(code);
    }
}
