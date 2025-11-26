<script setup>
import { RouterView } from 'vue-router'
import TabBar from './components/TabBar.vue'
</script>

<template>
  <div class="app-container">
    <main class="content">
      <RouterView />
    </main>
    <TabBar />
  </div>
</template>

<style scoped>
.app-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #1a1a1a;
  color: #ffffff;
}

.content {
  flex: 1;
  overflow-y: auto;
  padding-bottom: 60px; /* Space for TabBar */
}
</style>
