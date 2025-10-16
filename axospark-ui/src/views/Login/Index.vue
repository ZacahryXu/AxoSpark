<script setup>
import { Button } from "@/components/ui/button"
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Play,User,Lock } from 'lucide-vue-next'
import {ref, computed, onMounted, onBeforeMount} from "vue"
import { useRouter } from 'vue-router'
import { login, doRegister, sendVerificationCode } from '@/api'

// 页面状态
const activeTab = ref('login') // 'login' or 'register'
const router = useRouter()
const isLoading = ref(false)

// 表单数据
const loginForm = ref({
  username: '',
  password: ''
})

const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  verificationCode: ''
})

// 表单验证状态
const formErrors = ref({
  login: {
    username: '',
    password: ''
  },
  register: {
    username: '',
    password: '',
    confirmPassword: '',
    verificationCode: ''
  }
})

// 计算属性 - 表单验证
const registerFormValid = computed(() => {
  return registerForm.value.username &&
         registerForm.value.password &&
         registerForm.value.confirmPassword &&
         registerForm.value.password === registerForm.value.confirmPassword &&
         registerForm.value.verificationCode
})

const loginFormValid = computed(() => {
  return loginForm.value.username && loginForm.value.password
})

// 方法
const switchTab = (tab) => {
  activeTab.value = tab
}

const handleLogin = async () => {
  // 重置错误信息
  formErrors.value.login.username = ''
  formErrors.value.login.password = ''

  // 基本验证
  if (!loginForm.value.username) {
    formErrors.value.login.username = '请输入账号'
    return
  }

  if (!loginForm.value.password) {
    formErrors.value.login.password = '请输入密码'
    return
  }

  try {

    isLoading.value = true

    // 调用登录API
    const response = await login({
      username: loginForm.value.username,
      password: loginForm.value.password
    })

    console.log('服务器响应:', response)

    // 检查响应内容是否包含"登录成功"字样
    if ( response.msg.includes('操作成功')) {
      console.log('登录成功!')
      // 登录成功，跳转到首页
      router.push('/')
    } else {
      // 响应不包含"登录成功"，视为登录失败
      alert('登录失败: ' + (typeof response === 'string' ? response : '账号或密码错误'))
    }
  } catch (error) {
    console.error('登录请求失败:', error)
    console.log(error.response)
    // 处理登录错误
    if (error.response && error.response.data) {
      // 如果后端返回了错误信息，显示给用户
      alert(`登录失败: ${typeof error.response.data === 'string' ? error.response.data : '请检查您的账号和密码'}`)
    } else {
      // 否则显示通用错误信息
      alert('登录失败，请检查您的账号和密码或网络连接')
    }
  } finally {
    isLoading.value = false
  }
}

const handleRegister = async () => {
  // 重置错误信息
  formErrors.value.register.username = ''
  formErrors.value.register.password = ''
  formErrors.value.register.confirmPassword = ''
  formErrors.value.register.verificationCode = ''

  // 基本验证
  if (!registerForm.value.username) {
    formErrors.value.register.username = '请输入账号'
    return
  }

  if (!registerForm.value.password) {
    formErrors.value.register.password = '请输入密码'
    return
  }

  if (!registerForm.value.confirmPassword) {
    formErrors.value.register.confirmPassword = '请确认密码'
    return
  }

  if (registerForm.value.password !== registerForm.value.confirmPassword) {
    formErrors.value.register.confirmPassword = '两次输入的密码不一致'
    return
  }

  if (!registerForm.value.verificationCode) {
    formErrors.value.register.verificationCode = '请输入验证码'
    return
  }

  try {
    isLoading.value = true
    // 调用注册API
    const response = await doRegister({
      username: registerForm.value.username,
      password: registerForm.value.password,
      verificationCode: registerForm.value.verificationCode
    })

    console.log('注册成功:', response.data)

    // 注册成功，切换到登录页面
    alert('注册成功，请登录')
    switchTab('login')
    // 自动填充刚才注册的用户名
    loginForm.value.username = registerForm.value.username
  } catch (error) {
    console.error('注册失败:', error)
    // 处理注册错误
    if (error.response && error.response.data && error.response.data.message) {
      alert(`注册失败: ${error.response.data.message}`)
    } else {
      alert('注册失败，请稍后重试')
    }
  } finally {
    isLoading.value = false
  }
}

