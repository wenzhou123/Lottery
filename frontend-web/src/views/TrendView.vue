<script setup>
import { ref, onMounted, computed } from 'vue'
import { getDrawHistory } from '../api'

const activeTab = ref('ssq')
const historyList = ref([])
const displayCount = ref(20) // Show last 20 draws

// Always use mock data for now to demonstrate the UI
const loadData = async () => {
  generateMockData()
  console.log('Mock data generated:', historyList.value.length, 'draws')
  console.log('First draw:', historyList.value[0])
}

const generateMockData = () => {
  const mock = []
  for (let i = 0; i < displayCount.value; i++) {
    const redBalls = []
    const redMax = activeTab.value === 'ssq' ? 33 : activeTab.value === 'dlt' ? 35 : 10
    const redCount = activeTab.value === 'ssq' ? 6 : activeTab.value === 'dlt' ? 5 : 3
    
    while (redBalls.length < redCount) {
      const num = Math.floor(Math.random() * redMax) + 1
      if (!redBalls.includes(num)) redBalls.push(num)
    }
    redBalls.sort((a, b) => a - b)
    
    let blueBalls = null
    if (activeTab.value === 'ssq') {
      blueBalls = (Math.floor(Math.random() * 16) + 1).toString().padStart(2, '0')
    } else if (activeTab.value === 'dlt') {
      const blues = []
      while (blues.length < 2) {
        const num = Math.floor(Math.random() * 12) + 1
        if (!blues.includes(num)) blues.push(num)
      }
      blues.sort((a, b) => a - b)
      blueBalls = blues.map(n => n.toString().padStart(2, '0')).join(',')
    }

    mock.push({
      id: i,
      issueNumber: (2023135 - i).toString(),
      drawDate: '11-21',
      redBalls: redBalls.map(n => n.toString().padStart(2, '0')).join(','),
      blueBalls: blueBalls
    })
  }
  historyList.value = mock
}

const getMissCount = (currentIndex, number, type) => {
  // Calculate how many draws this number has been missing
  let count = 0
  for (let i = currentIndex; i < historyList.value.length; i++) {
    const draw = historyList.value[i]
    const balls = type === 'red' ? draw.redBalls : draw.blueBalls
    if (!balls) return ''
    const ballArray = balls.split(',').map(b => parseInt(b))
    if (ballArray.includes(number)) {
      break
    }
    count++
  }
  return count > 0 ? count : ''
}

const redBallRange = computed(() => {
  if (activeTab.value === 'ssq') return 33
  if (activeTab.value === 'dlt') return 35
  if (activeTab.value === '3d') return 10
  return 80
})

const blueBallRange = computed(() => {
  if (activeTab.value === 'ssq') return 16
  if (activeTab.value === 'dlt') return 12
  return 0
})

