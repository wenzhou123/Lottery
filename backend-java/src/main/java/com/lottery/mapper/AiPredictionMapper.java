package com.lottery.mapper;

import com.lottery.entity.AiPrediction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AiPredictionMapper {
    @Select("SELECT * FROM ai_predictions WHERE lottery_code = #{lotteryCode} ORDER BY created_at DESC LIMIT 1")
    AiPrediction findLatestByCode(String lotteryCode);
}