const handleCancel = () => {
  router.push('/')
}

const sendVerificationCodeHandler = async () => {
  // 验证用户名
  if (!registerForm.value.username) {
    formErrors.value.register.username = '请先输入账号'
    return
  }

  try {
    isLoading.value = true
    // 调用发送验证码API
    await sendVerificationCode(registerForm.value.username)
    alert('验证码已发送，请查收')
  } catch (error) {
    console.error('发送验证码失败:', error)
    if (error.response && error.response.data && error.response.data.message) {
      alert(`发送失败: ${error.response.data.message}`)
    } else {
      alert('发送验证码失败，请稍后重试')
    }
  } finally {
    isLoading.value = false
  }
}
onMounted(()=>{

})
</script>

<template>

  <div class="flex overflow-x-auto">

    <div class="min-w-[1440px] h-screen p-8 mockup-border flex items-center justify-center">


      <div class="w-full max-w-6xl grid grid-cols-2 gap-12 items-center">
        <!-- Left Side - Branding -->
        <div class="space-y-6">
          <div class="flex items-center gap-3 mb-8">
            <div class="w-16 h-16 rounded-2xl bg-gradient-to-br from-primary to-secondary flex items-center justify-center">
              <Play class="w-10 h-10" alt="play"/>
            </div>
            <div>
              <h1 class="text-4xl font-bold">AxoSpark</h1>
              <p class="text-gray-400 text-sm">二次元视频分享平台</p>
            </div>
          </div>

          <h2 class="text-5xl font-bold leading-tight">
            欢迎回到<br/>
            <span class="bg-gradient-to-r from-primary to-secondary bg-clip-text text-transparent">
                            动漫世界
                        </span>
          </h2>

          <p class="text-gray-400 text-lg leading-relaxed">
            探索无限精彩内容，与百万动漫爱好者一起分享热爱，创造属于你的二次元时光。
          </p>

          <div class="grid grid-cols-3 gap-4 pt-8">
            <div class="glass rounded-2xl p-4 text-center">
              <div class="text-3xl font-bold text-primary mb-1">1000+</div>
              <div class="text-sm text-gray-400">番剧资源</div>
            </div>
            <div class="glass rounded-2xl p-4 text-center">
              <div class="text-3xl font-bold text-secondary mb-1">500万+</div>
              <div class="text-sm text-gray-400">活跃用户</div>
            </div>
            <div class="glass rounded-2xl p-4 text-center">
              <div class="text-3xl font-bold text-primary mb-1">10亿+</div>
              <div class="text-sm text-gray-400">视频播放</div>
            </div>
          </div>
        </div>

        <!-- Right Side - Login Form -->
        <div class="gradient-border rounded-3xl p-8">
          <div class="mb-8">
            <h3 class="text-2xl font-bold mb-2">登录账号</h3>
            <p class="text-gray-400 text-sm">继续你的动漫之旅</p>
          </div>

          <form class="space-y-5" >
            <!-- Email/Username Input -->
            <div>
              <label class="block text-sm font-medium mb-2 text-gray-300">用户名</label>
              <div class="relative">
                <div class="absolute left-4 top-1/2 -translate-y-1/2">
                  <User class="w-5 h-5 opacity-50"/>
                </div>
                <input
                    type="text"
                    placeholder="请输入用户名"
                    class="input-glass w-full pl-12 pr-4 py-3 rounded-xl text-white placeholder-gray-500"
                    v-model="loginForm.username"
                >
              </div>
            </div>

            <!-- Password Input -->
            <div>
              <label class="block text-sm font-medium mb-2 text-gray-300">密码</label>
              <div class="relative">
                <div class="absolute left-4 top-1/2 -translate-y-1/2">
                  <Lock class="w-5 h-5 opacity-50" alt="lock"/>
                </div>
                <input
                    type="password"
                    placeholder="请输入密码"
                    class="input-glass w-full pl-12 pr-12 py-3 rounded-xl text-white placeholder-gray-500"
                    v-model="loginForm.password"
                >
                <button type="button" class="absolute right-4 top-1/2 -translate-y-1/2">
                  <img src="https://unpkg.com/lucide-static@latest/icons/eye-off.svg" class="w-5 h-5 opacity-50 invert hover:opacity-100 transition" alt="eye">
                </button>
              </div>
            </div>

            <!-- Remember & Forgot -->
            <div class="flex items-center justify-between text-sm">
              <label class="flex items-center gap-2 cursor-pointer">
                <input type="checkbox" class="w-4 h-4 rounded border-gray-600 bg-dark-700 text-primary focus:ring-primary focus:ring-offset-0">
                <span class="text-gray-400">记住我</span>
              </label>
              <a href="#" class="text-primary hover:text-secondary transition">忘记密码?</a>
            </div>

            <!-- Login Button -->
            <button
                type="button"
                class="w-full py-3 rounded-xl bg-gradient-to-r from-primary to-secondary font-medium hover:shadow-lg hover:shadow-primary/50 transition-all duration-300"
                @click="handleLogin"
            >
              登录
            </button>

            <!-- Divider -->
            <div class="relative my-6">
              <div class="absolute inset-0 flex items-center">
                <div class="w-full border-t border-gray-700"></div>
              </div>
              <div class="relative flex justify-center text-sm">
                <span class="px-4 bg-dark-700 text-gray-400">或使用以下方式登录</span>
              </div>
            </div>

            <!-- Social Login -->
            <div class="grid grid-cols-3 gap-3">
              <button type="button" class="glass rounded-xl p-3 hover:bg-dark-700 transition flex items-center justify-center gap-2">
                <img src="https://unpkg.com/lucide-static@latest/icons/github.svg" class="w-5 h-5 invert" alt="github">
              </button>
              <button type="button" class="glass rounded-xl p-3 hover:bg-dark-700 transition flex items-center justify-center gap-2">
                <img src="https://unpkg.com/lucide-static@latest/icons/mail.svg" class="w-5 h-5 invert" alt="google">
              </button>
              <button type="button" class="glass rounded-xl p-3 hover:bg-dark-700 transition flex items-center justify-center gap-2">
                <img src="https://unpkg.com/lucide-static@latest/icons/twitter.svg" class="w-5 h-5 invert" alt="twitter">
              </button>
            </div>

            <!-- Register Link -->
            <div class="text-center text-sm text-gray-400 pt-4">
              还没有账号?
              <a href="#" class="text-primary hover:text-secondary transition font-medium">立即注册</a>
            </div>
          </form>
        </div>
      </div>
    </div>

    <div class="min-w-[1440px] h-screen p-8 mockup-border flex items-center justify-center">

      <div class="w-full max-w-6xl grid grid-cols-2 gap-12 items-center">
        <!-- Left Side - Register Form -->
        <div class="gradient-border rounded-3xl p-8">
          <div class="mb-8">
            <h3 class="text-2xl font-bold mb-2">创建账号</h3>
            <p class="text-gray-400 text-sm">加入我们的动漫社区</p>
          </div>

          <form class="space-y-5">
            <!-- Username Input -->
            <div>
              <label class="block text-sm font-medium mb-2 text-gray-300">用户名</label>
              <div class="relative">
                <div class="absolute left-4 top-1/2 -translate-y-1/2">
                  <img src="https://unpkg.com/lucide-static@latest/icons/user.svg" class="w-5 h-5 opacity-50 invert" alt="user">
                </div>
                <input
                    type="text"
                    placeholder="请输入用户名"
                    class="input-glass w-full pl-12 pr-4 py-3 rounded-xl text-white placeholder-gray-500"
                >
              </div>
              <p class="text-xs text-gray-500 mt-1">用户名将作为你的唯一标识</p>
            </div>

            <!-- Email Input -->
            <div>
              <label class="block text-sm font-medium mb-2 text-gray-300">邮箱地址</label>
              <div class="relative">
                <div class="absolute left-4 top-1/2 -translate-y-1/2">
                  <img src="https://unpkg.com/lucide-static@latest/icons/mail.svg" class="w-5 h-5 opacity-50 invert" alt="mail">
                </div>
                <input
                    type="email"
                    placeholder="请输入邮箱地址"
                    class="input-glass w-full pl-12 pr-4 py-3 rounded-xl text-white placeholder-gray-500"
                >
              </div>
            </div>

            <!-- Password Input -->
            <div>
              <label class="block text-sm font-medium mb-2 text-gray-300">密码</label>
              <div class="relative">
                <div class="absolute left-4 top-1/2 -translate-y-1/2">
                  <img src="https://unpkg.com/lucide-static@latest/icons/lock.svg" class="w-5 h-5 opacity-50 invert" alt="lock">
                </div>
                <input
                    type="password"
                    placeholder="请输入密码"
                    class="input-glass w-full pl-12 pr-12 py-3 rounded-xl text-white placeholder-gray-500"
                >
                <button type="button" class="absolute right-4 top-1/2 -translate-y-1/2">
                  <img src="https://unpkg.com/lucide-static@latest/icons/eye-off.svg" class="w-5 h-5 opacity-50 invert hover:opacity-100 transition" alt="eye">
                </button>
              </div>
              <p class="text-xs text-gray-500 mt-1">至少8个字符，包含字母和数字</p>
            </div>

            <!-- Confirm Password Input -->
            <div>
              <label class="block text-sm font-medium mb-2 text-gray-300">确认密码</label>
              <div class="relative">
                <div class="absolute left-4 top-1/2 -translate-y-1/2">
                  <img src="https://unpkg.com/lucide-static@latest/icons/lock.svg" class="w-5 h-5 opacity-50 invert" alt="lock">
                </div>
                <input
                    type="password"
                    placeholder="请再次输入密码"
                    class="input-glass w-full pl-12 pr-12 py-3 rounded-xl text-white placeholder-gray-500"
                >
                <button type="button" class="absolute right-4 top-1/2 -translate-y-1/2">
                  <img src="https://unpkg.com/lucide-static@latest/icons/eye-off.svg" class="w-5 h-5 opacity-50 invert hover:opacity-100 transition" alt="eye">
                </button>
              </div>
            </div>

            <!-- Terms Agreement -->
            <div class="flex items-start gap-2 text-sm">
              <input type="checkbox" class="w-4 h-4 rounded border-gray-600 bg-dark-700 text-primary focus:ring-primary focus:ring-offset-0 mt-0.5">
              <label class="text-gray-400">
                我已阅读并同意
                <a href="#" class="text-primary hover:text-secondary transition">用户协议</a>
                和
                <a href="#" class="text-primary hover:text-secondary transition">隐私政策</a>
              </label>
            </div>

            <!-- Register Button -->
            <button
                type="submit"
                class="w-full py-3 rounded-xl bg-gradient-to-r from-primary to-secondary font-medium hover:shadow-lg hover:shadow-primary/50 transition-all duration-300"
            >
              注册账号
            </button>

            <!-- Divider -->
            <div class="relative my-6">
              <div class="absolute inset-0 flex items-center">
                <div class="w-full border-t border-gray-700"></div>
              </div>
              <div class="relative flex justify-center text-sm">
                <span class="px-4 bg-dark-700 text-gray-400">或使用以下方式注册</span>
              </div>
            </div>

            <!-- Social Register -->
            <div class="grid grid-cols-3 gap-3">
              <button type="button" class="glass rounded-xl p-3 hover:bg-dark-700 transition flex items-center justify-center gap-2">
                <img src="https://unpkg.com/lucide-static@latest/icons/github.svg" class="w-5 h-5 invert" alt="github">
              </button>
              <button type="button" class="glass rounded-xl p-3 hover:bg-dark-700 transition flex items-center justify-center gap-2">
                <img src="https://unpkg.com/lucide-static@latest/icons/mail.svg" class="w-5 h-5 invert" alt="google">
              </button>
              <button type="button" class="glass rounded-xl p-3 hover:bg-dark-700 transition flex items-center justify-center gap-2">
                <img src="https://unpkg.com/lucide-static@latest/icons/twitter.svg" class="w-5 h-5 invert" alt="twitter">
              </button>
            </div>

            <!-- Login Link -->
            <div class="text-center text-sm text-gray-400 pt-4">
              已有账号?
              <a href="#" class="text-primary hover:text-secondary transition font-medium">立即登录</a>
            </div>
          </form>
        </div>

        <!-- Right Side - Branding -->
        <div class="space-y-6">
          <div class="flex items-center gap-3 mb-8">
            <div class="w-16 h-16 rounded-2xl bg-gradient-to-br from-primary to-secondary flex items-center justify-center">
              <img src="https://unpkg.com/lucide-static@latest/icons/play.svg" class="w-10 h-10 invert" alt="play">
            </div>
            <div>
              <h1 class="text-4xl font-bold">AniTube</h1>
              <p class="text-gray-400 text-sm">二次元视频分享平台</p>
            </div>
          </div>

          <h2 class="text-5xl font-bold leading-tight">
            开启你的<br/>
            <span class="bg-gradient-to-r from-primary to-secondary bg-clip-text text-transparent">
                            动漫之旅
                        </span>
          </h2>

          <p class="text-gray-400 text-lg leading-relaxed">
            加入我们的社区，发现更多精彩内容，结识志同道合的朋友，分享你的创作与热爱。
          </p>

          <!-- Features List -->
          <div class="space-y-4 pt-8">
            <div class="flex items-start gap-4">
              <div class="w-12 h-12 rounded-xl bg-gradient-to-br from-primary/20 to-primary/10 flex items-center justify-center flex-shrink-0">
                <img src="https://unpkg.com/lucide-static@latest/icons/video.svg" class="w-6 h-6 invert opacity-80" alt="video">
              </div>
              <div>
                <h4 class="font-medium mb-1">海量视频资源</h4>
                <p class="text-sm text-gray-400">最新番剧、经典动画、原创内容应有尽有</p>
              </div>
            </div>

            <div class="flex items-start gap-4">
              <div class="w-12 h-12 rounded-xl bg-gradient-to-br from-secondary/20 to-secondary/10 flex items-center justify-center flex-shrink-0">
                <img src="https://unpkg.com/lucide-static@latest/icons/users.svg" class="w-6 h-6 invert opacity-80" alt="users">
              </div>
              <div>
                <h4 class="font-medium mb-1">活跃的社区</h4>
                <p class="text-sm text-gray-400">与百万动漫爱好者交流互动，分享快乐</p>
              </div>
            </div>

            <div class="flex items-start gap-4">
              <div class="w-12 h-12 rounded-xl bg-gradient-to-br from-primary/20 to-primary/10 flex items-center justify-center flex-shrink-0">
                <img src="https://unpkg.com/lucide-static@latest/icons/sparkles.svg" class="w-6 h-6 invert opacity-80" alt="sparkles">
              </div>
              <div>
                <h4 class="font-medium mb-1">个性化推荐</h4>
                <p class="text-sm text-gray-400">智能算法为你推荐感兴趣的内容</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>


  </div>
