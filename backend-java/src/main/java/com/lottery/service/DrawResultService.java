package com.lottery.service;

import com.lottery.entity.DrawResult;
import com.lottery.mapper.DrawResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DrawResultService {
    @Autowired
    private DrawResultMapper drawResultMapper;

    public DrawResult getLatest(String lotteryCode) {
        return drawResultMapper.findLatestByCode(lotteryCode);
    }

    public List<DrawResult> getHistory(String lotteryCode) {
        return drawResultMapper.findAllByCode(lotteryCode);
    }
}
