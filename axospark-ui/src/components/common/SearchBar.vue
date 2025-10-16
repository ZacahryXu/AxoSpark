<template>
  <div :class="containerClass">
    <Search :size="18" class="text-gray-400" />
    <input
        v-model="searchQuery"
        type="text"
        :placeholder="placeholder"
        class="bg-transparent outline-none w-full text-sm"
        @input="handleSearch"
    >
  </div>
</template>

<script setup>
import { Search } from 'lucide-vue-next'
import { ref,computed } from 'vue'

const props = defineProps({
  placeholder: {
    type: String,
    default: '搜索视频、UP主...'
  },
  fullWidth: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['search'])

const searchQuery = ref('')

const containerClass = computed(() => {
  const baseClass = 'glass rounded-xl px-4 py-2 flex items-center gap-2'
  return props.fullWidth ? `${baseClass} w-full` : `${baseClass} w-80`
})

const handleSearch = () => {
  emit('search', searchQuery.value)
}
</script>

