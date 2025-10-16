<script setup>
import { ref, computed } from 'vue'

// 模拟数据
const currentTab = ref('推荐')
const tabs = ref(['推荐', '关注', '热门', '排行榜', '直播'])

const videos = ref([
  {
    id: 1,
    title: '这是一个示例视频标题，展示了视频卡片的基本布局',
    cover: 'https://picsum.photos/400/225?random=1',
    author: {
      name: 'UP主名称',
      avatar: 'https://picsum.photos/40/40?random=1'
    },
    duration: '05:30',
    views: '123.4万',
    danmaku: '1.2万',
    publishedTime: '2小时前'
  },
  {
    id: 2,
    title: '另一个精彩的视频内容，展示了不同的视频信息',
    cover: 'https://picsum.photos/400/225?random=2',
    author: {
      name: '另一个UP主',
      avatar: 'https://picsum.photos/40/40?random=2'
    },
    duration: '12:45',
    views: '89.6万',
    danmaku: '8.9千',
    publishedTime: '5小时前'
  },
  {
    id: 3,
    title: '第三个视频标题，继续展示视频列表的布局效果',
    cover: 'https://picsum.photos/400/225?random=3',
    author: {
      name: '第三个UP主',
      avatar: 'https://picsum.photos/40/40?random=3'
    },
    duration: '08:20',
    views: '45.2万',
    danmaku: '3.4千',
    publishedTime: '1天前'
  },
  {
    id: 4,
    title: '第四个视频内容，展示更多的视频卡片样式',
    cover: 'https://picsum.photos/400/225?random=4',
    author: {
      name: '第四个UP主',
      avatar: 'https://picsum.photos/40/40?random=4'
    },
    duration: '15:30',
    views: '67.8万',
    danmaku: '5.6千',
    publishedTime: '2天前'
  },
  {
    id: 5,
    title: '第五个视频标题，继续展示视频网格布局',
    cover: 'https://picsum.photos/400/225?random=5',
    author: {
      name: '第五个UP主',
      avatar: 'https://picsum.photos/40/40?random=5'
    },
    duration: '06:15',
    views: '34.5万',
    danmaku: '2.1千',
    publishedTime: '3天前'
  },
  {
    id: 6,
    title: '第六个视频内容，展示完整的视频列表效果',
    cover: 'https://picsum.photos/400/225?random=6',
    author: {
      name: '第六个UP主',
      avatar: 'https://picsum.photos/40/40?random=6'
    },
    duration: '11:20',
    views: '78.9万',
    danmaku: '6.7千',
    publishedTime: '4天前'
  }
])

const gridLayout = ref('adaptive') // adaptive, twoColumns, oneColumn

const gridClass = computed(() => {
  if (gridLayout.value === 'adaptive') return 'grid-adaptive'
  if (gridLayout.value === 'twoColumns') return 'grid-two-columns'
  return 'grid-one-column'
})

const switchTab = (tab) => {
  currentTab.value = tab
}

const switchLayout = (layout) => {
  gridLayout.value = layout
}
</script>

