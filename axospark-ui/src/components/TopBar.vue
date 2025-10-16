<script setup>
import {ref, computed, onMounted} from 'vue'
import { useRouter } from 'vue-router'
import Cookies from 'js-cookie'
// or


const router = useRouter()

// 使用假数据替代原始TS数据
const props = defineProps({
  reachTop: {
    type: Boolean,
    default: true
  },
  isDark: {
    type: Boolean,
    default: false
  }
})

// 用户登录状态
const isLoggedIn = ref(false)

// 下拉菜单显示状态
const showUserMenu = ref(false)
const isMouseOverMenu = ref(false)

// 假数据 - 设置
const settings = {
  disableFrostedGlass: false,
  showTopBarThemeColorGradient: true
}

// 假数据 - 强制使用白色图标
const forceWhiteIcon = ref(false)

// 假数据 - 尺寸计算
const leftWidth = ref(250)
const rightWidth = ref(300)
const centerWidth = ref(600)
const searchContentWidth = ref(400)

// 简化的计算属性
const maxOffset = computed(() => {
  return Math.max(0, (centerWidth.value - searchContentWidth.value) / 2)
})

const searchOffset = computed(() => {
  const desired = (rightWidth.value - leftWidth.value) / 2
  const limit = maxOffset.value
  if (!limit)
    return 0
  return Math.min(Math.max(desired, -limit), limit)
})

// 方法
function handleNotificationsItemClick() {
  alert('通知点击')
}

// 导航到登录页面
function goToLogin() {
  router.push('/login')
}

// 上传功能
function goUpload() {
  if (!isLoggedIn.value) {
    router.push('/login')
    return
  }
  // 上传逻辑
  alert('上传功能')
}

// 切换用户菜单显示
function toggleUserMenu() {
  showUserMenu.value = !showUserMenu.value
}
onMounted(()=>{
  let token = Cookies.get('satoken')
  if(token){
    isLoggedIn.value = true
  }
})
</script>
<template>
  <header class="glass rounded-2xl px-6 py-4 mb-6">
    <div class="flex items-center justify-between">
      <div class="flex items-center gap-8">
        <div class="flex items-center gap-2">
          <div class="w-10 h-10 rounded-xl bg-gradient-to-br from-primary to-secondary flex items-center justify-center">
            <i data-lucide="play" class="w-6 h-6"></i>
          </div>
          <span class="text-xl font-bold">AxoSpark</span>
        </div>
        <nav class="flex gap-6">
          <a href="#" class="text-primary font-medium">首页</a>
          <a href="#" class="text-gray-400 hover:text-white transition">动画</a>
          <a href="#" class="text-gray-400 hover:text-white transition">番剧</a>
          <a href="#" class="text-gray-400 hover:text-white transition">直播</a>
        </nav>
      </div>
      <div class="flex items-center gap-4">
        <div class="glass rounded-xl px-4 py-2 flex items-center gap-2 w-80">
          <i data-lucide="search" class="w-5 h-5 text-gray-400"></i>
          <input type="text" placeholder="搜索视频、UP主..." class="bg-transparent outline-none w-full text-sm">
        </div>
        <button class="glass rounded-xl p-2 hover:bg-dark-700 transition">
          <i data-lucide="bell" class="w-5 h-5"></i>
        </button>
        <button class="glass rounded-xl px-4 py-2 flex items-center gap-2 hover:bg-dark-700 transition">
          <i data-lucide="upload" class="w-5 h-5"></i>
          <span class="text-sm">投稿</span>
        </button>
        <div class="w-10 h-10 rounded-xl bg-gradient-to-br from-primary to-secondary"></div>
      </div>
    </div>
  </header>

</template>

<style scoped>

</style>