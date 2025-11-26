# 🎯 智能彩票分析系统 (Smart Lottery System)

一个集成了 AI 预测、数据分析和多端支持的智能彩票分析平台，提供专业的彩票数据查询、走势分析和智能预测服务。

## 📋 项目简介

智能彩票分析系统是一个全栈应用，支持多种主流彩票类型（双色球、大乐透、福彩3D、快乐8等），通过 AI 算法和历史数据分析，为用户提供智能化的号码推荐和走势预测。

### ✨ 核心特性

- 🎲 **多彩种支持**：支持双色球(SSQ)、大乐透(DLT)、福彩3D(FC3D)、快乐8(KL8)等主流彩票
- 🤖 **AI 智能预测**：基于深度学习的号码预测，提供 Chain of Thought (CoT) 分析过程
- 📊 **数据可视化**：ECharts 驱动的走势图、热力图、统计图表
- 📱 **多端支持**：Web端、Android、iOS、微信小程序全平台覆盖
- 📰 **资讯中心**：彩票新闻、中奖故事、玩法规则、投注站信息
- 🔍 **历史查询**：完整的开奖历史数据查询和分析
- 📈 **趋势分析**：号码冷热分析、遗漏统计、区间分布

## 🏗️ 技术架构

### 前端技术栈
- **框架**: Vue 3 + Vite
- **路由**: Vue Router 4
- **HTTP**: Axios
- **图表**: ECharts 6.0
- **样式**: 原生 CSS (现代化设计系统)

### 后端技术栈
- **语言**: Java 17
- **框架**: Spring Boot 3.1.5
- **ORM**: MyBatis 3.0.2
- **数据库**: MySQL / H2 (开发环境)
- **构建工具**: Maven

### AI 服务
- **框架**: FastAPI
- **服务器**: Uvicorn
- **算法**: NumPy + 自定义预测模型
- **特性**: CoT (Chain of Thought) 推理过程展示

## 📁 项目结构

```
Lottery/
├── frontend-web/          # Web 前端应用
│   ├── src/
│   │   ├── views/         # 页面组件
│   │   ├── components/    # 通用组件
│   │   ├── api/           # API 接口
│   │   ├── router/        # 路由配置
│   │   └── assets/        # 静态资源
│   ├── package.json
│   └── vite.config.js
│
├── backend-java/          # Java 后端服务
│   ├── src/main/
│   │   ├── java/com/lottery/
│   │   │   ├── controller/    # 控制器
│   │   │   ├── service/       # 业务逻辑
│   │   │   ├── mapper/        # MyBatis Mapper
│   │   │   └── model/         # 数据模型
│   │   └── resources/
│   │       ├── application.properties
│   │       └── mapper/        # MyBatis XML
│   └── pom.xml
│
├── backend-ai/            # AI 预测服务
│   ├── main.py            # FastAPI 应用
│   └── requirements.txt
│
├── database/              # 数据库脚本
│   └── schema.sql         # 数据库表结构
│
├── app-android/           # Android 应用
├── app-ios/               # iOS 应用
├── mini-program/          # 微信小程序
└── README.md
```

## 🚀 快速开始

### 环境要求

- **Node.js**: >= 18.0.0
- **Java**: 17
- **Python**: >= 3.8
- **MySQL**: >= 8.0 (可选，开发环境可使用 H2)
- **Maven**: >= 3.6

### 1️⃣ 数据库初始化

```bash
# 使用 MySQL
mysql -u root -p < database/schema.sql

# 或使用 H2 数据库（开发环境，无需额外配置）
```

### 2️⃣ 启动后端服务

```bash
cd backend-java

# 使用 Maven 运行
mvn spring-boot:run

# 或打包后运行
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

后端服务将在 `http://localhost:8080` 启动

### 3️⃣ 启动 AI 服务

```bash
cd backend-ai

# 安装依赖
pip install -r requirements.txt

# 启动服务
python main.py
```

AI 服务将在 `http://localhost:8000` 启动

