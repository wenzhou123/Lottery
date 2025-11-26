package com.lottery.controller;

import com.lottery.entity.News;
import com.lottery.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "*")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @GetMapping("/list")
    public List<News> getList(@RequestParam(required = false) String category) {
        if (category != null) {
            return newsService.getNewsByCategory(category);
        }
        return newsService.getAllNews();
    }
}
