import { createRouter, createWebHashHistory } from 'vue-router'
import HomeView from '@/views/manager/HomeView.vue'
import OrderView from '@/views/user/OrderView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/order',
    name: 'order',
    component: OrderView
  }
]

const router = createRouter({
  history: createWebHashHistory(process.env.BASE_URL),
  routes
})

export default router
