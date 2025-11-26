<script setup>
// No script needed for pure router-link navigation
</script>

<template>
  <nav class="tab-bar">
    <router-link to="/" class="tab-item" active-class="active">
      <div class="icon">
        <svg viewBox="0 0 24 24" width="24" height="24" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
          <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"></path>
        </svg>
      </div>
      <span>开奖</span>
    </router-link>
    <router-link to="/trend" class="tab-item" active-class="active">
      <div class="icon">
        <svg viewBox="0 0 24 24" width="24" height="24" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
          <path d="M3 3v18h18"></path>
          <path d="M18.7 8l-5.1 5.2-2.8-2.7L7 14.3"></path>
        </svg>
      </div>
      <span>走势</span>
    </router-link>
    <router-link to="/discovery" class="tab-item" active-class="active">
      <div class="icon">
        <svg viewBox="0 0 24 24" width="24" height="24" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
          <circle cx="12" cy="12" r="10"></circle>
          <polygon points="16.24 7.76 14.12 14.12 7.76 16.24 9.88 9.88 16.24 7.76"></polygon>
        </svg>
      </div>
      <span>发现</span>
    </router-link>
    <router-link to="/mine" class="tab-item" active-class="active">
      <div class="icon">
        <svg viewBox="0 0 24 24" width="24" height="24" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
          <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
          <circle cx="12" cy="7" r="4"></circle>
        </svg>
      </div>
      <span>我的</span>
    </router-link>
  </nav>
</template>

<style scoped>
.tab-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 60px;
  background-color: #2c2c2c;
  display: flex;
  justify-content: space-around;
  align-items: center;
  border-top: 1px solid #333;
  z-index: 1000;
}

.tab-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #888;
  cursor: pointer;
  flex: 1;
}

.tab-item.active {
  color: #42b983;
}

.icon {
  font-size: 24px;
  margin-bottom: 2px;
}

.label {
  font-size: 12px;
}
</style>
