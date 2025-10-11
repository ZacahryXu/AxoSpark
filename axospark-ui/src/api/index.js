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


