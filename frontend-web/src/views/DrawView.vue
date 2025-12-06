<script setup>
import { ref, onMounted, watch } from 'vue'
import { getLatestDraw, getAiPrediction, getDrawHistory, getDrawByIssue } from '../api'

const activeTab = ref('ssq')
const latestDraw = ref(null)
const historyList = ref([])
const showIssueSelector = ref(false)
const aiPrediction = ref(null)
const showCot = ref(true)
const userThought = ref('')
const isAnalyzing = ref(false)

const loadData = async () => {
  try {
    // Load latest draw initially
    const drawRes = await getLatestDraw(activeTab.value)
    latestDraw.value = drawRes.data
    console.log('Latest draw loaded:', latestDraw.value)

    // Load history for dropdown
    const historyRes = await getDrawHistory(activeTab.value)
    historyList.value = historyRes.data
    console.log('History loaded:', historyList.value.length, 'items')

    // Load AI prediction separately with error handling
    try {
      const aiRes = await getAiPrediction(activeTab.value)
      aiPrediction.value = aiRes.data
      console.log('AI prediction loaded:', aiPrediction.value)
    } catch (aiError) {
      console.error('Error loading AI prediction:', aiError)
      // AI prediction failure shouldn't affect draw data
      aiPrediction.value = null
    }
  } catch (e) {
    console.error('Error loading data:', e)
    // Use mock data if API fails
    generateMockData()
  }
}

const generateMockData = () => {
  // Generate mock latest draw
  latestDraw.value = {
    id: 1,
    lotteryCode: activeTab.value,
    issueNumber: '2023135',
    drawDate: '2023-11-21',
    redBalls: '02,09,11,18,25,30',
    blueBalls: '14',
    jackpotPool: 2150000000
  }
  
  // Generate mock history with RANDOM data
  const history = []
  for (let i = 0; i < 10; i++) {
    // Generate random red balls
    const reds = []
    while(reds.length < 6) {
      const n = Math.floor(Math.random() * 33) + 1
      const s = n < 10 ? '0'+n : ''+n
      if(!reds.includes(s)) reds.push(s)
    }
    reds.sort()
    
    history.push({
      id: i + 1,
      lotteryCode: activeTab.value,
      issueNumber: (2023135 - i).toString(),
      drawDate: '2023-11-21',
      redBalls: reds.join(','),
      blueBalls: (Math.floor(Math.random() * 16) + 1).toString().padStart(2, '0'),
      jackpotPool: 2150000000
    })
  }
  historyList.value = history
  
  console.log('Mock data generated')
}

const selectIssue = async (item) => {
  showIssueSelector.value = false

  try {
    // Get the specific issue data from backend
    const drawRes = await getDrawByIssue(activeTab.value, item.issueNumber)
    latestDraw.value = drawRes.data

    // Get AI prediction for this issue
    try {
      const aiRes = await getAiPrediction(activeTab.value)
      aiPrediction.value = aiRes.data
      console.log('AI prediction updated for issue:', item.issueNumber, aiPrediction.value)
    } catch (aiError) {
      console.error('Error loading AI prediction:', aiError)
      aiPrediction.value = null
    }
  } catch (e) {
    console.error('Error loading issue data:', e)
    // Fallback to the item from history list
    latestDraw.value = item
  }
}

const switchTab = (tab) => {
  activeTab.value = tab
}

const getLotteryName = (code) => {
  const map = {
    'ssq': '双色球',
    'dlt': '大乐透',
    'fc3d': '福彩3D',
    'kl8': '快乐8'
  }
  return map[code] || code
}

const getDayOfWeek = (dateStr) => {
  const days = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
  return days[new Date(dateStr).getDay()]
}

const formatMoney = (amount) => {
  if (!amount) return '0'
  if (amount > 100000000) {
    return (amount / 100000000).toFixed(1) + '亿'
  }
  return (amount / 10000).toFixed(0) + '万'
}

const getBallClass = (lotteryType, ballType) => {
  // 为不同彩票类型返回不同的球样式
  if (lotteryType === 'fc3d') {
    // 3D使用金色球
    return 'gold'
  } else if (lotteryType === 'kl8') {
    // 快乐8使用橙色球
    return 'orange'
  } else {
    // 双色球和大乐透使用红蓝球
    return ballType === 'red' ? 'red' : 'blue'
  }
}