<template>
  <div class="home-page">
    <!-- 搜索区域 -->
    <div class="search-section">
      <div class="search-container">
        <div class="logo">
          <svg width="180" height="60" viewBox="0 0 2299 1024" fill="var(--bew-theme-color)">
            <path d="M1775.840814 322.588002c6.0164 1.002733 53.144869-9.525967 55.150336-6.016401 3.0082 4.5123 24.065601 155.92504 18.550567 156.927774s-44.621635 10.027334-44.621635 10.027334c-3.0082-20.556034-28.577901-147.903173-29.079268-160.938707m75.205003-14.539634l20.556034 162.944174c10.5287-0.501367 53.144869-3.509567 57.155803-4.010934-6.0164-61.668103-16.545101-158.933241-16.545101-158.93324-20.054668-4.010934-41.112069-4.010934-61.166736 0m-40.610702 226.116376s92.752838-23.564234 126.344406-12.0328c17.046467 61.668103 48.131202 407.611118 51.139402 421.649386-21.057401 2.506833-90.246004 8.523234-95.761037 10.027333-4.5123-26.071068-81.72277-403.098818-81.722771-419.643919m343.436183-207.565809c5.515034 1.5041 54.648969-5.013667 55.150335-1.5041 1.002733 12.032801 6.0164 157.42914 0.501367 157.930507s-44.621635 4.010934-44.621635 4.010934c-1.002733-20.054668-12.032801-146.90044-11.030067-160.437341m75.70637-4.010933l4.010933 160.938707c10.5287 0 52.643502 2.506833 57.155803 2.005467-1.002733-61.668103 0-158.933241 0-158.933241-20.054668-3.509567-40.610702-5.013667-61.166736-4.010933m-64.676303 216.089043s94.758304-12.534167 126.845772 2.506833c7.019134 72.196803 6.0164 408.613852 7.019134 422.652119-21.558768 0-90.246004 1.002733-95.761038 2.005467-1.002733-26.071068-39.607968-410.619319-38.103868-427.164419m-220.099977-413.627519c54.648969 278.759879 96.262404 755.058234 97.766504 785.641602 0 0 43.117535 1.002733 91.750105 4.010934C2105.740095 614.383415 2070.644427 134.575493 2071.145794 119.033126c-12.032801-13.536901-126.344406 6.0164-126.344406 6.0164m-120.328005 659.297196c-10.5287-78.213204-290.291313-166.955108-447.720454-138.377206 0 0-19.553301-172.470141-27.073801-339.425248-6.517767-143.390873-1.002733-282.770813 0.501366-305.833681-10.5287-7.5205-123.837572 46.627102-185.004308 69.188603 0 0 73.199537 309.844614 126.344406 952.59671 0 0 84.730971 9.0246 230.12731-19.051934s317.365114-115.815705 302.825481-219.097244m-341.932083 140.88404l-24.566967-176.982441c6.0164-3.0082 156.927774 53.144869 172.971507 63.172203-2.506833 11.030067-148.40454 113.810238-148.40454 113.810238M610.664628 322.588002c6.0164 1.002733 53.144869-9.525967 55.150335-6.016401 3.0082 4.5123 24.065601 155.92504 18.550568 156.927774s-44.621635 10.027334-44.621635 10.027334c-3.0082-20.556034-28.577901-147.903173-29.079268-160.938707m75.205003-14.539634l20.556034 162.944174c10.5287-0.501367 53.144869-3.509567 57.155803-4.010934-6.517767-61.668103-16.545101-158.933241-16.545101-158.93324-20.054668-4.010934-41.112069-4.010934-61.166736 0m-40.610702 226.116376s92.752838-23.564234 126.344406-12.0328c17.046467 61.668103 48.131202 407.611118 51.139402 421.649386-21.057401 2.506833-90.246004 8.523234-95.761037 10.027333-4.5123-26.071068-81.72277-403.098818-81.722771-419.643919m343.436182-207.565809c5.515034 1.5041 54.648969-5.013667 55.150336-1.5041 1.002733 12.032801 6.0164 157.42914 0.501367 157.930507s-44.621635 4.010934-44.621635 4.010934c-1.002733-20.054668-11.531434-146.90044-11.030068-160.437341m75.706371-4.010933l4.010933 160.938707c10.5287 0 52.643502 2.506833 57.155803 2.005467-1.002733-61.668103 0-158.933241 0-158.933241-20.054668-3.509567-40.610702-4.5123-61.166736-4.010933m-64.676303 216.089043s94.758304-12.534167 126.845772 2.506833c7.019134 72.196803 6.0164 408.613852 7.019134 422.652119-21.558768 0-90.246004 1.002733-95.761038 2.005467-0.501367-26.071068-39.607968-410.619319-38.103868-427.164419m-220.099977-413.627519c54.648969 278.759879 96.262404 755.058234 97.766504 785.641602 0 0 43.117535 1.002733 91.750105 4.010934-28.577901-300.318647-63.67357-780.126569-63.172203-796.170303-12.032801-13.035534-126.344406 6.517767-126.344406 6.517767m-120.328005 659.297196c-10.5287-78.213204-290.291313-166.955108-447.720454-138.377206 0 0-19.553301-172.470141-27.073801-339.425248-6.517767-143.390873-1.002733-282.770813 0.501366-305.833681C174.475608-6.308547 61.166736 47.337689 0 69.89919c0 0 73.199537 309.844614 126.344406 952.59671 0 0 84.730971 9.0246 230.12731-19.051934s317.365114-115.815705 302.825481-219.097244m-341.932083 140.88404l-24.566967-176.982441c6.0164-3.0082 156.927774 53.144869 172.971507 63.172203-2.506833 11.030067-148.40454 113.810238-148.40454 113.810238"/>
          </svg>
        </div>
        <div class="search-bar">
          <input type="text" placeholder="搜索视频、UP主、番剧" class="search-input">
          <button class="search-btn">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
              <path d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"/>
            </svg>
          </button>
        </div>
      </div>
    </div>

    <!-- 标签栏 -->
    <div class="tabs-container">
      <div class="tabs">
        <button
          v-for="tab in tabs"
          :key="tab"
          :class="{ active: currentTab === tab }"
          @click="switchTab(tab)"
          class="tab-btn"
        >
          {{ tab }}
        </button>
      </div>

      <!-- 布局切换器 -->
      <div class="layout-switcher">
        <button
          :class="{ active: gridLayout === 'adaptive' }"
          @click="switchLayout('adaptive')"
          class="layout-btn"
          title="自适应布局"
        >
          <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
            <path d="M3 3h7v7H3V3zm0 11h7v7H3v-7zm11-11h7v7h-7V3zm0 11h7v7h-7v-7z"/>
          </svg>
        </button>
        <button
          :class="{ active: gridLayout === 'twoColumns' }"
          @click="switchLayout('twoColumns')"
          class="layout-btn"
          title="两列布局"
        >
          <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
            <path d="M3 3h7v18H3V3zm11 0h7v18h-7V3z"/>
          </svg>
        </button>
        <button
          :class="{ active: gridLayout === 'oneColumn' }"
          @click="switchLayout('oneColumn')"
          class="layout-btn"
          title="单列布局"
        >
          <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
            <path d="M3 3h18v3H3V3zm0 5h18v3H3V8zm0 5h18v3H3v-3zm0 5h18v3H3v-3z"/>
          </svg>
        </button>
      </div>
    </div>

    <!-- 视频列表 -->
    <div class="video-container" :class="gridClass">
      <div v-for="video in videos" :key="video.id" class="video-card">
        <div class="video-cover">
          <img :src="video.cover" :alt="video.title" class="cover-image">
          <div class="video-duration">{{ video.duration }}</div>
        </div>
        <div class="video-info">
          <h3 class="video-title">{{ video.title }}</h3>
          <div class="video-meta">
            <div class="author-info">
              <img :src="video.author.avatar" :alt="video.author.name" class="author-avatar">
              <span class="author-name">{{ video.author.name }}</span>
            </div>
            <div class="video-stats">
              <span class="views">{{ video.views }}播放</span>
              <span class="danmaku">{{ video.danmaku }}弹幕</span>
            </div>
            <div class="video-time">{{ video.publishedTime }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 全局重置和基础样式 */
* {
  box-sizing: border-box;
}

.home-page {
  min-height: 100vh;
  background: var(--bew-bg, #f5f5f5);
  padding: 20px;
  width: 100%;
  max-width: 100%;
  overflow-x: hidden;
  margin: 0;
  position: relative;
}

/* 确保容器能够正确自适应 */
.home-page > * {
  max-width: 100%;
}

/* CSS 变量定义 */
:root {
  --bew-theme-color: #00aeec;
  --bew-bg: #f5f5f5;
  --bew-content: rgba(255, 255, 255, 0.68);
  --bew-elevated: rgba(255, 255, 255, 0.68);
  --bew-text-1: #18191c;
  --bew-text-2: rgba(24, 25, 28, 0.8);
  --bew-text-3: rgba(24, 25, 28, 0.6);
  --bew-fill-1: rgba(131, 131, 145, 0.15);
  --bew-fill-2: rgba(131, 131, 145, 0.3);
  --bew-border-color: rgba(131, 131, 145, 0.18);
  --bew-radius: 12px;
  --bew-shadow-1: 0 4px 6px -1px rgb(0 0 0 / 0.08), 0 2px 4px -2px rgb(0 0 0 / 0.04);
  --bew-shadow-2: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.06);
  --bew-filter-glass-1: blur(20px) saturate(180%);
}

/* 搜索区域 */
.search-section {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 500px;
  margin-bottom: 40px;
  width: 100%;
  padding: 0 20px;
}

.search-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  width: 100%;
  max-width: 600px;
}

