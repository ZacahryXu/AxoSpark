import http from '../http/http'

export function upload(file) {
    return http.post('/video/uploadVideo',file,{
        headers: { 'Content-Type': 'multipart/form-data' }
    })
}
export const postVideo = (data) => {
    return http.post('/video/postVideo', data)
}