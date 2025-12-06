package com.lottery.mapper;

import com.lottery.entity.DrawResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface DrawResultMapper {
    @Select("SELECT * FROM draw_results WHERE lottery_code = #{lotteryCode} ORDER BY draw_date DESC LIMIT 1")
    DrawResult findLatestByCode(String lotteryCode);

    @Select("SELECT * FROM draw_results WHERE lottery_code = #{lotteryCode} ORDER BY draw_date DESC LIMIT 10")
    List<DrawResult> findAllByCode(String lotteryCode);

    @Select("SELECT * FROM draw_results WHERE lottery_code = #{lotteryCode} AND issue_number = #{issueNumber}")
    DrawResult findByCodeAndIssue(String lotteryCode, String issueNumber);
}