const switchTab = (tab) => {
  activeTab.value = tab
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<template>
  <div class="trend-view">
    <div class="lottery-tabs">
      <div class="tab" :class="{ active: activeTab === 'ssq' }" @click="switchTab('ssq')">双色球</div>
      <div class="tab" :class="{ active: activeTab === 'dlt' }" @click="switchTab('dlt')">大乐透</div>
      <div class="tab" :class="{ active: activeTab === '3d' }" @click="switchTab('3d')">3D</div>
      <div class="tab" :class="{ active: activeTab === 'kl8' }" @click="switchTab('kl8')">快乐8</div>
    </div>

    <!-- Trend Chart Table -->
    <div class="trend-chart">
      <div class="chart-header">
        <div class="header-cell period">期号</div>
        <div class="header-cell date">日期</div>
        <div class="header-numbers">
          <div class="header-cell number" v-for="n in redBallRange" :key="'h-r-'+n">
            {{ n.toString().padStart(2, '0') }}
          </div>
        </div>
        <div class="header-numbers blue" v-if="blueBallRange > 0">
          <div class="header-cell number blue" v-for="n in blueBallRange" :key="'h-b-'+n">
            {{ n.toString().padStart(2, '0') }}
          </div>
        </div>
      </div>

      <div class="chart-body">
        <div class="chart-row" v-for="(draw, index) in historyList" :key="draw.id">
          <div class="cell period">{{ draw.issueNumber }}</div>
          <div class="cell date">{{ draw.drawDate || '11-21' }}</div>
          
          <!-- Red balls trend -->
          <div class="trend-numbers">
            <div 
              class="cell number" 
              v-for="n in redBallRange" 
              :key="'r-'+n+'-'+index"
            >
              <span 
                v-if="draw.redBalls && draw.redBalls.split(',').map(b => parseInt(b)).includes(n)"
                class="ball red"
              >
                {{ n.toString().padStart(2, '0') }}
              </span>
              <span v-else class="miss-count">{{ getMissCount(index, n, 'red') }}</span>
            </div>
          </div>

          <!-- Blue balls trend -->
          <div class="trend-numbers blue" v-if="blueBallRange > 0">
            <div 
              class="cell number" 
              v-for="n in blueBallRange" 
              :key="'b-'+n+'-'+index"
            >
              <span 
                v-if="draw.blueBalls && draw.blueBalls.split(',').map(b => parseInt(b)).includes(n)"
                class="ball blue"
              >
                {{ n.toString().padStart(2, '0') }}
              </span>
              <span v-else class="miss-count">{{ getMissCount(index, n, 'blue') }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<style scoped>
.trend-view {
  padding: 16px;
  background-color: #1a1a1a;
  min-height: 100vh;
  padding-bottom: 80px;
}

.lottery-tabs {
  display: flex;
  gap: 24px;
  margin-bottom: 20px;
  border-bottom: 1px solid #333;
  background-color: #1a1a1a;
  padding-bottom: 12px;
}

.tab {
  font-size: 16px;
  color: #888;
  cursor: pointer;
  position: relative;
}

.tab.active {
  color: #fff;
  font-weight: bold;
}

.tab.active::after {
  content: '';
  position: absolute;
  bottom: -13px;
  left: 50%;
  transform: translateX(-50%);
  width: 20px;
  height: 3px;
  background-color: #42b983;
  border-radius: 2px;
}

.trend-chart {
  background-color: #242424;
  border-radius: 8px;
  overflow-x: auto;
  overflow-y: visible;
}

.chart-header {
  display: flex;
  background-color: #333;
  border-bottom: 2px solid #42b983;
}

.header-cell {
  padding: 8px 4px;
  font-size: 11px;
  color: #888;
  text-align: center;
  font-weight: bold;
  border-right: 1px solid #444;
}

.header-cell.period {
  min-width: 80px;
  background-color: #333;
}

.header-cell.date {
  min-width: 60px;
  background-color: #333;
}

.header-numbers {
  display: flex;
  flex: 1;
}

.header-numbers.blue {
  border-left: 2px solid #42b983;
}

.header-cell.number {
  min-width: 28px;
  width: 28px;
  flex-shrink: 0;
}

.header-cell.number.blue {
  color: #1890ff;
}

.chart-body {
  position: relative;
}

.chart-row {
  display: flex;
  border-bottom: 1px solid #333;
  position: relative;
}

.chart-row:hover {
  background-color: #2a2a2a;
}

.cell {
  padding: 6px 4px;
  font-size: 11px;
  color: #ccc;
  text-align: center;
  border-right: 1px solid #444;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 32px;
}

.cell.period {
  min-width: 80px;
  background-color: #242424;
  font-weight: bold;
  color: #fff;
}

.chart-row:hover .cell.period {
  background-color: #2a2a2a;
}

.cell.date {
  min-width: 60px;
  background-color: #242424;
  color: #888;
}

.chart-row:hover .cell.date {
  background-color: #2a2a2a;
}

.trend-numbers {
  display: flex;
  flex: 1;
  position: relative;
}

.trend-numbers.blue {
  border-left: 2px solid #42b983;
}

.cell.number {
  min-width: 28px;
  width: 28px;
  flex-shrink: 0;
  position: relative;
}

.ball {
  display: inline-block;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  line-height: 24px;
  font-size: 10px;
  font-weight: bold;
  color: #fff;
  position: relative;
  z-index: 2;
}

.ball.red {
  background: linear-gradient(135deg, #ff4d4f 0%, #cf1322 100%);
  box-shadow: 0 2px 4px rgba(255, 77, 79, 0.3);
}

.ball.blue {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  box-shadow: 0 2px 4px rgba(24, 144, 255, 0.3);
}

.miss-count {
  font-size: 10px;
  color: #666;
}

/* Scrollbar styling */
.trend-chart::-webkit-scrollbar {
  height: 8px;
}

.trend-chart::-webkit-scrollbar-track {
  background: #1a1a1a;
}

.trend-chart::-webkit-scrollbar-thumb {
  background: #42b983;
  border-radius: 4px;
}

.trend-chart::-webkit-scrollbar-thumb:hover {
  background: #52c997;
}
</style>
