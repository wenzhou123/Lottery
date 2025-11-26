<template>
  <div class="mine-view">
    <div class="profile-header">
      <div class="avatar">👤</div>
      <div class="user-info">
        <div class="username">User123</div>
        <div class="uid">ID: 888888</div>
      </div>
    </div>

    <div class="menu-list">
      <div class="menu-item">
        <span>📝 购彩记录</span>
        <span>></span>
      </div>
      <div class="menu-item">
        <span>💰 我的钱包</span>
        <span>></span>
      </div>
      <div class="menu-item">
        <span>📞 联系客服</span>
        <span>></span>
      </div>
      <div class="menu-item">
        <span>ℹ️ 关于APP</span>
        <span>></span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.mine-view {
  padding: 16px;
  background-color: #1a1a1a;
  min-height: 100%;
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 24px 0;
  margin-bottom: 24px;
  border-bottom: 1px solid #333;
}

.avatar {
  width: 60px;
  height: 60px;
  background-color: #444;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
}

.username {
  font-size: 20px;
  font-weight: bold;
}

.uid {
  font-size: 12px;
  color: #888;
}

.menu-item {
  display: flex;
  justify-content: space-between;
  padding: 16px;
  background-color: #2c2c2c;
  margin-bottom: 12px;
  border-radius: 8px;
  cursor: pointer;
}
</style>