</template>

<style scoped>
* {
  scrollbar-width: none;
  -ms-overflow-style: none;
}
*::-webkit-scrollbar {
  display: none;
}
body {
  background: linear-gradient(135deg, #0a0a0f 0%, #13131a 50%, #1a1a24 100%);
}
.glass {
  background: rgba(26, 26, 36, 0.6);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
}
.glass-hover:hover {
  background: rgba(26, 26, 36, 0.8);
  border-color: rgba(255, 45, 149, 0.3);
}
.mockup-border {
  border-right: 2px dashed rgba(255, 255, 255, 0.2);
}
.input-glass {
  background: rgba(26, 26, 36, 0.4);
  border: 1px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
}
.input-glass:focus {
  background: rgba(26, 26, 36, 0.6);
  border-color: rgba(255, 45, 149, 0.5);
  outline: none;
}
.gradient-border {
  position: relative;
  background: rgba(26, 26, 36, 0.6);
  backdrop-filter: blur(20px);
}
.gradient-border::before {
  content: '';
  position: absolute;
  inset: 0;
  border-radius: inherit;
  padding: 1px;
  background: linear-gradient(135deg, #ff2d95, #00d9ff);
  -webkit-mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
  -webkit-mask-composite: xor;
  mask-composite: exclude;
  pointer-events: none;
}
</style>