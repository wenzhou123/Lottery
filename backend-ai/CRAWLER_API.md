# 彩票爬虫 API 文档

## 概述

AI后端提供了完整的彩票数据爬虫功能，可以爬取双色球、大乐透、福彩3D、快乐8的开奖信息，并自动存入数据库。

## API 接口

### 1. 健康检查

**接口**: `GET /health`

**描述**: 检查服务和数据库连接状态

**响应**:
```json
{
  "status": "healthy",
  "database": true
}
```

---

### 2. 爬取单个彩票数据

**接口**: `POST /crawler/fetch`

**描述**: 爬取指定彩票的开奖数据

**请求体**:
```json
{
  "lottery_code": "ssq",        // 彩票代码: ssq(双色球), dlt(大乐透), fc3d(福彩3D), kl8(快乐8)
  "save_to_db": true,           // 是否保存到数据库
  "fetch_history": false,       // 是否获取历史数据
  "limit": 100                  // 历史数据数量限制
}
```

**响应**:
```json
{
  "success": true,
  "lottery_code": "ssq",
  "lottery_name": "双色球",
  "fetched_count": 1,
  "saved_count": 1,
  "data": [
    {
      "lottery_code": "ssq",
      "issue_number": "2025135",
      "draw_date": "2025-12-06",
      "red_balls": "02,09,11,18,25,30",
      "blue_balls": "14",
      "sales_amount": 350000000,
      "jackpot_pool": 2150000000
    }
  ]
}
```

**示例**:
```bash
# 爬取双色球最新一期
curl -X POST http://localhost:8000/crawler/fetch \
  -H "Content-Type: application/json" \
  -d '{"lottery_code": "ssq", "save_to_db": true}'

# 爬取大乐透最近100期
curl -X POST http://localhost:8000/crawler/fetch \
  -H "Content-Type: application/json" \
  -d '{"lottery_code": "dlt", "fetch_history": true, "limit": 100}'
```

---

### 3. 批量爬取所有彩票数据

**接口**: `POST /crawler/fetch-all`

**描述**: 一次性爬取所有彩票的数据（推荐用于定时任务）

**参数**:
- `save_to_db` (query): 是否保存到数据库，默认 true
- `limit` (query): 每种彩票爬取的数量，默认 10

**响应**:
```json
{
  "success": true,
  "total_saved": 40,
  "results": {
    "ssq": {
      "lottery_name": "双色球",
      "fetched_count": 10,
      "saved_count": 10,
      "status": "success"
    },
    "dlt": {
      "lottery_name": "大乐透",
      "fetched_count": 10,
      "saved_count": 10,
      "status": "success"
    },
    "fc3d": {
      "lottery_name": "福彩3D",
      "fetched_count": 10,
      "saved_count": 10,
      "status": "success"
    },
    "kl8": {
      "lottery_name": "快乐8",
      "fetched_count": 10,
      "saved_count": 10,
      "status": "success"
    }
  }
}
```

**示例**:
```bash
# 爬取所有彩票最近10期数据
curl -X POST "http://localhost:8000/crawler/fetch-all?save_to_db=true&limit=10"
```

---

### 4. 获取最新期号

**接口**: `GET /crawler/latest/{lottery_code}`

**描述**: 获取数据库中指定彩票的最新期号

**响应**:
```json
{
  "lottery_code": "ssq",
  "latest_issue": "2025135"
}
```

**示例**:
```bash
curl http://localhost:8000/crawler/latest/ssq
```

---

## Java后端集成示例

### 1. 定时任务配置

在Java后端添加定时任务，定期调用爬虫API更新数据：

```java
@Component
public class LotteryCrawlerScheduler {

    @Autowired
    private RestTemplate restTemplate;

    private static final String AI_SERVICE_URL = "http://lottery-ai-1:8000";

    // 每天凌晨2点执行
    @Scheduled(cron = "0 0 2 * * ?")
    public void crawlAllLotteryData() {
        try {
            String url = AI_SERVICE_URL + "/crawler/fetch-all?save_to_db=true&limit=10";
            ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("彩票数据爬取成功: {}", response.getBody());
            }
        } catch (Exception e) {
            logger.error("彩票数据爬取失败", e);
        }
    }

    // 每小时执行一次，爬取最新数据
    @Scheduled(fixedRate = 3600000)
    public void crawlLatestData() {
        String[] lotteryCodes = {"ssq", "dlt", "fc3d", "kl8"};

        for (String code : lotteryCodes) {
            try {
                String url = AI_SERVICE_URL + "/crawler/fetch";

                Map<String, Object> request = new HashMap<>();
                request.put("lottery_code", code);
                request.put("save_to_db", true);
                request.put("fetch_history", false);

                ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
                logger.info("爬取{}成功", code);
            } catch (Exception e) {
                logger.error("爬取{}失败", code, e);
            }
        }
    }
}
```

### 2. RestTemplate配置

```java
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

---

## 注意事项

1. **数据源**: 当前使用模拟数据，实际部署时需要替换为真实的彩票数据源
2. **反爬机制**: 建议添加请求间隔、随机User-Agent等反反爬措施
3. **数据验证**: 爬取的数据应进行格式验证和去重处理
4. **错误处理**: 爬虫失败时应有重试机制和告警通知
5. **数据库连接**: 确保AI服务能够访问MySQL数据库

---

## 支持的彩票类型

| 代码 | 名称 | 红球范围 | 蓝球范围 |
|------|------|----------|----------|
| ssq | 双色球 | 1-33 (6个) | 1-16 (1个) |
| dlt | 大乐透 | 1-35 (5个) | 1-12 (2个) |
| fc3d | 福彩3D | 0-9 (3个) | 无 |
| kl8 | 快乐8 | 1-80 (20个) | 无 |
