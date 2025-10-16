import { ref } from 'vue'

export function useVideoData() {
    const videos = ref([
        {
            id: 1,
            title: '【MAD】超燃动画混剪 | 热血沸腾的瞬间',
            thumbnail: 'https://images.unsplash.com/photo-1578632767115-351597cf2477?w=400&h=225&fit=crop',
            duration: '12:34',
            badge: { type: 'new', text: 'NEW' },
            author: {
                name: '动画剪辑师',
                avatar: ''
            },
            views: 1285000,
            comments: 23000
        },
        {
            id: 2,
            title: '【新番】某科学的超电磁炮 第01话',
            thumbnail: 'https://images.unsplash.com/photo-1613376023733-0a73315d9b06?w=400&h=225&fit=crop',
            duration: '24:15',
            author: {
                name: '官方频道',
                avatar: ''
            },
            views: 4562000,
            comments: 89000
        },
        {
            id: 3,
            title: '【MMD】精美3D动画舞蹈演示',
            thumbnail: 'https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=400&h=225&fit=crop',
            duration: '08:42',
            author: {
                name: 'MMD创作者',
                avatar: ''
            },
            views: 893000,
            comments: 12000
        },
        {
            id: 4,
            title: '【手书】原创动画短片 温馨治愈系',
            thumbnail: 'https://images.unsplash.com/photo-1618336753974-aae8e04506aa?w=400&h=225&fit=crop',
            duration: '15:28',
            badge: { type: 'hot', text: 'HOT' },
            author: {
                name: '手书画师',
                avatar: ''
            },
            views: 2347000,
            comments: 56000
        },
        {
            id: 5,
            title: '【配音】经典动画片段重新配音',
            thumbnail: 'https://images.unsplash.com/photo-1626618012641-bfbca5a31239?w=400&h=225&fit=crop',
            duration: '19:45',
            author: {
                name: '声优工作室',
                avatar: ''
            },
            views: 1678000,
            comments: 34000
        },
        {
            id: 6,
            title: '【番剧】进击的巨人 最终季 第12话',
            thumbnail: 'https://images.unsplash.com/photo-1612198188060-c7c2a3b66eae?w=400&h=225&fit=crop',
            duration: '23:12',
            author: {
                name: '官方频道',
                avatar: ''
            },
            views: 8921000,
            comments: 157000
        },
        {
            id: 7,
            title: '【AMV】动漫音乐视频混剪合集',
            thumbnail: 'https://images.unsplash.com/photo-1635805737707-575885ab0820?w=400&h=225&fit=crop',
            duration: '10:23',
            author: {
                name: 'AMV制作组',
                avatar: ''
            },
            views: 3456000,
            comments: 68000
        },
        {
            id: 8,
            title: '【原创】自制动画短片 科幻题材',
            thumbnail: 'https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=400&h=225&fit=crop',
            duration: '16:54',
            badge: { type: 'new', text: 'NEW' },
            author: {
                name: '独立动画师',
                avatar: ''
            },
            views: 789000,
            comments: 21000
        }
    ])

    const categories = ref([
        { id: 'recommend', name: '推荐' },
        { id: 'new', name: '新番' },
        { id: 'hot', name: '热门' },
        { id: 'mad', name: 'MAD·AMV' },
        { id: 'mmd', name: 'MMD' },
        { id: 'handdrawn', name: '手书' },
        { id: 'dub', name: '配音' }
    ])

    return {
        videos,
        categories
    }
}