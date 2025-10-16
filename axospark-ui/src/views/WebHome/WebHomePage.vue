<template>
  <div class="min-w-[1440px] h-screen p-8">
    <WebHeader />

    <CategoryNav
        :categories="categories"
        @select="handleCategorySelect"
    />

    <VideoGrid
        :videos="videos"
        :columns="4"
        :gap="6"
        card-size="md"
        @video-click="handleVideoClick"
    />
  </div>
</template>

<script setup>
import WebHeader from "@/components/layout/WebHeader.vue";
import CategoryNav from "@/components/navigation/CategoryNav.vue";
import VideoGrid from '@/components/video/VideoGrid.vue'
import { useVideoData } from '@/composables/useVideoData.js'

const { videos, categories } = useVideoData()

const handleCategorySelect = (categoryId) => {
  console.log('Selected category:', categoryId)
  // 实现分类筛选逻辑
}

const handleVideoClick = (video) => {
  console.log('Clicked video:', video)
  // 跳转到视频播放页
}
</script>