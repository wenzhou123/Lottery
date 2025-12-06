package com.lottery.config;

import com.lottery.entity.DrawResult;
import com.lottery.mapper.DrawResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class DataSeeder implements CommandLineRunner {

        @Autowired
        private JdbcTemplate jdbcTemplate;

        @Autowired
        private DrawResultMapper drawResultMapper;

        @Override
        public void run(String... args) throws Exception {
                // Initialize Lottery Types if not exists
                Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM lottery_types", Integer.class);
                if (count != null && count == 0) {
                        jdbcTemplate.update("INSERT INTO lottery_types (code, name, description) VALUES (?, ?, ?)",
                                        "ssq", "双色球",
                                        "福彩双色球");
                        jdbcTemplate.update("INSERT INTO lottery_types (code, name, description) VALUES (?, ?, ?)",
                                        "dlt", "大乐透",
                                        "体彩大乐透");
                        jdbcTemplate.update("INSERT INTO lottery_types (code, name, description) VALUES (?, ?, ?)",
                                        "fc3d", "福彩3D",
                                        "福彩3D");
                        jdbcTemplate.update("INSERT INTO lottery_types (code, name, description) VALUES (?, ?, ?)",
                                        "kl8", "快乐8",
                                        "福彩快乐8");
                }

                // Seed Draw Results - DISABLED: Use real data from crawler instead
                // seedDrawResults("ssq", 33, 16, 6, 1, 30);
                // seedDrawResults("dlt", 35, 12, 5, 2, 30);
                // seedDrawResults("fc3d", 9, 0, 3, 0, 30);
                // seedDrawResults("kl8", 80, 0, 20, 0, 30);

                // Seed News
                Integer newsCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM news", Integer.class);
                if (newsCount != null && newsCount == 0) {
                        jdbcTemplate.update(
                                        "INSERT INTO news (title, content, category, publish_date) VALUES (?, ?, ?, NOW())",
                                        "千万大奖得主现身分享中奖故事", "一位幸运彩民在上周喜中大奖，这是他购彩五年来的首次...", "story");
                        jdbcTemplate.update(
                                        "INSERT INTO news (title, content, category, publish_date) VALUES (?, ?, ?, NOW())",
                                        "双色球规则迎来重大更新", "官方发布最新通知，自下月起，双色球玩法将有新的调整...", "rule");
                        jdbcTemplate.update(
                                        "INSERT INTO news (title, content, category, publish_date) VALUES (?, ?, ?, NOW())",
                                        "本市又一站点中出百万大奖！", "位于市中心的幸运彩票站再次诞生大奖，吸引众多彩民前往...", "station");
                        jdbcTemplate.update(
                                        "INSERT INTO news (title, content, category, publish_date) VALUES (?, ?, ?, NOW())",
                                        "专家分析：如何科学选择号码组合", "资深彩票分析师为您解读近期号码趋势，提供选号新思路...", "notice");
                }

                // Seed AI Predictions
                Integer aiCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ai_predictions", Integer.class);
                if (aiCount != null && aiCount == 0) {
                        String cot = "1. 历史数据检索: 分析过去100期开奖号码...\n" +
                                        "2. 趋势识别: 检测到红球区间[10-20]近期热度上升 (Confidence: 0.85)\n" +
                                        "3. 冷热号分析: 蓝球 08 已遗漏 15 期，触发回补概率模型\n" +
                                        "4. 模式匹配: 识别到 '三连号' 模式概率降低，排除连号组合\n" +
                                        "5. 深度学习推理: LSTM模型预测下期红球和值范围 [90-110]\n" +
                                        "6. 最终优化: 结合随机扰动因子生成最终推荐号码";

                        jdbcTemplate.update(
                                        "INSERT INTO ai_predictions (lottery_code, issue_number, predicted_numbers, cot_analysis, model_version, created_at) VALUES (?, ?, ?, ?, ?, NOW())",
                                        "ssq", "2023136", "05 12 17 21 28 31 + 08", cot, "v1.0");
                        jdbcTemplate.update(
                                        "INSERT INTO ai_predictions (lottery_code, issue_number, predicted_numbers, cot_analysis, model_version, created_at) VALUES (?, ?, ?, ?, ?, NOW())",
                                        "dlt", "2023136", "03 09 15 22 26 + 04 11", cot, "v1.0");
                }
        }

        private void seedDrawResults(String code, int redMax, int blueMax, int redCount, int blueCount, int count) {
                Integer existingCount = jdbcTemplate.queryForObject(
                                "SELECT COUNT(*) FROM draw_results WHERE lottery_code = ?", Integer.class, code);

                if (existingCount != null && existingCount == 0) {
                        Random random = new Random();
                        for (int i = 0; i < count; i++) {
                                String issue = String.valueOf(2023135 - i);

                                // Generate Red Balls
                                List<Integer> reds = new ArrayList<>();
                                while (reds.size() < redCount) {
                                        int num = random.nextInt(redMax) + 1;
                                        if (!reds.contains(num))
                                                reds.add(num);
                                }
                                Collections.sort(reds);
                                String redStr = reds.stream()
                                                .map(n -> String.format("%02d", n))
                                                .collect(Collectors.joining(","));

                                // Generate Blue Balls
                                String blueStr = null;
                                if (blueCount > 0) {
                                        List<Integer> blues = new ArrayList<>();
                                        while (blues.size() < blueCount) {
                                                int num = random.nextInt(blueMax) + 1;
                                                if (!blues.contains(num))
                                                        blues.add(num);
                                        }
                                        Collections.sort(blues);
                                        blueStr = blues.stream()
                                                        .map(n -> String.format("%02d", n))
                                                        .collect(Collectors.joining(","));
                                }

                                jdbcTemplate.update(
                                                "INSERT INTO draw_results (lottery_code, issue_number, draw_date, red_balls, blue_balls, sales_amount, jackpot_pool) VALUES (?, ?, DATE_ADD(CURRENT_DATE, INTERVAL ? DAY), ?, ?, ?, ?)",
                                                code, issue, -i * 2, redStr, blueStr,
                                                new BigDecimal(random.nextInt(100000000) + 300000000),
                                                new BigDecimal(random.nextInt(500000000) + 1000000000));
                        }
                }
        }
}
