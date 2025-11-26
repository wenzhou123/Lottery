package com.lottery.service;

import com.lottery.entity.AiPrediction;
import com.lottery.mapper.AiPredictionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AiPredictionService {
    @Autowired
    private AiPredictionMapper aiPredictionMapper;

    public AiPrediction getLatestPrediction(String lotteryCode) {
        return aiPredictionMapper.findLatestByCode(lotteryCode);
    }
}