### 4️⃣ 启动前端应用

```bash
cd frontend-web

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端应用将在 `http://localhost:5173` 启动

## 📱 移动端开发

### Android 应用
```bash
cd app-android
# 参考 app-android/README.md
```

### iOS 应用
```bash
cd app-ios
# 参考 app-ios/README.md
```

### 微信小程序
```bash
cd mini-program
# 使用微信开发者工具打开此目录
```

## 🎯 核心功能模块

### 1. 开奖查询
- 最新开奖结果实时展示
- 历史开奖数据查询
- 奖池金额和销售额统计

### 2. AI 预测
- 基于历史数据的智能号码推荐
- CoT 分析过程可视化
- 多种预测算法支持
- 置信度评分

### 3. 走势分析
- 号码走势图（网格式展示）
- 冷热号统计
- 遗漏值分析
- 区间分布图
- 和值走势

### 4. 资讯中心
- 彩票新闻动态
- 中奖故事分享
- 玩法规则说明
- 投注站信息查询

## 🔧 配置说明

### 后端配置 (`backend-java/src/main/resources/application.properties`)

```properties
# 服务器端口
server.port=8080

# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/lottery
spring.datasource.username=root
spring.datasource.password=your_password

# MyBatis 配置
mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.type-aliases-package=com.lottery.model
```

### 前端 API 配置 (`frontend-web/src/api/`)

```javascript
// 修改 API 基础地址
const API_BASE_URL = 'http://localhost:8080/api';
const AI_BASE_URL = 'http://localhost:8000';
```

## 📊 数据库设计

### 核心表结构

- **users**: 用户信息表
- **lottery_types**: 彩票种类表
- **draw_results**: 开奖结果表
- **news**: 资讯表
- **ai_predictions**: AI 预测记录表

详细表结构请参考 `database/schema.sql`

## 🧪 API 接口文档

### 后端 API (Java)

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/lottery/types` | GET | 获取彩票类型列表 |
| `/api/lottery/latest/{code}` | GET | 获取最新开奖结果 |
| `/api/lottery/history/{code}` | GET | 获取历史开奖数据 |
| `/api/news` | GET | 获取资讯列表 |
| `/api/news/{id}` | GET | 获取资讯详情 |

### AI API (Python)

| 接口 | 方法 | 说明 |
|------|------|------|
| `/predict` | POST | 获取 AI 预测结果 |

请求示例：
```json
{
  "lottery_code": "ssq",
  "history_data": []
}
```

响应示例：
```json
{
  "lottery_code": "ssq",
  "prediction": "03 12 18 25 28 31 + 08",
  "cot_analysis": "1. 历史数据检索...\n2. 趋势识别...",
  "confidence": 0.85
}
```

## 🎨 UI/UX 设计

- **现代化设计系统**：采用渐变色、玻璃态效果、微动画
- **深色模式支持**：护眼的深色主题
- **响应式布局**：完美适配各种屏幕尺寸
- **流畅动画**：平滑的页面过渡和交互反馈

## 🔒 安全说明

⚠️ **重要提示**：
- 本系统仅供学习和研究使用
- AI 预测结果仅供参考，不构成购彩建议
- 彩票投注存在风险，请理性购彩
- 禁止用于任何非法用途

## 📝 开发计划

- [ ] 增加更多彩票类型支持
- [ ] 优化 AI 预测算法
- [ ] 添加用户系统和个人中心
- [ ] 实现号码收藏和跟投功能
- [ ] 开发社区交流模块
- [ ] 集成第三方数据源
- [ ] Docker 容器化部署
- [ ] 性能优化和缓存策略

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目仅供学习交流使用。

## 👥 联系方式

如有问题或建议，欢迎通过以下方式联系：

- 提交 GitHub Issue
- 发送邮件至项目维护者

---

**⚠️ 免责声明**: 本系统仅用于技术学习和研究，不提供任何形式的购彩建议。彩票投注有风险，请理性娱乐，量力而行。
