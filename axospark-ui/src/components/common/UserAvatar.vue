<template>
  <div v-if="isLoggedIn">
    <DropdownMenu>
      <DropdownMenuTrigger>
        <div
            :class="avatarClass"
            :style="imageUrl ? `background-image: url(${imageUrl})` : ''"
            @mouseenter="showDownMenu=true"
            @mouseleave="showDownMenu=false"
        >
          <User  :size="iconSize" />
        </div>
      </DropdownMenuTrigger>
      <DropdownMenuContent>
        <DropdownMenuLabel class="from-primary to-secondary">
          <Button class="text-white  " @click="logout">退出登录</Button>
        </DropdownMenuLabel>

      </DropdownMenuContent>
    </DropdownMenu>
  </div>
  <div
      :class="avatarClass"
      :style="imageUrl ? `background-image: url(${imageUrl})` : ''"
      @click="goLogin"
      v-else
  >
    <span>登录</span>
  </div>
</template>

<script setup>
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import { User } from 'lucide-vue-next'
import Cookies from 'js-cookie'
import { ref,computed,onMounted } from 'vue'
import {useRouter} from "vue-router";
import {Button} from "@/components/ui/button/index.js";
import { logout as logoutApi } from '@/api/login.js'

const router = useRouter()
const isLoggedIn = ref(false)
const showDownMenu = ref(false)
const props = defineProps({
  imageUrl: String,
  size: {
    type: String,
    default: 'md',
    validator: (value) => ['sm', 'md', 'lg'].includes(value)
  }
})

const avatarClass = computed(() => {
  const baseClass = 'rounded-xl bg-gradient-to-br from-primary to-secondary flex items-center justify-center bg-cover bg-center cursor-pointer'
  const sizes = {
    sm: 'w-7 h-7',
    md: 'w-9 h-9',
    lg: 'w-10 h-10'
  }
  return `${baseClass} ${sizes[props.size]}`
})

const iconSize = computed(() => {
  const sizes = { sm: 16, md: 20, lg: 24 }
  return sizes[props.size]
})
onMounted(()=>{
  let token = Cookies.get('satoken')
  console.log(token)
  if(token){
    isLoggedIn.value = true
  }
})
const goLogin = () => {
  router.push('/login')
}
const logout = async () => {
  try {
    // 发送登出请求
    await logoutApi()

    // 清除cookie中的token
    Cookies.remove('satoken')

    // 同时清除localStorage中的token (因为http.js中使用了localStorage)
    localStorage.removeItem('access_token')
    // 更新登录状态
    isLoggedIn.value = false
    // 可选：重定向到首页
    router.push('/')
  } catch (error) {
    console.error('登出失败:', error)
    // 即使API请求失败，也清除本地token
    Cookies.remove('satoken')
    localStorage.removeItem('access_token')
    isLoggedIn.value = false
  }
}
</script>