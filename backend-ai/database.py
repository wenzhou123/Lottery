import pymysql
from typing import List, Dict, Optional
from config import DB_CONFIG
import logging

logger = logging.getLogger(__name__)

class Database:
    def __init__(self):
        self.config = DB_CONFIG
        self.connection = None

    def connect(self):
        """建立数据库连接"""
        try:
            self.connection = pymysql.connect(**self.config)
            logger.info("数据库连接成功")
            return True
        except Exception as e:
            logger.error(f"数据库连接失败: {e}")
            return False

    def close(self):
        """关闭数据库连接"""
        if self.connection:
            self.connection.close()
            logger.info("数据库连接已关闭")

    def insert_draw_result(self, lottery_code: str, issue_number: str,
                          draw_date: str, red_balls: str, blue_balls: str,
                          sales_amount: Optional[float] = None,
                          jackpot_pool: Optional[float] = None) -> bool:
        """
        插入开奖结果
        """
        try:
            with self.connection.cursor() as cursor:
                # 检查是否已存在
                check_sql = """
                    SELECT id FROM draw_results
                    WHERE lottery_code = %s AND issue_number = %s
                """
                cursor.execute(check_sql, (lottery_code, issue_number))
                if cursor.fetchone():
                    logger.info(f"期号 {issue_number} 已存在，跳过插入")
                    return False

                # 插入新数据
                insert_sql = """
                    INSERT INTO draw_results
                    (lottery_code, issue_number, draw_date, red_balls, blue_balls,
                     sales_amount, jackpot_pool, created_at)
                    VALUES (%s, %s, %s, %s, %s, %s, %s, NOW())
                """
                cursor.execute(insert_sql, (
                    lottery_code, issue_number, draw_date, red_balls, blue_balls,
                    sales_amount, jackpot_pool
                ))
                self.connection.commit()
                logger.info(f"成功插入期号 {issue_number} 的开奖数据")
                return True
        except Exception as e:
            self.connection.rollback()
            logger.error(f"插入开奖数据失败: {e}")
            return False

    def batch_insert_draw_results(self, results: List[Dict]) -> int:
        """
        批量插入开奖结果
        返回成功插入的数量
        """
        success_count = 0
        for result in results:
            if self.insert_draw_result(
                lottery_code=result.get('lottery_code'),
                issue_number=result.get('issue_number'),
                draw_date=result.get('draw_date'),
                red_balls=result.get('red_balls'),
                blue_balls=result.get('blue_balls'),
                sales_amount=result.get('sales_amount'),
                jackpot_pool=result.get('jackpot_pool')
            ):
                success_count += 1
        return success_count

    def get_latest_issue(self, lottery_code: str) -> Optional[str]:
        """获取最新期号"""
        try:
            with self.connection.cursor() as cursor:
                sql = """
                    SELECT issue_number FROM draw_results
                    WHERE lottery_code = %s
                    ORDER BY draw_date DESC, issue_number DESC
                    LIMIT 1
                """
                cursor.execute(sql, (lottery_code,))
                result = cursor.fetchone()
                return result[0] if result else None
        except Exception as e:
            logger.error(f"获取最新期号失败: {e}")
            return None