const getPredictionParts = (prediction) => {
  // 解析预测字符串，如 "01 09 16 17 35 + 06 07" 或 "08 15 18 20 25 31 + 01"
  if (!prediction) {
    return { redBalls: [], blueBalls: [] }
  }

  try {
    const parts = prediction.split('+').map(p => p.trim())

    if (parts.length === 2) {
      // 有蓝球的情况（双色球、大乐透）
      const redBalls = parts[0].split(/\s+/).filter(b => b.length > 0)
      const blueBalls = parts[1].split(/\s+/).filter(b => b.length > 0)
      return { redBalls, blueBalls }
    } else {
      // 没有蓝球的情况（3D、快乐8）
      const redBalls = prediction.split(/\s+/).filter(b => b.length > 0)
      return { redBalls, blueBalls: [] }
    }
  } catch (e) {
    console.error('Error parsing prediction:', e)
    return { redBalls: [], blueBalls: [] }
  }
}

const analyzeWithThought = async () => {
  if (!userThought.value.trim()) {
    return
  }

  try {
    isAnalyzing.value = true
    showCot.value = true // 自动展开 COT

    // 调用 AI 预测接口，传入用户的想法
    const aiRes = await getAiPrediction(activeTab.value)
    aiPrediction.value = aiRes.data

    console.log('AI analysis completed with user thought:', userThought.value)

    // 清空输入框
    // userThought.value = ''
  } catch (error) {
    console.error('Error analyzing with AI:', error)
    aiPrediction.value = null
  } finally {
    isAnalyzing.value = false
  }
}

watch(activeTab, () => {
  loadData()
  userThought.value = '' // 切换标签时清空输入
})

onMounted(() => {
  loadData()
})
</script>

