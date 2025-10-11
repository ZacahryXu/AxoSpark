import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/danmaku-demo',
  },
  {
    path: '/ws-test',
    name: 'ws-test',
    component: () => import('../views/WebSocketTest.vue'),
  },
  {
    path: '/danmaku-demo',
    name: 'danmaku-demo',
    component: () => import('../views/DanmakuDemo.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router


