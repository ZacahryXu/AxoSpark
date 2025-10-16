import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/home',
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
  {
    path:'/home',
    name: 'home',
    component: () => import('../views/WebHome/WebHomePage.vue'),
  },
  {
    path:'/login',
    name: 'login',
    component: () => import('../views/Login/Index.vue'),
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router


