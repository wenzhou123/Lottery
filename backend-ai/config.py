import os
from dotenv import load_dotenv

load_dotenv()

# 数据库配置
DB_CONFIG = {
    'host': os.getenv('DB_HOST', 'lottery-db-1'),
    'port': int(os.getenv('DB_PORT', 3306)),
    'user': os.getenv('DB_USER', 'root'),
    'password': os.getenv('DB_PASSWORD', 'password'),
    'database': os.getenv('DB_NAME', 'lottery_db'),
    'charset': 'utf8mb4'
}

# 爬虫配置
CRAWLER_CONFIG = {
    'user_agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
    'timeout': 10,
    'retry_times': 3,
    'retry_delay': 2
}

# 彩票数据源配置（使用公开的彩票开奖信息网站）
LOTTERY_SOURCES = {
    'ssq': {
        'name': '双色球',
        'url': 'https://www.cwl.gov.cn/cwl_admin/front/cwlkj/search/kjxx/findDrawNotice',
        'params': {'name': 'ssq'}
    },
    'dlt': {
        'name': '大乐透',
        'url': 'https://webapi.sporttery.cn/gateway/lottery/getHistoryPageListV1.qry',
        'params': {'gameNo': 85}
    },
    'fc3d': {
        'name': '福彩3D',
        'url': 'https://www.cwl.gov.cn/cwl_admin/front/cwlkj/search/kjxx/findDrawNotice',
        'params': {'name': '3d'}
    },
    'kl8': {
        'name': '快乐8',
        'url': 'https://www.cwl.gov.cn/cwl_admin/front/cwlkj/search/kjxx/findDrawNotice',
        'params': {'name': 'kl8'}
    }
}