.logo {
  margin-bottom: 20px;
}

.search-bar {
  position: relative;
  width: 100%;
  max-width: 550px;
  height: 46px;
}

.search-input {
  width: 100%;
  height: 100%;
  padding: 0 60px 0 24px;
  border: 1px solid var(--bew-border-color);
  border-radius: 60px;
  background: var(--bew-content);
  backdrop-filter: var(--bew-filter-glass-1);
  box-shadow: var(--bew-shadow-2);
  font-size: 16px;
  color: var(--bew-text-1);
  transition: all 0.3s ease;
}

.search-input:focus {
  outline: none;
  border-color: var(--bew-theme-color);
  box-shadow: 0 0 0 2px var(--bew-theme-color), var(--bew-shadow-2);
}

.search-btn {
  position: absolute;
  right: 6px;
  top: 50%;
  transform: translateY(-50%);
  width: 32px;
  height: 32px;
  border: none;
  border-radius: 50%;
  background: var(--bew-fill-1);
  color: var(--bew-text-1);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.search-btn:hover {
  background: var(--bew-fill-2);
  color: var(--bew-theme-color);
}

/* 标签栏 */
.tabs-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  position: sticky;
  top: 10px;
  z-index: 9;
  width: 100%;
  padding: 0 10px;
  flex-wrap: wrap;
  gap: 10px;
}