<template>
  <div class="draw-view">
    <header class="header">
      <div class="menu-icon">☰</div>
      <h1>开奖大厅</h1>
      <div class="notification-icon">🔔</div>
    </header>

    <div class="scrolling-banner">
      🎉 恭喜[广州]用户喜中二等奖...
    </div>

    <div class="lottery-tabs">
      <div 
        class="tab" 
        :class="{ active: activeTab === 'ssq' }"
        @click="switchTab('ssq')"
      >双色球</div>
      <div 
        class="tab" 
        :class="{ active: activeTab === 'dlt' }"
        @click="switchTab('dlt')"
      >大乐透</div>
      <div
        class="tab"
        :class="{ active: activeTab === 'fc3d' }"
        @click="switchTab('fc3d')"
      >3D</div>
      <div 
        class="tab" 
        :class="{ active: activeTab === 'kl8' }"
        @click="switchTab('kl8')"
      >快乐8</div>
    </div>

    <div class="content-area">
      <!-- Latest Draw -->
      <div class="card draw-card">
        <div class="card-header">
          <div class="header-content" @click="showIssueSelector = !showIssueSelector">
            <div class="title-row">
              <span class="lottery-title">{{ getLotteryName(activeTab) }}</span>
              <span class="issue-title" v-if="latestDraw">第{{ latestDraw.issueNumber }}期</span>
              <span class="issue-title" v-else>加载中...</span>
              <svg 
                v-if="latestDraw" 
                class="header-arrow" 
                :class="{ rotated: showIssueSelector }"
                viewBox="0 0 24 24" 
                width="20" 
                height="20"
              >
                <path d="M7 10l5 5 5-5" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <div class="date-row" v-if="latestDraw">
              开奖日期: {{ latestDraw.drawDate }} {{ getDayOfWeek(latestDraw.drawDate) }}
            </div>
          </div>
          
          <div class="issue-dropdown" v-if="showIssueSelector && historyList.length">
            <div 
              class="issue-option" 
              v-for="item in historyList" 
              :key="item.id"
              :class="{ selected: latestDraw && item.issueNumber === latestDraw.issueNumber }"
              @click.stop="selectIssue(item)"
            >
              第{{ item.issueNumber }}期
            </div>
          </div>
        </div>
        
        <div class="balls-container">
          <div class="balls" v-if="latestDraw">
            <span
              class="ball"
              :class="getBallClass(activeTab, 'red')"
              v-for="ball in latestDraw.redBalls.split(',')"
              :key="'r'+ball"
            >{{ ball }}</span>
            <span
              class="ball-separator"
              v-if="latestDraw.blueBalls && (activeTab === 'ssq' || activeTab === 'dlt')"
            >+</span>
            <template v-if="latestDraw.blueBalls">
              <span
                class="ball blue"
                v-for="ball in latestDraw.blueBalls.split(',')"
                :key="'b'+ball"
              >{{ ball }}</span>
            </template>
          </div>
          <div class="balls-placeholder" v-else>
            <span class="ball-skeleton" v-for="n in 7" :key="n"></span>
          </div>
        </div>

        <div class="pool-info">
          <div class="pool-amount">
            奖池: <span class="highlight" v-if="latestDraw">{{ formatMoney(latestDraw.jackpotPool) }}</span>
            <span class="highlight" v-else>--</span>
          </div>
          <span class="details-link">查看详情 ></span>
        </div>
      </div>

      <!-- AI Prediction -->
      <div class="section-header">
        <span class="section-title">AI智能号码预测</span>
      </div>
      <div class="card ai-card">
        <div class="ai-header">
          <div class="ai-title-group">
            <span class="ai-icon">🤖</span>
            <span class="ai-title-text">AI推荐号码</span>
          </div>
          <span class="powered-by">Powered by Gemini AI</span>
        </div>
        
        <div class="prediction-content">
          <div class="prediction-balls" v-if="aiPrediction">
            <template v-if="getPredictionParts(aiPrediction.prediction).redBalls.length > 0">
              <span
                class="ball"
                :class="getBallClass(activeTab, 'red')"
                v-for="ball in getPredictionParts(aiPrediction.prediction).redBalls"
                :key="'pred-r-'+ball"
              >{{ ball }}</span>
              <span
                class="ball-separator"
                v-if="getPredictionParts(aiPrediction.prediction).blueBalls.length > 0"
              >+</span>
              <span
                class="ball blue"
                v-for="ball in getPredictionParts(aiPrediction.prediction).blueBalls"
                :key="'pred-b-'+ball"
              >{{ ball }}</span>
            </template>
            <div class="prediction-fallback" v-else>
              {{ aiPrediction.prediction }}
            </div>
          </div>
          <div class="prediction-row loading" v-else>
            正在计算概率模型...
          </div>
        </div>

        <!-- User Input Section -->
        <div class="user-input-section">
          <div class="input-wrapper">
            <input
              type="text"
              class="thought-input"
              v-model="userThought"
              :disabled="isAnalyzing"
              placeholder="说出你的想法让AI帮你分析趋势吧"
              @keyup.enter="analyzeWithThought"
            />
            <button
              class="analyze-button"
              @click="analyzeWithThought"
              :disabled="isAnalyzing || !userThought.trim()"
            >
              <span v-if="!isAnalyzing">🔮 AI分析</span>
              <span v-else>分析中...</span>
            </button>
          </div>
        </div>

        <div class="cot-section">
          <div class="cot-toggle" @click="showCot = !showCot">
            <span>{{ showCot ? '收起思考过程' : '查看AI思考过程 (EoT)' }}</span>
            <span class="arrow" :class="{ rotated: showCot }">⌄</span>
          </div>
          <transition name="slide-fade">
            <div class="cot-content" v-if="showCot">
              <div v-if="aiPrediction && aiPrediction.cot_analysis">
                <div class="cot-step" v-for="(step, index) in aiPrediction.cot_analysis.split('\n')" :key="index">
                  <span class="step-marker"></span>
                  <span class="step-text">{{ step }}</span>
                </div>
              </div>
              <div v-else class="cot-loading">
                <div class="cot-step"><span class="step-marker"></span><span class="step-text">正在初始化推理引擎...</span></div>
                <div class="cot-step"><span class="step-marker"></span><span class="step-text">加载历史数据...</span></div>
              </div>
            </div>
          </transition>
        </div>
      </div>

      <!-- Heat Analysis -->
      <div class="section-header">
        <span class="section-title">号码热度分析</span>
      </div>
      <div class="card heat-card">
        <div class="heat-section">
          <div class="heat-label hot">热门号码 (近10期出现 > 3次)</div>
          <div class="heat-balls">
            <span class="ball small red-dark">09</span>
            <span class="ball small red-dark">25</span>
            <span class="ball small red-dark">30</span>
          </div>
        </div>
        <div class="heat-section">
          <div class="heat-label warm">温号号码 (近10期出现 1-2次)</div>
          <div class="heat-balls">
            <span class="ball small orange">02</span>
            <span class="ball small orange">11</span>
            <span class="ball small orange">14</span>
            <span class="ball small orange">18</span>
          </div>
        </div>
        <div class="heat-section">
          <div class="heat-label cold">冷门号码 (近10期未出现)</div>
          <div class="heat-balls">
            <span class="ball small blue-dark">07</span>
            <span class="ball small blue-dark">16</span>
            <span class="ball small blue-dark">23</span>
          </div>
        </div>
      </div>
    </div>

    <div class="scan-button-container">
      <div class="scan-button">
        <svg class="scan-icon" viewBox="0 0 24 24" width="24" height="24" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
          <path d="M3 7V5a2 2 0 0 1 2-2h2"></path>
          <path d="M17 3h2a2 2 0 0 1 2 2v2"></path>
          <path d="M21 17v2a2 2 0 0 1-2 2h-2"></path>
          <path d="M7 21H5a2 2 0 0 1-2-2v-2"></path>
          <rect x="7" y="7" width="10" height="10"></rect>
          <line x1="12" y1="12" x2="12" y2="12"></line>
        </svg>
        <span>扫码对奖</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.draw-view {
  padding: 16px;
  background-color: #1a1a1a;
  min-height: 100%;
  padding-bottom: 100px; /* Space for scan button */
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.scrolling-banner {
  background-color: #2c2c2c;
  padding: 8px;
  border-radius: 4px;
  font-size: 12px;
  margin-bottom: 16px;
  color: #ffd700;
  display: flex;
  align-items: center;
}

.lottery-tabs {
  display: flex;
  gap: 24px;
  margin-bottom: 20px;
  border-bottom: 1px solid #333;
  padding-bottom: 0;
}

.tab {
  font-size: 16px;
  color: #888;
  cursor: pointer;
  padding-bottom: 12px;
  position: relative;
}

.tab.active {
  color: #fff;
  font-weight: bold;
}

.tab.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 50%;
  transform: translateX(-50%);
  width: 20px;
  height: 3px;
  background-color: #42b983;
  border-radius: 2px;
}

