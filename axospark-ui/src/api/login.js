import http from '../http/http'

// 示例：获取用户信息
export function fetchCurrentUser() {
  return http.get('/user/me')
}

// 示例：分页查询视频列表
export function fetchVideoPage(params) {
  return http.get('/video/page', { params })
}

// 示例：提交投稿
export function submitCreation(data) {
  return http.post('/creation/submit', data)
}

// 用户登录 - 修改为GET请求并使用params传递参数
export function login(data) {
  return http.post('/login',  data )
}

// 用户注册
export function doRegister(data) {
  return http.post('/user/register', data)
}

// 发送验证码
export function sendVerificationCode(username) {
  return http.post('/user/sendVerificationCode', { username })
}

// 用户登出
export function logout() {
  return http.get('/logout')
}
