<script setup>
import { ref, computed } from 'vue'
import {Button} from "@/components/ui/button/index.js";

// 模拟数据（保持不变）
const currentTab = ref('推荐')
const tabs = ref(['推荐', '关注', '热门', '排行榜', '直播'])

const videos = ref([
  {
    id: 1,
    title: '示例视频标题，展示视频卡片布局',
    cover: 'https://picsum.photos/400/225?random=1',
    author: { name: 'UP主1', avatar: 'https://picsum.photos/40/40?random=1' },
    duration: '05:30',
    views: '123.4万',
    danmaku: '1.2万',
    publishedTime: '2小时前'
  },
  {
    id: 2,
    title: '另一个精彩视频内容',
    cover: 'https://picsum.photos/400/225?random=2',
    author: { name: 'UP主2', avatar: 'https://picsum.photos/40/40?random=2' },
    duration: '12:45',
    views: '89.6万',
    danmaku: '8.9千',
    publishedTime: '5小时前'
  },
  {
    id: 3,
    title: '第三个视频标题，继续展示布局',
    cover: 'https://picsum.photos/400/225?random=3',
    author: { name: 'UP主3', avatar: 'https://picsum.photos/40/40?random=3' },
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

const gridLayout = ref('adaptive') // 自适应、双列、单列

const gridClass = computed(() => {
  if (gridLayout.value === 'adaptive') return 'grid-cols-[repeat(auto-fit,minmax(280px,1fr))]' // 缩小最小宽度
  if (gridLayout.value === 'twoColumns') return 'grid-cols-1 sm:grid-cols-2'
  return 'grid-cols-1'
})

const switchTab = (tab) => { currentTab.value = tab }
const switchLayout = (layout) => { gridLayout.value = layout }
</script>

<template>
  <div class="flex overflow-x-auto">

    <!-- Mockup 1: Desktop Homepage -->
    <div class="min-w-[1440px] h-screen p-8 mockup-border">

      <!-- Category Navigation -->
      <div class="glass rounded-2xl px-6 py-4 mb-6">
        <div class="flex items-center gap-4 overflow-x-auto">
          <Button class="px-4 py-2 rounded-lg bg-gradient-to-r from-primary to-secondary text-white font-medium whitespace-nowrap">
            推荐
          </Button>
          <button class="px-4 py-2 rounded-lg glass-hover transition whitespace-nowrap">
            新番
          </button>
          <button class="px-4 py-2 rounded-lg glass-hover transition whitespace-nowrap">
            热门
          </button>
          <button class="px-4 py-2 rounded-lg glass-hover transition whitespace-nowrap">
            MAD·AMV
          </button>
          <button class="px-4 py-2 rounded-lg glass-hover transition whitespace-nowrap">
            MMD
          </button>
          <button class="px-4 py-2 rounded-lg glass-hover transition whitespace-nowrap">
            手书
          </button>
          <button class="px-4 py-2 rounded-lg glass-hover transition whitespace-nowrap">
            配音
          </button>
        </div>
      </div>

      <!-- Video Grid -->
      <div class="grid grid-cols-4 gap-6">
        <!-- Video Card 1 -->
        <div class="glass rounded-2xl overflow-hidden glass-hover transition cursor-pointer">
          <div class="relative aspect-video">
            <img src="https://images.unsplash.com/photo-1578632767115-351597cf2477?w=400&h=225&fit=crop"
                 alt="Video thumbnail" class="w-full h-full object-cover">
            <div class="absolute bottom-2 right-2 px-2 py-1 rounded-lg bg-black/80 text-xs font-medium">
              12:34
            </div>
            <div class="absolute top-2 left-2 px-2 py-1 rounded-lg bg-primary/90 text-xs font-medium">
              NEW
            </div>
          </div>
          <div class="p-4">
            <h3 class="font-medium mb-2 line-clamp-2">【MAD】超燃动画混剪 | 热血沸腾的瞬间</h3>
            <div class="flex items-center gap-2 mb-3">
              <div class="w-8 h-8 rounded-full bg-gradient-to-br from-primary to-secondary"></div>
              <span class="text-sm text-gray-400">动画剪辑师</span>
            </div>
            <div class="flex items-center gap-4 text-xs text-gray-400">
                            <span class="flex items-center gap-1">
                                <i data-lucide="play" class="w-3 h-3"></i>
                                128.5万
                            </span>
              <span class="flex items-center gap-1">
                                <i data-lucide="message-circle" class="w-3 h-3"></i>
                                2.3万
                            </span>
            </div>
          </div>
        </div>

        <!-- Video Card 2 -->
        <div class="glass rounded-2xl overflow-hidden glass-hover transition cursor-pointer">
          <div class="relative aspect-video">
            <img src="https://images.unsplash.com/photo-1613376023733-0a73315d9b06?w=400&h=225&fit=crop"
                 alt="Video thumbnail" class="w-full h-full object-cover">
            <div class="absolute bottom-2 right-2 px-2 py-1 rounded-lg bg-black/80 text-xs font-medium">
              24:15
            </div>
          </div>
          <div class="p-4">
            <h3 class="font-medium mb-2 line-clamp-2">【新番】某科学的超电磁炮 第01话</h3>
            <div class="flex items-center gap-2 mb-3">
              <div class="w-8 h-8 rounded-full bg-gradient-to-br from-secondary to-primary"></div>
              <span class="text-sm text-gray-400">官方频道</span>
            </div>
            <div class="flex items-center gap-4 text-xs text-gray-400">
                            <span class="flex items-center gap-1">
                                <i data-lucide="play" class="w-3 h-3"></i>
                                456.2万
                            </span>
              <span class="flex items-center gap-1">
                                <i data-lucide="message-circle" class="w-3 h-3"></i>
                                8.9万
                            </span>
            </div>
          </div>
        </div>

        <!-- Video Card 3 -->
        <div class="glass rounded-2xl overflow-hidden glass-hover transition cursor-pointer">
          <div class="relative aspect-video">
            <img src="https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=400&h=225&fit=crop"
                 alt="Video thumbnail" class="w-full h-full object-cover">
            <div class="absolute bottom-2 right-2 px-2 py-1 rounded-lg bg-black/80 text-xs font-medium">
              08:42
            </div>
          </div>
          <div class="p-4">
            <h3 class="font-medium mb-2 line-clamp-2">【MMD】精美3D动画舞蹈演示</h3>
            <div class="flex items-center gap-2 mb-3">
              <div class="w-8 h-8 rounded-full bg-gradient-to-br from-primary to-secondary"></div>
              <span class="text-sm text-gray-400">MMD创作者</span>
            </div>
            <div class="flex items-center gap-4 text-xs text-gray-400">
                            <span class="flex items-center gap-1">
                                <i data-lucide="play" class="w-3 h-3"></i>
                                89.3万
                            </span>
              <span class="flex items-center gap-1">
                                <i data-lucide="message-circle" class="w-3 h-3"></i>
                                1.2万
                            </span>
            </div>
          </div>
        </div>

        <!-- Video Card 4 -->
        <div class="glass rounded-2xl overflow-hidden glass-hover transition cursor-pointer">
          <div class="relative aspect-video">
            <img src="https://images.unsplash.com/photo-1618336753974-aae8e04506aa?w=400&h=225&fit=crop"
                 alt="Video thumbnail" class="w-full h-full object-cover">
            <div class="absolute bottom-2 right-2 px-2 py-1 rounded-lg bg-black/80 text-xs font-medium">
              15:28
            </div>
            <div class="absolute top-2 left-2 px-2 py-1 rounded-lg bg-secondary/90 text-xs font-medium">
              HOT
            </div>
          </div>
          <div class="p-4">
            <h3 class="font-medium mb-2 line-clamp-2">【手书】原创动画短片 温馨治愈系</h3>
            <div class="flex items-center gap-2 mb-3">
              <div class="w-8 h-8 rounded-full bg-gradient-to-br from-secondary to-primary"></div>
              <span class="text-sm text-gray-400">手书画师</span>
            </div>
            <div class="flex items-center gap-4 text-xs text-gray-400">
                            <span class="flex items-center gap-1">
                                <i data-lucide="play" class="w-3 h-3"></i>
                                234.7万
                            </span>
              <span class="flex items-center gap-1">
                                <i data-lucide="message-circle" class="w-3 h-3"></i>
                                5.6万
                            </span>
            </div>
          </div>
        </div>

        <!-- Video Card 5 -->
        <div class="glass rounded-2xl overflow-hidden glass-hover transition cursor-pointer">
          <div class="relative aspect-video">
            <img src="https://images.unsplash.com/photo-1626618012641-bfbca5a31239?w=400&h=225&fit=crop"
                 alt="Video thumbnail" class="w-full h-full object-cover">
            <div class="absolute bottom-2 right-2 px-2 py-1 rounded-lg bg-black/80 text-xs font-medium">
              19:45
            </div>
          </div>
          <div class="p-4">
            <h3 class="font-medium mb-2 line-clamp-2">【配音】经典动画片段重新配音</h3>
            <div class="flex items-center gap-2 mb-3">
              <div class="w-8 h-8 rounded-full bg-gradient-to-br from-primary to-secondary"></div>
              <span class="text-sm text-gray-400">声优工作室</span>
            </div>
            <div class="flex items-center gap-4 text-xs text-gray-400">
                            <span class="flex items-center gap-1">
                                <i data-lucide="play" class="w-3 h-3"></i>
                                167.8万
                            </span>
              <span class="flex items-center gap-1">
                                <i data-lucide="message-circle" class="w-3 h-3"></i>
                                3.4万
                            </span>
            </div>
          </div>
        </div>

        <!-- Video Card 6 -->
        <div class="glass rounded-2xl overflow-hidden glass-hover transition cursor-pointer">
          <div class="relative aspect-video">
            <img src="https://images.unsplash.com/photo-1612198188060-c7c2a3b66eae?w=400&h=225&fit=crop"
                 alt="Video thumbnail" class="w-full h-full object-cover">
            <div class="absolute bottom-2 right-2 px-2 py-1 rounded-lg bg-black/80 text-xs font-medium">
              23:12
            </div>
          </div>
          <div class="p-4">
            <h3 class="font-medium mb-2 line-clamp-2">【番剧】进击的巨人 最终季 第12话</h3>
            <div class="flex items-center gap-2 mb-3">
              <div class="w-8 h-8 rounded-full bg-gradient-to-br from-secondary to-primary"></div>
              <span class="text-sm text-gray-400">官方频道</span>
            </div>
            <div class="flex items-center gap-4 text-xs text-gray-400">
                            <span class="flex items-center gap-1">
                                <i data-lucide="play" class="w-3 h-3"></i>
                                892.1万
                            </span>
              <span class="flex items-center gap-1">
                                <i data-lucide="message-circle" class="w-3 h-3"></i>
                                15.7万
                            </span>
            </div>
          </div>
        </div>

        <!-- Video Card 7 -->
        <div class="glass rounded-2xl overflow-hidden glass-hover transition cursor-pointer">
          <div class="relative aspect-video">
            <img src="https://images.unsplash.com/photo-1635805737707-575885ab0820?w=400&h=225&fit=crop"
                 alt="Video thumbnail" class="w-full h-full object-cover">
            <div class="absolute bottom-2 right-2 px-2 py-1 rounded-lg bg-black/80 text-xs font-medium">
              10:23
            </div>
          </div>
          <div class="p-4">
            <h3 class="font-medium mb-2 line-clamp-2">【AMV】动漫音乐视频混剪合集</h3>
            <div class="flex items-center gap-2 mb-3">
              <div class="w-8 h-8 rounded-full bg-gradient-to-br from-primary to-secondary"></div>
              <span class="text-sm text-gray-400">AMV制作组</span>
            </div>
            <div class="flex items-center gap-4 text-xs text-gray-400">
                            <span class="flex items-center gap-1">
                                <i data-lucide="play" class="w-3 h-3"></i>
                                345.6万
                            </span>
              <span class="flex items-center gap-1">
                                <i data-lucide="message-circle" class="w-3 h-3"></i>
                                6.8万
                            </span>
            </div>
          </div>
        </div>

        <!-- Video Card 8 -->
        <div class="glass rounded-2xl overflow-hidden glass-hover transition cursor-pointer">
          <div class="relative aspect-video">
            <img src="https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=400&h=225&fit=crop"
                 alt="Video thumbnail" class="w-full h-full object-cover">
            <div class="absolute bottom-2 right-2 px-2 py-1 rounded-lg bg-black/80 text-xs font-medium">
              16:54
            </div>
            <div class="absolute top-2 left-2 px-2 py-1 rounded-lg bg-primary/90 text-xs font-medium">
              NEW
            </div>
          </div>
          <div class="p-4">
            <h3 class="font-medium mb-2 line-clamp-2">【原创】自制动画短片 科幻题材</h3>
            <div class="flex items-center gap-2 mb-3">
              <div class="w-8 h-8 rounded-full bg-gradient-to-br from-secondary to-primary"></div>
              <span class="text-sm text-gray-400">独立动画师</span>
            </div>
            <div class="flex items-center gap-4 text-xs text-gray-400">
                            <span class="flex items-center gap-1">
                                <i data-lucide="play" class="w-3 h-3"></i>
                                78.9万
                            </span>
              <span class="flex items-center gap-1">
                                <i data-lucide="message-circle" class="w-3 h-3"></i>
                                2.1万
                            </span>
            </div>
          </div>
        </div>
      </div>
    </div>


  </div>

</template>

<style scoped>

</style>