<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { Play, Pause, SkipForward, Volume2, VolumeX, MessageSquare, Settings, Maximize, Eye, ThumbsUp, Star, Share2, Palette } from 'lucide-vue-next'
import WebHeader from "@/components/layout/WebHeader.vue";

// 视频播放状态
const videoRef = ref(null)
const isPlaying = ref(false)
const currentTime = ref(0)
const duration = ref(0)
const volume = ref(1)
const isMuted = ref(false)

// 弹幕相关
const danmakuText = ref('')
const danmakuColor = ref('#ffffff')
const showColorPicker = ref(false)
const danmakuList = ref([])
const danmakuId = ref(0)

// 颜色选项
const colors = ['#ffffff', '#ff1493', '#00d9ff', '#ffd700', '#00ff00', '#ff6600']

// 计算进度百分比
const progress = computed(() => {
  return duration.value > 0 ? (currentTime.value / duration.value) * 100 : 0
})

// 格式化时间
const formatTime = (seconds) => {
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
}

// 播放/暂停
const togglePlay = () => {
  if (!videoRef.value) return
  if (isPlaying.value) {
    videoRef.value.pause()
  } else {
    videoRef.value.play()
  }
  isPlaying.value = !isPlaying.value
}

// 更新时间
const updateTime = () => {
  if (videoRef.value) {
    currentTime.value = videoRef.value.currentTime
  }
}

// 加载元数据
const loadedMetadata = () => {
  if (videoRef.value) {
    duration.value = videoRef.value.duration
  }
}

// 点击进度条
const handleProgressClick = (e) => {
  if (!videoRef.value) return
  const rect = e.currentTarget.getBoundingClientRect()
  const percent = (e.clientX - rect.left) / rect.width
  videoRef.value.currentTime = percent * duration.value
}

// 快进
const skipForward = () => {
  if (videoRef.value) {
    videoRef.value.currentTime = Math.min(videoRef.value.currentTime + 10, duration.value)
  }
}

// 切换静音
const toggleMute = () => {
  if (videoRef.value) {
    videoRef.value.muted = !isMuted.value
    isMuted.value = !isMuted.value
  }
}

// 发送弹幕
const sendDanmaku = () => {
  if (!danmakuText.value.trim()) return

  const newDanmaku = {
    id: danmakuId.value++,
    text: danmakuText.value,
    color: danmakuColor.value,
    top: Math.random() * 60 + 15,
    timestamp: Date.now()
  }

  danmakuList.value.push(newDanmaku)
  danmakuText.value = ''

  // 8秒后移除弹幕
  setTimeout(() => {
    danmakuList.value = danmakuList.value.filter(d => d.id !== newDanmaku.id)
  }, 8000)
}

// 选择颜色
const selectColor = (color) => {
  danmakuColor.value = color
  showColorPicker.value = false
}

// 键盘事件 - 按回车发送
const handleKeyPress = (e) => {
  if (e.key === 'Enter') {
    sendDanmaku()
  }
}

// 组件挂载
onMounted(() => {
  if (videoRef.value) {
    videoRef.value.addEventListener('timeupdate', updateTime)
    videoRef.value.addEventListener('loadedmetadata', loadedMetadata)
  }
})

// 组件卸载
onUnmounted(() => {
  if (videoRef.value) {
    videoRef.value.removeEventListener('timeupdate', updateTime)
    videoRef.value.removeEventListener('loadedmetadata', loadedMetadata)
  }
})
</script>

