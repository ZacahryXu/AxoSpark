<template>
  <div :class="gridClass">
    <VideoCard
        v-for="video in videos"
        :key="video.id"
        :video="video"
        :size="cardSize"
        :show-author="showAuthor"
        :show-comments="showComments"
        @click="handleVideoClick"
    />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import VideoCard from './VideoCard.vue'

const props = defineProps({
  videos: {
    type: Array,
    required: true
  },
  columns: {
    type: Number,
    default: 4
  },
  gap: {
    type: Number,
    default: 6
  },
  cardSize: {
    type: String,
    default: 'md'
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

const emit = defineEmits(['video-click'])

const gridClass = computed(() => {
  return `grid grid-cols-${props.columns} gap-${props.gap}`
})

const handleVideoClick = (video) => {
  emit('video-click', video)
}
</script>