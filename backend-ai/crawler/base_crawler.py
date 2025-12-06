import requests
import time
import logging
from typing import List, Dict, Optional
from abc import ABC, abstractmethod
from config import CRAWLER_CONFIG

logger = logging.getLogger(__name__)

class BaseCrawler(ABC):
    """彩票爬虫基类"""

    def __init__(self, lottery_code: str, lottery_name: str):
        self.lottery_code = lottery_code
        self.lottery_name = lottery_name
        self.config = CRAWLER_CONFIG
        self.session = requests.Session()
        self.session.headers.update({
            'User-Agent': self.config['user_agent']
        })

    @abstractmethod
    def fetch_latest(self) -> Optional[Dict]:
        """
        获取最新一期开奖数据
        返回格式: {
            'lottery_code': str,
            'issue_number': str,
            'draw_date': str,
            'red_balls': str,
            'blue_balls': str,
            'sales_amount': float,
            'jackpot_pool': float
        }
        """
        pass

    @abstractmethod
    def fetch_history(self, limit: int = 100) -> List[Dict]:
        """
        获取历史开奖数据
        """
        pass

    def _request_with_retry(self, url: str, method: str = 'GET',
                           params: Dict = None, json_data: Dict = None,
                           headers: Dict = None) -> Optional[requests.Response]:
        """
        带重试机制的请求
        """
        retry_times = self.config['retry_times']
        retry_delay = self.config['retry_delay']
        timeout = self.config['timeout']

        for attempt in range(retry_times):
            try:
                if method.upper() == 'GET':
                    response = self.session.get(
                        url, params=params, headers=headers, timeout=timeout
                    )
                else:
                    response = self.session.post(
                        url, json=json_data, headers=headers, timeout=timeout
                    )

                response.raise_for_status()
                return response

            except Exception as e:
                logger.warning(f"请求失败 (尝试 {attempt + 1}/{retry_times}): {e}")
                if attempt < retry_times - 1:
                    time.sleep(retry_delay)
                else:
                    logger.error(f"请求最终失败: {url}")
                    return None

    def parse_ball_numbers(self, balls: List[str]) -> str:
        """
        格式化球号
        """
        return ','.join([f"{int(b):02d}" for b in balls])
