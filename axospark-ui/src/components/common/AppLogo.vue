<template>
  <div class="flex items-center gap-2">
    <div :class="logoClass">
      <Play :size="iconSize" />
    </div>
    <span :class="textClass">AxoSpark</span>
  </div>
</template>

<script setup>
import { Play } from 'lucide-vue-next'
import { computed } from 'vue'

const props = defineProps({
  size: {
    type: String,
    default: 'md', // sm, md, lg
    validator: (value) => ['sm', 'md', 'lg'].includes(value)
  }
})

const logoClass = computed(() => {
  const baseClass = 'rounded-xl bg-gradient-to-br from-primary to-secondary flex items-center justify-center'
  const sizes = {
    sm: 'w-8 h-8',
    md: 'w-9 h-9',
    lg: 'w-10 h-10'
  }
  return `${baseClass} ${sizes[props.size]}`
})

const iconSize = computed(() => {
  const sizes = { sm: 16, md: 20, lg: 24 }
  return sizes[props.size]
})

const textClass = computed(() => {
  const sizes = {
    sm: 'text-base',
    md: 'text-lg',
    lg: 'text-xl'
  }
  return `${sizes[props.size]} font-bold`
})
</script>