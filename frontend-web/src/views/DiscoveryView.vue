<script setup>
import { ref, onMounted } from 'vue'
import { getNews } from '../api'

const newsList = ref([])
const activeCategory = ref(null)

const loadNews = async (category) => {
  try {
    const res = await getNews(category)
    newsList.value = res.data
  } catch (e) {
    console.error(e)
  }
}

const switchCategory = (category) => {
  activeCategory.value = category
  loadNews(category === 'latest' ? null : category)
}

onMounted(() => {
  loadNews()
})
</script>

<template>
  <div class="discovery-view">
    <header class="header">
      <div class="icon">📰</div>
      <h1>资讯中心</h1>
      <div class="search-icon">🔍</div>
    </header>

    <div class="category-tabs">
      <span :class="{ active: activeCategory === null }" @click="switchCategory('latest')">最新</span>
      <span :class="{ active: activeCategory === 'story' }" @click="switchCategory('story')">中奖故事</span>
      <span :class="{ active: activeCategory === 'rule' }" @click="switchCategory('rule')">玩法技巧</span>
      <span :class="{ active: activeCategory === 'notice' }" @click="switchCategory('notice')">规则变动</span>
    </div>

    <div class="news-list">
      <div class="news-item" v-for="item in newsList" :key="item.id">
        <div class="news-content">
          <div class="news-title">{{ item.title }}</div>
          <div class="news-desc">{{ item.content }}</div>
          <div class="news-meta">发布于 {{ new Date(item.publishDate).toLocaleDateString() }}</div>
        </div>
        <div class="news-image"></div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.discovery-view {
  padding: 16px;
  background-color: #1a1a1a;
  min-height: 100%;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.category-tabs {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;
  font-size: 14px;
  color: #888;
}

.category-tabs .active {
  color: #fff;
  font-weight: bold;
  border-bottom: 2px solid #ff4d4f;
  padding-bottom: 4px;
}

.news-item {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
  border-bottom: 1px solid #333;
  padding-bottom: 16px;
}

.news-content {
  flex: 1;
}

.news-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
  line-height: 1.4;
}

.news-desc {
  font-size: 12px;
  color: #aaa;
  margin-bottom: 8px;
  line-height: 1.4;
}

.news-meta {
  font-size: 10px;
  color: #666;
}

.news-image {
  width: 100px;
  height: 70px;
  background-color: #444;
  border-radius: 8px;
}
</style>
