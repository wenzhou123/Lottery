package com.lottery.service;

import com.lottery.entity.News;
import com.lottery.mapper.NewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NewsService {
    @Autowired
    private NewsMapper newsMapper;

    public List<News> getAllNews() {
        return newsMapper.findAll();
    }

    public List<News> getNewsByCategory(String category) {
        return newsMapper.findByCategory(category);
    }
}
