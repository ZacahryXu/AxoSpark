<template>
  <div class="glass rounded-2xl overflow-hidden glass-hover transition cursor-pointer" @click="handleClick">
    <!-- 视频缩略图 -->
    <div class="relative aspect-video">
      <img
          :src="video.thumbnail"
          :alt="video.title"
          class="w-full h-full object-cover"
      >

      <!-- 时长标签 -->
      <div class="absolute bottom-2 right-2 px-2 py-1 rounded-lg bg-black/80 text-xs font-medium">
        {{ video.duration }}
      </div>

      <!-- 状态标签 -->
      <div
          v-if="video.badge"
          :class="getBadgeClass(video.badge.type)"
          class="absolute top-2 left-2 px-2 py-1 rounded-lg text-xs font-medium"
      >
        {{ video.badge.text }}
      </div>
    </div>

    <!-- 视频信息 -->
    <div :class="contentPadding">
      <h3 :class="titleClass">{{ video.title }}</h3>

      <!-- 作者信息 -->
      <div v-if="showAuthor" class="flex items-center gap-2 mb-3">
        <UserAvatar :size="avatarSize" :image-url="video.author.avatar" />
        <span :class="authorClass">{{ video.author.name }}</span>
      </div>

      <!-- 统计信息 -->
      <div :class="statsClass">
        <span class="flex items-center gap-1">
          <Play :size="12" />
          {{ formatNumber(video.views) }}
        </span>
        <span v-if="showComments" class="flex items-center gap-1">
          <MessageCircle :size="12" />
          {{ formatNumber(video.comments) }}
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Play, MessageCircle } from 'lucide-vue-next'
import { computed } from 'vue'
import UserAvatar from '../common/UserAvatar.vue'

const props = defineProps({
  video: {
    type: Object,
    required: true
  },
  size: {
    type: String,
    default: 'md',
    validator: (value) => ['sm', 'md', 'lg'].includes(value)
  },
  showAuthor: {
    type: Boolean,
    default: true
  },
  showComments: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['click'])

const contentPadding = computed(() => {
  const sizes = { sm: 'p-3', md: 'p-4', lg: 'p-5' }
  return sizes[props.size]
})

const titleClass = computed(() => {
  const sizes = { sm: 'text-sm', md: 'text-base', lg: 'text-lg' }
  return `font-medium mb-2 line-clamp-2 ${sizes[props.size]}`
})

const avatarSize = computed(() => {
  const sizes = { sm: 'sm', md: 'sm', lg: 'md' }
  return sizes[props.size]
})

const authorClass = computed(() => {
  const sizes = { sm: 'text-xs', md: 'text-sm', lg: 'text-base' }
  return `text-gray-400 ${sizes[props.size]}`
})

const statsClass = computed(() => {
  const sizes = { sm: 'text-xs', md: 'text-xs', lg: 'text-sm' }
  return `flex items-center gap-4 text-gray-400 ${sizes[props.size]}`
})

const getBadgeClass = (type) => {
  const classes = {
    new: 'bg-primary/90',
    hot: 'bg-secondary/90',
    live: 'bg-red-500/90'
  }
  return classes[type] || 'bg-primary/90'
}

const formatNumber = (num) => {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + '万'
  }
  return num.toString()
}

const handleClick = () => {
  emit('click', props.video)
}
</script>