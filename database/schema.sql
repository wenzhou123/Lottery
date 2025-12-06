-- 用户表
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50),
    avatar_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 彩票种类表
CREATE TABLE lottery_types (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE, -- e.g., 'ssq', 'dlt'
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255)
);

-- 开奖结果表
CREATE TABLE draw_results (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lottery_code VARCHAR(20) NOT NULL,
    issue_number VARCHAR(20) NOT NULL, -- 期号
    draw_date DATE NOT NULL,
    red_balls VARCHAR(255) NOT NULL, -- 红球，逗号分隔
    blue_balls VARCHAR(255), -- 蓝球，逗号分隔
    sales_amount DECIMAL(15, 2), -- 销售额
    jackpot_pool DECIMAL(15, 2), -- 奖池
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_issue (lottery_code, issue_number)
);

-- 资讯表
CREATE TABLE news (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(20) NOT NULL, -- 'story', 'rule', 'station', 'notice'
    image_url VARCHAR(255),
    publish_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    views INT DEFAULT 0
);

-- AI预测记录表
CREATE TABLE ai_predictions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lottery_code VARCHAR(20) NOT NULL,
    issue_number VARCHAR(20) NOT NULL,
    predicted_numbers VARCHAR(100) NOT NULL,
    cot_analysis TEXT, -- Chain of Thought 分析过程
    model_version VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 初始化数据
INSERT INTO
    lottery_types (code, name, description)
VALUES ('ssq', '双色球', '福彩双色球'),
    ('dlt', '大乐透', '体彩大乐透'),
    ('fc3d', '福彩3D', '福彩3D'),
    ('kl8', '快乐8', '福彩快乐8');

INSERT INTO draw_results (lottery_code, issue_number, draw_date, red_balls, blue_balls, sales_amount, jackpot_pool) VALUES
('ssq', '2023140', '2023-12-05', '03,08,12,19,25,32', '07', 350000000.00, 2400000000.00),
('ssq', '2023139', '2023-12-03', '01,05,09,13,22,28', '11', 342000000.00, 2380000000.00),
('dlt', '23140', '2023-12-06', '04,06,12,24,35', '01,12', 290000000.00, 1800000000.00),
('dlt', '23139', '2023-12-04', '02,11,15,28,30', '03,09', 285000000.00, 1750000000.00),
('fc3d', '2023326', '2023-12-06', '5,2,9', NULL, 50000000.00, 0.00),
('kl8', '2023326', '2023-12-06', '01,02,05,08,11,15,19,22,25,28,33,36,44,48,52,55,59,66,70,77', NULL, 80000000.00, 0.00);