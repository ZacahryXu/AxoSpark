import axios from 'axios'

// 基础配置：从环境变量读取后端网关地址
const baseURL = import.meta.env.VITE_API_BASE_URL || '/api'

const http = axios.create({
  baseURL,
  timeout: 15000,
  withCredentials: false,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 请求拦截：添加鉴权头、公共参数
http.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('access_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截：统一处理业务码与错误
http.interceptors.response.use(
  (response) => {
    // 如果后端采用统一响应结构 { code, data, message }
    const payload = response.data
    if (payload && typeof payload === 'object' && 'code' in payload) {
      const code = payload.code
      if (code === 1 || code === 200) {
        return payload
      }
      const message = payload.message || '请求失败'
      return Promise.reject(new Error(message))
    }
    // 非统一结构，直接返回 data
    return payload
  },
  (error) => {
    if (error.response) {
      const status = error.response.status
      // 可根据状态码扩展处理逻辑
      if (status === 401) {
        // 未登录或登录过期，可在此触发登出或刷新令牌
        // 这里仅清理本地 token
        localStorage.removeItem('access_token')
      }
    }
    return Promise.reject(error)
  }
)

export default http