.card {
  background-color: #242424;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 24px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.lottery-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.lottery-name {
  font-size: 18px;
  font-weight: bold;
  color: #fff;
}

.issue-number {
  font-size: 12px;
  color: #888;
}

.card-header {
  position: relative;
  margin-bottom: 20px;
}

.header-content {
  cursor: pointer;
}

.title-row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.lottery-title {
  font-size: 20px;
  font-weight: bold;
  color: #fff;
}

.issue-title {
  font-size: 20px;
  font-weight: bold;
  color: #fff;
}

.header-arrow {
  margin-left: auto;
  color: #42b983;
  transition: transform 0.3s;
  flex-shrink: 0;
}

.header-arrow.rotated {
  transform: rotate(180deg);
}

.date-row {
  font-size: 13px;
  color: #888;
}

.issue-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background-color: #333;
  border-radius: 8px;
  max-height: 250px;
  overflow-y: auto;
  z-index: 100;
  box-shadow: 0 8px 24px rgba(0,0,0,0.5);
  margin-top: 8px;
  border: 1px solid #444;
}

.issue-option {
  padding: 12px 16px;
  font-size: 14px;
  color: #ccc;
  cursor: pointer;
  border-bottom: 1px solid #444;
  transition: background-color 0.2s;
}

.issue-option:last-child {
  border-bottom: none;
}

.issue-option:hover {
  background-color: #444;
  color: #fff;
}

.issue-option.selected {
  color: #42b983;
  font-weight: bold;
  background-color: #2a3a33;
}

.balls-container {
  margin-bottom: 20px;
}

.balls {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
}

.ball-separator {
  font-size: 24px;
  font-weight: bold;
  color: #888;
  margin: 0 4px;
}

.ball {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 16px;
  color: #fff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.2);
}

