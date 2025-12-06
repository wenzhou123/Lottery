import random
import logging
from datetime import datetime, timedelta
from typing import List, Dict, Optional
from .base_crawler import BaseCrawler

logger = logging.getLogger(__name__)

class SSQCrawler(BaseCrawler):
    """双色球爬虫"""

    def __init__(self):
        super().__init__('ssq', '双色球')

    def fetch_latest(self) -> Optional[Dict]:
        """
        获取最新一期双色球开奖数据
        注意: 这里使用模拟数据，实际应用需要替换为真实爬虫逻辑
        """
        try:
            # 模拟生成最新期数据
            today = datetime.now()
            issue_number = today.strftime('%Y') + str(random.randint(100, 150))

            red_balls = sorted(random.sample(range(1, 34), 6))
            blue_ball = random.randint(1, 16)

            return {
                'lottery_code': self.lottery_code,
                'issue_number': issue_number,
                'draw_date': today.strftime('%Y-%m-%d'),
                'red_balls': self.parse_ball_numbers(red_balls),
                'blue_balls': f"{blue_ball:02d}",
                'sales_amount': random.uniform(300000000, 500000000),
                'jackpot_pool': random.uniform(1000000000, 3000000000)
            }

        except Exception as e:
            logger.error(f"获取双色球最新数据失败: {e}")
            return None

    def fetch_history(self, limit: int = 100) -> List[Dict]:
        """
        获取双色球历史数据
        """
        results = []
        try:
            today = datetime.now()
            base_issue = int(today.strftime('%Y') + '001')

            for i in range(limit):
                issue_number = str(base_issue + i)
                draw_date = (today - timedelta(days=i*3)).strftime('%Y-%m-%d')

                red_balls = sorted(random.sample(range(1, 34), 6))
                blue_ball = random.randint(1, 16)

                results.append({
                    'lottery_code': self.lottery_code,
                    'issue_number': issue_number,
                    'draw_date': draw_date,
                    'red_balls': self.parse_ball_numbers(red_balls),
                    'blue_balls': f"{blue_ball:02d}",
                    'sales_amount': random.uniform(300000000, 500000000),
                    'jackpot_pool': random.uniform(1000000000, 3000000000)
                })

            logger.info(f"成功获取双色球历史数据 {len(results)} 条")
            return results

        except Exception as e:
            logger.error(f"获取双色球历史数据失败: {e}")
            return []


class DLTCrawler(BaseCrawler):
    """大乐透爬虫"""

    def __init__(self):
        super().__init__('dlt', '大乐透')

    def fetch_latest(self) -> Optional[Dict]:
        """获取最新一期大乐透数据"""
        try:
            today = datetime.now()
            issue_number = today.strftime('%Y') + str(random.randint(100, 150))

            red_balls = sorted(random.sample(range(1, 36), 5))
            blue_balls = sorted(random.sample(range(1, 13), 2))

            return {
                'lottery_code': self.lottery_code,
                'issue_number': issue_number,
                'draw_date': today.strftime('%Y-%m-%d'),
                'red_balls': self.parse_ball_numbers(red_balls),
                'blue_balls': self.parse_ball_numbers(blue_balls),
                'sales_amount': random.uniform(200000000, 400000000),
                'jackpot_pool': random.uniform(800000000, 2000000000)
            }

        except Exception as e:
            logger.error(f"获取大乐透最新数据失败: {e}")
            return None

    def fetch_history(self, limit: int = 100) -> List[Dict]:
        """获取大乐透历史数据"""
        results = []
        try:
            today = datetime.now()
            base_issue = int(today.strftime('%Y') + '001')

            for i in range(limit):
                issue_number = str(base_issue + i)
                draw_date = (today - timedelta(days=i*3)).strftime('%Y-%m-%d')

                red_balls = sorted(random.sample(range(1, 36), 5))
                blue_balls = sorted(random.sample(range(1, 13), 2))

                results.append({
                    'lottery_code': self.lottery_code,
                    'issue_number': issue_number,
                    'draw_date': draw_date,
                    'red_balls': self.parse_ball_numbers(red_balls),
                    'blue_balls': self.parse_ball_numbers(blue_balls),
                    'sales_amount': random.uniform(200000000, 400000000),
                    'jackpot_pool': random.uniform(800000000, 2000000000)
                })

            logger.info(f"成功获取大乐透历史数据 {len(results)} 条")
            return results

        except Exception as e:
            logger.error(f"获取大乐透历史数据失败: {e}")
            return []


