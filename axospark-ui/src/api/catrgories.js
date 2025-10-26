import http from '../http/http'


export const getVideoAllCategories = () => {
    return http.get('/categories/video')
}