<script setup>
import { ref } from 'vue'

const props = defineProps({
  categories: {
    type: Array,
    default: () => [
      { id: 'recommend', name: '推荐' },
      { id: 'new', name: '新番' },
      { id: 'hot', name: '热门' },
      { id: 'mad', name: 'MAD·AMV' },
      { id: 'mmd', name: 'MMD' },
      { id: 'handdrawn', name: '手书' },
      { id: 'dub', name: '配音' }
    ]
  },
  size: {
    type: String,
    default: 'md'
  }
})

const emit = defineEmits(['select'])

const selectedCategory = ref('recommend')

const getCategoryClass = (categoryId) => {
  const baseClass = 'px-4 py-2 rounded-lg font-medium whitespace-nowrap transition'
  if (categoryId === selectedCategory.value) {
    return `${baseClass} bg-gradient-to-r from-primary to-secondary text-white`
  }
  return `${baseClass} glass-hover`
}

const selectCategory = (categoryId) => {
  selectedCategory.value = categoryId
  emit('select', categoryId)
}
</script>

<template>
  <div class="glass rounded-2xl px-6 py-4">
    <div class="flex items-center gap-4 overflow-x-auto">
      <button
          v-for="category in categories"
          :key="category.id"
          :class="getCategoryClass(category.id)"
          @click="selectCategory(category.id)"
      >
        {{ category.name }}
      </button>
    </div>
  </div>
</template>

<style scoped>

</style>