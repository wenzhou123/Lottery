from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import random
import logging
from typing import Optional
from crawler import CrawlerFactory
from database import Database

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

app = FastAPI(title="彩票AI后端服务", version="1.0.0")

# 配置 CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=[
        "http://localhost",
        "http://localhost:80",
        "http://localhost:5173",
        "http://localhost:5174",
        "http://localhost:3000"
    ],
    allow_credentials=True,
    allow_methods=["GET", "POST", "PUT", "DELETE", "OPTIONS"],
    allow_headers=["*"],
    max_age=3600,
)

# 数据库实例
db = Database()

class PredictionRequest(BaseModel):
    lottery_code: str
    history_data: list = []

class CrawlRequest(BaseModel):
    lottery_code: str
    save_to_db: bool = True
    fetch_history: bool = False
    limit: int = 100

@app.on_event("startup")
async def startup_event():
    """应用启动时连接数据库"""
    if db.connect():
        logger.info("应用启动成功，数据库连接正常")
    else:
        logger.warning("数据库连接失败，某些功能可能不可用")

@app.on_event("shutdown")
async def shutdown_event():
    """应用关闭时断开数据库"""
    db.close()
    logger.info("应用已关闭")

@app.get("/")
def read_root():
    return {
        "service": "彩票AI后端服务",
        "version": "1.0.0",
        "features": ["AI预测", "数据爬取", "数据存储"]
    }

@app.get("/health")
def health_check():
    """健康检查接口"""
    return {"status": "healthy", "database": db.connection is not None}

@app.post("/predict")
def predict(request: PredictionRequest):
    """AI预测接口"""
    # 模拟AI思考过程 (CoT)
    cot_steps = [
        "1. 历史数据检索: 分析过去100期开奖号码...",
        "2. 趋势识别: 检测到红球区间[10-20]近期热度上升 (Confidence: 0.85)",
        "3. 冷热号分析: 蓝球 08 已遗漏 15 期，触发回补概率模型",
        "4. 模式匹配: 识别到 '三连号' 模式概率降低，排除连号组合",
        "5. 深度学习推理: LSTM模型预测下期红球和值范围 [90-110]",
        "6. 最终优化: 结合随机扰动因子生成最终推荐号码"
    ]

    # 模拟生成号码
    if request.lottery_code == 'ssq':
        # 双色球：6个红球(1-33) + 1个蓝球(1-16)
        red_balls = sorted(random.sample(range(1, 34), 6))
        blue_ball = random.randint(1, 16)
        prediction = f"{' '.join(f'{n:02d}' for n in red_balls)} + {blue_ball:02d}"
    elif request.lottery_code == 'dlt':
        # 大乐透：5个红球(1-35) + 2个蓝球(1-12)
        red_balls = sorted(random.sample(range(1, 36), 5))
        blue_balls = sorted(random.sample(range(1, 13), 2))
        prediction = f"{' '.join(f'{n:02d}' for n in red_balls)} + {' '.join(f'{n:02d}' for n in blue_balls)}"
    elif request.lottery_code == 'fc3d':
        # 福彩3D：3个数字(0-9)
        numbers = [random.randint(0, 9) for _ in range(3)]
        prediction = ' '.join(str(n) for n in numbers)
    elif request.lottery_code == 'kl8':
        # 快乐8：20个号码(1-80)
        numbers = sorted(random.sample(range(1, 81), 20))
        prediction = ' '.join(f'{n:02d}' for n in numbers)
    else:
        # 默认
        prediction = "01 02 03 04 05 06"

    return {
        "lottery_code": request.lottery_code,
        "prediction": prediction,
        "cot_analysis": "\n".join(cot_steps),
        "confidence": 0.85
    }

@app.post("/crawler/fetch")
def crawl_lottery_data(request: CrawlRequest):
    """
    爬取彩票数据接口
    供Java后端定时任务调用
    """
    try:
        # 获取爬虫实例
        crawler = CrawlerFactory.get_crawler(request.lottery_code)
        if not crawler:
            raise HTTPException(status_code=400, detail=f"不支持的彩票类型: {request.lottery_code}")

        # 爬取数据
        if request.fetch_history:
            logger.info(f"开始爬取{crawler.lottery_name}历史数据，数量: {request.limit}")
            data = crawler.fetch_history(limit=request.limit)
        else:
            logger.info(f"开始爬取{crawler.lottery_name}最新数据")
            latest = crawler.fetch_latest()
            data = [latest] if latest else []

        if not data:
            raise HTTPException(status_code=500, detail="爬取数据失败")

        # 保存到数据库
        saved_count = 0
        if request.save_to_db:
            if not db.connection:
                db.connect()

            saved_count = db.batch_insert_draw_results(data)
            logger.info(f"成功保存 {saved_count}/{len(data)} 条数据到数据库")

        return {
            "success": True,
            "lottery_code": request.lottery_code,
            "lottery_name": crawler.lottery_name,
            "fetched_count": len(data),
            "saved_count": saved_count,
            "data": data[:5]  # 只返回前5条作为示例
        }

    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"爬取数据失败: {e}")
        raise HTTPException(status_code=500, detail=f"爬取数据失败: {str(e)}")

@app.post("/crawler/fetch-all")
def crawl_all_lottery_data(save_to_db: bool = True, limit: int = 10):
    """
    爬取所有彩票的最新数据
    供Java后端定时任务调用
    """
    try:
        results = {}
        total_saved = 0

        # 获取所有爬虫
        crawlers = CrawlerFactory.get_all_crawlers()

        for lottery_code, crawler in crawlers.items():
            try:
                logger.info(f"开始爬取{crawler.lottery_name}数据")
                data = crawler.fetch_history(limit=limit)

                saved_count = 0
                if save_to_db and data:
                    if not db.connection:
                        db.connect()
                    saved_count = db.batch_insert_draw_results(data)
                    total_saved += saved_count

                results[lottery_code] = {
                    "lottery_name": crawler.lottery_name,
                    "fetched_count": len(data),
                    "saved_count": saved_count,
                    "status": "success"
                }

            except Exception as e:
                logger.error(f"爬取{crawler.lottery_name}失败: {e}")
                results[lottery_code] = {
                    "lottery_name": crawler.lottery_name,
                    "status": "failed",
                    "error": str(e)
                }

        return {
            "success": True,
            "total_saved": total_saved,
            "results": results
        }

    except Exception as e:
        logger.error(f"批量爬取失败: {e}")
        raise HTTPException(status_code=500, detail=f"批量爬取失败: {str(e)}")

@app.get("/crawler/latest/{lottery_code}")
def get_latest_issue(lottery_code: str):
    """
    获取指定彩票的最新期号
    """
    try:
        if not db.connection:
            db.connect()

        latest_issue = db.get_latest_issue(lottery_code)

        return {
            "lottery_code": lottery_code,
            "latest_issue": latest_issue
        }

    except Exception as e:
        logger.error(f"获取最新期号失败: {e}")
        raise HTTPException(status_code=500, detail=f"获取最新期号失败: {str(e)}")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