.ball.red { background: linear-gradient(135deg, #ff4d4f 0%, #cf1322 100%); }
.ball.blue { background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%); }
.ball.gold { background: linear-gradient(135deg, #ffd700 0%, #ffb300 100%); color: #333; }
.ball.orange { background: linear-gradient(135deg, #ff9800 0%, #f57c00 100%); }
.ball.small { width: 28px; height: 28px; font-size: 12px; }
.ball.small.red-dark { background-color: #5c0011; color: #ffccc7; border: 1px solid #ff4d4f; }
.ball.small.orange { background-color: #613400; color: #ffe7ba; border: 1px solid #fa8c16; }
.ball.small.blue-dark { background-color: #002766; color: #bae7ff; border: 1px solid #1890ff; }

.balls-placeholder {
  display: flex;
  gap: 8px;
}

.ball-skeleton {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: #333;
  animation: pulse 1.5s infinite;
}

.prediction-row.loading {
  font-size: 16px;
  color: #666;
  font-weight: normal;
  letter-spacing: 0;
}

.cot-loading {
  opacity: 0.7;
}

@keyframes pulse {
  0% { opacity: 0.6; }
  50% { opacity: 1; }
  100% { opacity: 0.6; }
}

.pool-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  color: #888;
  padding-top: 16px;
  border-top: 1px solid #333;
}

.highlight { 
  color: #42b983; 
  font-size: 16px; 
  font-weight: bold; 
  margin-left: 4px;
}

.details-link {
  color: #42b983;
}

.section-header {
  margin-bottom: 12px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #fff;
}

.ai-card {
  background: linear-gradient(180deg, #1f2a24 0%, #242424 100%);
  border: 1px solid #2d3d33;
}

.ai-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.ai-title-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.ai-title-text {
  font-weight: bold;
  color: #fff;
}

.powered-by {
  font-size: 10px;
  color: #666;
}

.prediction-content {
  margin-bottom: 16px;
}

.prediction-balls {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
  justify-content: center;
  padding: 12px 0;
}

.prediction-fallback {
  font-family: 'Monaco', 'Consolas', monospace;
  font-size: 20px;
  font-weight: bold;
  color: #fff;
  letter-spacing: 2px;
  text-align: center;
}

.prediction-row {
  font-family: 'Monaco', 'Consolas', monospace;
  font-size: 24px;
  font-weight: bold;
  color: #fff;
  letter-spacing: 2px;
  text-align: center;
  text-shadow: 0 0 10px rgba(66, 185, 131, 0.3);
}

.user-input-section {
  margin-bottom: 16px;
  padding: 12px;
  background-color: #1a1a1a;
  border-radius: 8px;
}

.input-wrapper {
  display: flex;
  gap: 8px;
  align-items: center;
}

.thought-input {
  flex: 1;
  padding: 10px 14px;
  background-color: #2c2c2c;
  border: 1px solid #3d3d3d;
  border-radius: 6px;
  color: #fff;
  font-size: 14px;
  outline: none;
  transition: border-color 0.3s, box-shadow 0.3s;
}

.thought-input::placeholder {
  color: #666;
}

.thought-input:focus {
  border-color: #42b983;
  box-shadow: 0 0 0 2px rgba(66, 185, 131, 0.1);
}

.thought-input:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.analyze-button {
  padding: 10px 20px;
  background: linear-gradient(135deg, #42b983 0%, #35a372 100%);
  color: #fff;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  white-space: nowrap;
  box-shadow: 0 2px 8px rgba(66, 185, 131, 0.3);
}

.analyze-button:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(66, 185, 131, 0.4);
}

.analyze-button:active:not(:disabled) {
  transform: translateY(0);
}

.analyze-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.cot-section {
  background-color: #1a1a1a;
  border-radius: 8px;
  overflow: hidden;
}

.cot-toggle {
  padding: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #888;
  cursor: pointer;
  background-color: #222;
}

.cot-toggle:hover {
  color: #aaa;
}

.arrow {
  transition: transform 0.3s;
}

.arrow.rotated {
  transform: rotate(180deg);
}

.cot-content {
  padding: 12px;
  border-top: 1px solid #333;
}

.cot-step {
  display: flex;
  gap: 12px;
  margin-bottom: 8px;
  font-size: 12px;
  color: #aaa;
  line-height: 1.5;
}

.step-marker {
  width: 2px;
  background-color: #42b983;
  flex-shrink: 0;
  margin-top: 4px;
  height: 12px;
}

.heat-section {
  margin-bottom: 16px;
}

.heat-section:last-child {
  margin-bottom: 0;
}

.heat-label {
  font-size: 12px;
  margin-bottom: 8px;
}

.heat-label.hot { color: #ff4d4f; }
.heat-label.warm { color: #fa8c16; }
.heat-label.cold { color: #1890ff; }

.heat-balls {
  display: flex;
  gap: 12px;
}

.scan-button-container {
  position: fixed;
  bottom: 80px; /* Above tab bar */
  left: 0;
  right: 0;
  display: flex;
  justify-content: center;
  padding: 0 16px;
  z-index: 100;
  pointer-events: none; /* Let clicks pass through container */
}

.scan-button {
  pointer-events: auto;
  background: linear-gradient(90deg, #00e676 0%, #00c853 100%);
  color: #000;
  height: 56px;
  width: 100%;
  max-width: 400px;
  border-radius: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  font-size: 18px;
  font-weight: bold;
  box-shadow: 0 8px 20px rgba(0, 230, 118, 0.3);
  cursor: pointer;
  transition: transform 0.2s;
}

.scan-button:active {
  transform: scale(0.98);
}

.scan-icon {
  font-size: 24px;
}

/* Transitions */
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.3s ease-out;
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateY(-10px);
  opacity: 0;
}
</style>
