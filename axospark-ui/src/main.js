import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import http from './http/http'
import router from './router'

const app = createApp(App)

// 全局属性和 provide 注入
app.config.globalProperties.$http = http
app.provide('http', http)

app.use(router)
app.mount('#app')