.tabs {
  display: flex;
  gap: 4px;
  background: var(--bew-elevated);
  backdrop-filter: var(--bew-filter-glass-1);
  padding: 4px;
  border-radius: 20px;
  box-shadow: var(--bew-shadow-1);
  border: 1px solid var(--bew-border-color);
}

.tab-btn {
  padding: 8px 16px;
  border: none;
  background: transparent;
  color: var(--bew-text-2);
  font-weight: bold;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
}

.tab-btn:hover {
  background: var(--bew-fill-2);
  color: var(--bew-text-1);
}

.tab-btn.active {
  background: var(--bew-theme-color);
  color: white;
}

.layout-switcher {
  display: flex;
  gap: 4px;
  background: var(--bew-elevated);
  backdrop-filter: var(--bew-filter-glass-1);
  padding: 4px;
  border-radius: 20px;
  box-shadow: var(--bew-shadow-1);
  border: 1px solid var(--bew-border-color);
}

.layout-btn {
  width: 32px;
  height: 32px;
  border: none;
  background: transparent;
  color: var(--bew-text-2);
  border-radius: 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.layout-btn:hover {
  background: var(--bew-fill-2);
  color: var(--bew-text-1);
}

.layout-btn.active {
  background: var(--bew-theme-color);
  color: white;
}

/* 视频容器 */
.video-container {
  display: grid;
  gap: 20px;
  width: 100%;
  padding: 0 10px;
}

.grid-adaptive {
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
}

.grid-two-columns {
  grid-template-columns: repeat(2, 1fr);
}

.grid-one-column {
  grid-template-columns: 1fr;
}

/* 视频卡片 */
.video-card {
  background: var(--bew-elevated);
  backdrop-filter: var(--bew-filter-glass-1);
  border-radius: var(--bew-radius);
  box-shadow: var(--bew-shadow-1);
  border: 1px solid var(--bew-border-color);
  overflow: hidden;
  transition: all 0.3s ease;
  cursor: pointer;
}

.video-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--bew-shadow-2);
}

.video-cover {
  position: relative;
  width: 100%;
  aspect-ratio: 16/9;
  overflow: hidden;
}

.cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.video-card:hover .cover-image {
  transform: scale(1.05);
}

.video-duration {
  position: absolute;
  bottom: 8px;
  right: 8px;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.video-info {
  padding: 16px;
}

.video-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--bew-text-1);
  margin: 0 0 12px 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.video-meta {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.author-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
}

.author-name {
  font-size: 14px;
  color: var(--bew-text-2);
  font-weight: 500;
}

.video-stats {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: var(--bew-text-3);
}

.video-time {
  font-size: 12px;
  color: var(--bew-text-3);
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .grid-adaptive {
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  }
}

@media (max-width: 768px) {
  .home-page {
    padding: 12px;
  }

  .search-section {
    min-height: 300px;
    padding: 0 10px;
  }

  .search-bar {
    width: 100%;
    max-width: 400px;
  }

  .tabs-container {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
    padding: 0 5px;
  }

  .tabs {
    justify-content: center;
    flex-wrap: wrap;
  }

  .layout-switcher {
    align-self: center;
  }

  .grid-adaptive {
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  }

  .grid-two-columns {
    grid-template-columns: 1fr;
  }

  .video-container {
    padding: 0 5px;
  }
}

@media (max-width: 480px) {
  .home-page {
    padding: 12px;
  }

  .search-section {
    height: 300px;
  }

  .search-container {
    gap: 16px;
  }

  .logo svg {
    width: 120px;
    height: 40px;
  }
}
</style>