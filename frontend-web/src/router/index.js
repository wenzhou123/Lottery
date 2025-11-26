import { createRouter, createWebHistory } from 'vue-router'
import DrawView from '../views/DrawView.vue'
import TrendView from '../views/TrendView.vue'
import DiscoveryView from '../views/DiscoveryView.vue'
import MineView from '../views/MineView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'draw',
      component: DrawView
    },
    {
      path: '/trend',
      name: 'trend',
      component: TrendView
    },
    {
      path: '/discovery',
      name: 'discovery',
      component: DiscoveryView
    },
    {
      path: '/mine',
      name: 'mine',
      component: MineView
    }
  ]
})

export default router