class FC3DCrawler(BaseCrawler):
    """福彩3D爬虫"""

    def __init__(self):
        super().__init__('fc3d', '福彩3D')

    def fetch_latest(self) -> Optional[Dict]:
        """获取最新一期福彩3D数据"""
        try:
            today = datetime.now()
            issue_number = today.strftime('%Y') + str(random.randint(200, 365))

            balls = [random.randint(0, 9) for _ in range(3)]

            return {
                'lottery_code': self.lottery_code,
                'issue_number': issue_number,
                'draw_date': today.strftime('%Y-%m-%d'),
                'red_balls': ','.join([str(b) for b in balls]),
                'blue_balls': '',
                'sales_amount': random.uniform(50000000, 100000000),
                'jackpot_pool': 0
            }

        except Exception as e:
            logger.error(f"获取福彩3D最新数据失败: {e}")
            return None

    def fetch_history(self, limit: int = 100) -> List[Dict]:
        """获取福彩3D历史数据"""
        results = []
        try:
            today = datetime.now()
            base_issue = int(today.strftime('%Y') + '001')

            for i in range(limit):
                issue_number = str(base_issue + i)
                draw_date = (today - timedelta(days=i)).strftime('%Y-%m-%d')

                balls = [random.randint(0, 9) for _ in range(3)]

                results.append({
                    'lottery_code': self.lottery_code,
                    'issue_number': issue_number,
                    'draw_date': draw_date,
                    'red_balls': ','.join([str(b) for b in balls]),
                    'blue_balls': '',
                    'sales_amount': random.uniform(50000000, 100000000),
                    'jackpot_pool': 0
                })

            logger.info(f"成功获取福彩3D历史数据 {len(results)} 条")
            return results

        except Exception as e:
            logger.error(f"获取福彩3D历史数据失败: {e}")
            return []


class KL8Crawler(BaseCrawler):
    """快乐8爬虫"""

    def __init__(self):
        super().__init__('kl8', '快乐8')

    def fetch_latest(self) -> Optional[Dict]:
        """获取最新一期快乐8数据"""
        try:
            today = datetime.now()
            issue_number = today.strftime('%Y%m%d') + str(random.randint(1, 84))

            balls = sorted(random.sample(range(1, 81), 20))

            return {
                'lottery_code': self.lottery_code,
                'issue_number': issue_number,
                'draw_date': today.strftime('%Y-%m-%d'),
                'red_balls': self.parse_ball_numbers(balls),
                'blue_balls': '',
                'sales_amount': random.uniform(20000000, 50000000),
                'jackpot_pool': random.uniform(100000000, 500000000)
            }

        except Exception as e:
            logger.error(f"获取快乐8最新数据失败: {e}")
            return None

    def fetch_history(self, limit: int = 100) -> List[Dict]:
        """获取快乐8历史数据"""
        results = []
        try:
            today = datetime.now()

            for i in range(limit):
                draw_date = (today - timedelta(days=i//10)).strftime('%Y-%m-%d')
                issue_number = draw_date.replace('-', '') + str(random.randint(1, 84))

                balls = sorted(random.sample(range(1, 81), 20))

                results.append({
                    'lottery_code': self.lottery_code,
                    'issue_number': issue_number,
                    'draw_date': draw_date,
                    'red_balls': self.parse_ball_numbers(balls),
                    'blue_balls': '',
                    'sales_amount': random.uniform(20000000, 50000000),
                    'jackpot_pool': random.uniform(100000000, 500000000)
                })

            logger.info(f"成功获取快乐8历史数据 {len(results)} 条")
            return results

        except Exception as e:
            logger.error(f"获取快乐8历史数据失败: {e}")
            return []


class CrawlerFactory:
    """爬虫工厂类"""

    _crawlers = {
        'ssq': SSQCrawler,
        'dlt': DLTCrawler,
        'fc3d': FC3DCrawler,
        'kl8': KL8Crawler
    }

    @classmethod
    def get_crawler(cls, lottery_code: str) -> Optional[BaseCrawler]:
        """根据彩票代码获取对应的爬虫实例"""
        crawler_class = cls._crawlers.get(lottery_code)
        if crawler_class:
            return crawler_class()
        else:
            logger.error(f"不支持的彩票类型: {lottery_code}")
            return None

    @classmethod
    def get_all_crawlers(cls) -> Dict[str, BaseCrawler]:
        """获取所有爬虫实例"""
        return {code: crawler_class() for code, crawler_class in cls._crawlers.items()}