<template>
  <div class="bg-dark-900 h-full flex flex-col">
    <!-- Header -->
    <WebHeader/>

    <div class="flex-1 flex overflow-hidden">
      <!-- Main Content -->
      <div class="flex-1 flex flex-col p-6 gap-6 overflow-y-auto">
        <!-- Video Player -->
        <div class="glass rounded-2xl overflow-hidden">
          <div class="relative aspect-video bg-black">
            <!-- 视频元素 -->
            <video
                ref="videoRef"
                class="w-full h-full"
                src="https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                @click="togglePlay"
            ></video>

            <!-- Danmaku Overlay -->
            <div class="absolute inset-0 overflow-hidden pointer-events-none">
              <div
                  v-for="danmaku in danmakuList"
                  :key="danmaku.id"
                  class="danmaku"
                  :style="{
                  top: danmaku.top + '%',
                  color: danmaku.color
                }"
              >
                {{ danmaku.text }}
              </div>
            </div>

            <!-- Play Button Overlay -->
            <div
                v-if="!isPlaying"
                class="absolute inset-0 flex items-center justify-center bg-black/20"
                @click="togglePlay"
            >
              <button class="glass rounded-full p-6 hover:scale-110 transition-transform">
                <Play class="w-12 h-12" :fill="'white'" />
              </button>
            </div>

            <!-- Video Controls -->
            <div class="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/80 to-transparent p-6">
              <!-- Progress Bar -->
              <div
                  class="progress-bar rounded-full mb-4"
                  @click="handleProgressClick"
              >
                <div
                    class="progress-fill rounded-full"
                    :style="{ width: progress + '%' }"
                >
                  <div class="progress-thumb"></div>
                </div>
              </div>

              <div class="flex items-center justify-between">
                <div class="flex items-center gap-4">
                  <button @click="togglePlay" class="hover:text-primary transition">
                    <Play v-if="!isPlaying" class="w-5 h-5" />
                    <Pause v-else class="w-5 h-5" />
                  </button>
                  <button @click="skipForward" class="hover:text-primary transition">
                    <SkipForward class="w-5 h-5" />
                  </button>
                  <button @click="toggleMute" class="hover:text-primary transition">
                    <Volume2 v-if="!isMuted" class="w-5 h-5" />
                    <VolumeX v-else class="w-5 h-5" />
                  </button>
                  <span class="text-sm text-white/80">
                    {{ formatTime(currentTime) }} / {{ formatTime(duration) }}
                  </span>
                </div>
                <div class="flex items-center gap-4">
                  <button class="hover:text-primary transition">
                    <MessageSquare class="w-5 h-5" />
                  </button>
                  <button class="hover:text-primary transition">
                    <Settings class="w-5 h-5" />
                  </button>
                  <button class="hover:text-primary transition">
                    <Maximize class="w-5 h-5" />
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- Danmaku Input -->
          <div class="p-4 border-t border-white/5">
            <div class="flex items-center gap-3">
              <input
                  v-model="danmakuText"
                  @keypress="handleKeyPress"
                  type="text"
                  placeholder="发送弹幕..."
                  class="flex-1 bg-white/5 rounded-full px-4 py-2 text-sm outline-none focus:bg-white/10 transition"
              />
              <div class="relative">
                <button
                    @click="showColorPicker = !showColorPicker"
                    class="glass rounded-full px-4 py-2 text-sm hover:bg-white/10 transition flex items-center gap-2"
                >
                  <Palette class="w-4 h-4" />
                  <div class="w-4 h-4 rounded-full" :style="{ backgroundColor: danmakuColor }"></div>
                </button>
                <div
                    v-if="showColorPicker"
                    class="absolute bottom-full mb-2 right-0 glass rounded-lg p-3 flex gap-2"
                >
                  <button
                      v-for="color in colors"
                      :key="color"
                      @click="selectColor(color)"
                      class="w-8 h-8 rounded-full border-2 border-white/20 hover:scale-110 transition"
                      :style="{ backgroundColor: color }"
                  ></button>
                </div>
              </div>
              <button
                  @click="sendDanmaku"
                  class="gradient-bg rounded-full px-6 py-2 text-sm font-medium hover:opacity-90 transition"
              >
                发送
              </button>
            </div>
          </div>
        </div>

        <!-- Video Info -->
        <div class="glass rounded-2xl p-6">
          <h1 class="text-2xl font-bold mb-3">【新番】某科学的超电磁炮 第01话</h1>
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center gap-6 text-sm text-white/60">
              <span class="flex items-center gap-1">
                <Eye class="w-4 h-4" />
                1.2M
              </span>
              <span class="flex items-center gap-1">
                <MessageSquare class="w-4 h-4" />
                8.5K
              </span>
              <span>2024-01-15</span>
            </div>
            <div class="flex items-center gap-3">
              <button class="glass rounded-full px-4 py-2 text-sm hover:bg-white/10 transition flex items-center gap-2">
                <ThumbsUp class="w-4 h-4" />
                <span>12.5K</span>
              </button>
              <button class="glass rounded-full px-4 py-2 text-sm hover:bg-white/10 transition flex items-center gap-2">
                <Star class="w-4 h-4" />
                收藏
              </button>
              <button class="glass rounded-full px-4 py-2 text-sm hover:bg-white/10 transition flex items-center gap-2">
                <Share2 class="w-4 h-4" />
                分享
              </button>
            </div>
          </div>
          <div class="flex items-start gap-4">
            <div class="w-12 h-12 rounded-full bg-gradient-to-br from-primary to-secondary"></div>
            <div class="flex-1">
              <div class="flex items-center gap-2 mb-2">
                <span class="font-medium">动画工作室</span>
                <span class="text-xs glass rounded-full px-2 py-1">认证</span>
              </div>
              <p class="text-sm text-white/60 leading-relaxed">
                学园都市，是从东京西部分割出来的都市，人口约有230万人，其中约有八成人口为学生，所以被称作「学园都市」...
              </p>
            </div>
            <button class="gradient-bg rounded-full px-6 py-2 text-sm font-medium hover:opacity-90 transition">
              关注
            </button>
          </div>
        </div>

        <!-- Comments -->
        <div class="glass rounded-2xl p-6">
          <div class="flex items-center justify-between mb-6">
            <h2 class="text-lg font-bold">评论 8,542</h2>
            <div class="flex items-center gap-2">
              <button class="text-sm text-primary">最新</button>
              <span class="text-white/20">|</span>
              <button class="text-sm text-white/60 hover:text-white transition">最热</button>
            </div>
          </div>

          <!-- Comment Item -->
          <div class="space-y-4">
            <div class="flex gap-4">
              <div class="w-10 h-10 rounded-full bg-gradient-to-br from-primary to-secondary flex-shrink-0"></div>
              <div class="flex-1">
                <div class="flex items-center gap-2 mb-1">
                  <span class="font-medium text-sm">动漫爱好者</span>
                  <span class="text-xs text-white/40">2小时前</span>
                </div>
                <p class="text-sm text-white/80 mb-2">这一集的作画质量真的太高了！期待下一集！</p>
                <div class="flex items-center gap-4 text-xs text-white/60">
                  <button class="hover:text-primary transition flex items-center gap-1">
                    <ThumbsUp class="w-3 h-3" />
                    256
                  </button>
                  <button class="hover:text-primary transition">回复</button>
                </div>
              </div>
            </div>

            <div class="flex gap-4">
              <div class="w-10 h-10 rounded-full bg-gradient-to-br from-secondary to-primary flex-shrink-0"></div>
              <div class="flex-1">
                <div class="flex items-center gap-2 mb-1">
                  <span class="font-medium text-sm">二次元世界</span>
                  <span class="text-xs text-white/40">5小时前</span>
                </div>
                <p class="text-sm text-white/80 mb-2">炮姐永远的神！</p>
                <div class="flex items-center gap-4 text-xs text-white/60">
                  <button class="hover:text-primary transition flex items-center gap-1">
                    <ThumbsUp class="w-3 h-3" />
                    189
                  </button>
                  <button class="hover:text-primary transition">回复</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Sidebar - Related Videos -->
      <div class="w-96 p-6 border-l border-white/5 overflow-y-auto">
        <h3 class="text-lg font-bold mb-4">相关推荐</h3>
        <div class="space-y-3">
          <!-- Related Video Item -->
          <div class="glass rounded-xl overflow-hidden hover:bg-white/10 transition cursor-pointer">
            <div class="flex gap-3 p-3">
              <div class="relative w-40 h-24 flex-shrink-0 rounded-lg overflow-hidden">
                <img src="https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=400&h=240&fit=crop"
                     alt="Related" class="w-full h-full object-cover">
                <div class="absolute bottom-1 right-1 bg-black/80 rounded px-1.5 py-0.5 text-xs">24:15</div>
              </div>
              <div class="flex-1 min-w-0">
                <h4 class="text-sm font-medium mb-1 line-clamp-2">某科学的超电磁炮 第02话</h4>
                <p class="text-xs text-white/60 mb-2">动画工作室</p>
                <div class="flex items-center gap-2 text-xs text-white/40">
                  <span>856K观看</span>
                  <span>•</span>
                  <span>1天前</span>
                </div>
              </div>
            </div>
          </div>

          <div class="glass rounded-xl overflow-hidden hover:bg-white/10 transition cursor-pointer">
            <div class="flex gap-3 p-3">
              <div class="relative w-40 h-24 flex-shrink-0 rounded-lg overflow-hidden">
                <img src="https://images.unsplash.com/photo-1613376023733-0a73315d9b06?w=400&h=240&fit=crop"
                     alt="Related" class="w-full h-full object-cover">
                <div class="absolute bottom-1 right-1 bg-black/80 rounded px-1.5 py-0.5 text-xs">23:45</div>
              </div>
              <div class="flex-1 min-w-0">
                <h4 class="text-sm font-medium mb-1 line-clamp-2">某科学的超电磁炮 第03话</h4>
                <p class="text-xs text-white/60 mb-2">动画工作室</p>
                <div class="flex items-center gap-2 text-xs text-white/40">
                  <span>723K观看</span>
                  <span>•</span>
                  <span>2天前</span>
                </div>
              </div>
            </div>
          </div>

          <div class="glass rounded-xl overflow-hidden hover:bg-white/10 transition cursor-pointer">
            <div class="flex gap-3 p-3">
              <div class="relative w-40 h-24 flex-shrink-0 rounded-lg overflow-hidden">
                <img src="https://images.unsplash.com/photo-1578632292335-df3abbb0d586?w=400&h=240&fit=crop"
                     alt="Related" class="w-full h-full object-cover">
                <div class="absolute bottom-1 right-1 bg-black/80 rounded px-1.5 py-0.5 text-xs">24:30</div>
              </div>
              <div class="flex-1 min-w-0">
                <h4 class="text-sm font-medium mb-1 line-clamp-2">刀剑神域 精彩片段合集</h4>
                <p class="text-xs text-white/60 mb-2">ACG频道</p>
                <div class="flex items-center gap-2 text-xs text-white/40">
                  <span>1.2M观看</span>
                  <span>•</span>
                  <span>3天前</span>
                </div>
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

.gradient-text {
  background: linear-gradient(
      90deg,
      var(--color-primary),
      var(--color-secondary)
  );
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.gradient-bg {
  background: linear-gradient(
      90deg,
      var(--color-primary),
      var(--color-secondary)
  );
}

.glass {
  background: rgba(26, 26, 39, 0.7);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.danmaku {
  position: absolute;
  white-space: nowrap;
  font-size: 1.125rem;
  font-weight: 600;
  color: white;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.8), -1px -1px 2px rgba(0, 0, 0, 0.8);
  animation: danmaku-scroll 8s linear;
  pointer-events: none;
}

@keyframes danmaku-scroll {
  from { transform: translateX(100vw); }
  to { transform: translateX(-100%); }
}

.progress-bar {
  position: relative;
  height: 4px;
  background: rgba(255, 255, 255, 0.2);
  cursor: pointer;
  transition: height 0.2s;
}

.progress-bar:hover {
  height: 6px;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(
      90deg,
      var(--color-primary),
      var(--color-secondary)
  );
  position: relative;
}

.progress-thumb {
  position: absolute;
  right: -6px;
  top: 50%;
  transform: translateY(-50%);
  width: 12px;
  height: 12px;
  background: white;
  border-radius: 50%;
  box-shadow: 0 0 8px rgba(255, 20, 147, 0.5);
}
</style>