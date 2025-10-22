<script setup>
import { ref, computed } from 'vue'
import {postVideo,upload} from '@/api/io.js'

const selectedFile = ref(null)
const uploading = ref(false)
const uploadProgress = ref(0)
const processingSteps = ref({
  upload: 'pending',
  thumbnail: 'pending',
  transcode: 'pending'
})
const formData = ref({
  id: null,
  title: '',
  description: '',
  category: 1,
  filePath: ''
})
const dragActive = ref(false)
const fileInputRef = ref(null)

const handleBlock = (e) => {
  e.preventDefault()
  e.stopPropagation()
  if (e.type === "dragenter" || e.type === "dragover" || e.type === "mouseover" || e.type === "mouseenter") {
    dragActive.value = true
  } else if (e.type === "dragleave" || e.type === "mouseout" || e.type === "mouseleave") {
    dragActive.value = false
  }
}

const handleDrop = (e) => {
  e.preventDefault()
  e.stopPropagation()
  dragActive.value = false

  if (e.dataTransfer.files && e.dataTransfer.files[0]) {
    handleFile(e.dataTransfer.files[0])
  }
}

const handleFileInput = (e) => {
  if (e.target.files && e.target.files[0]) {
    handleFile(e.target.files[0])
  }
}

const handleFile = async (file) => {
  const validTypes = ['video/mp4', 'video/quicktime', 'video/x-msvideo']
  const maxSize = 2 * 1024 * 1024 * 1024 // 2GB

  if (!validTypes.includes(file.type)) {
    alert('请上传 MP4, MOV 或 AVI 格式的视频文件')
    return
  }

  if (file.size > maxSize) {
    alert('文件大小不能超过 2GB')
    return
  }

  selectedFile.value = file
  formData.value.title = file.name.replace(/\.[^/.]+$/, '')

  // 立即开始上传并显示进度
  uploading.value = true
  uploadProgress.value = 0
  processingSteps.value = {
    upload: 'progress',
    thumbnail: 'pending',
    transcode: 'pending'
  }

  let uploadFormData = new FormData()
  uploadFormData.append('videoFile', selectedFile.value)

  // 模拟上传进度
  const progressInterval = setInterval(() => {
    if (uploadProgress.value < 90) {
      uploadProgress.value += 5
    }
  }, 200)

  try {
    const result = await upload(uploadFormData)
    console.log(result.data.id)
    formData.value.id = result.data.id
    formData.value.filePath = result.data.filePath
    clearInterval(progressInterval)
    uploadProgress.value = 100
    processingSteps.value.upload = 'completed'
    processingSteps.value.thumbnail = 'progress'

    // 模拟缩略图生成
    setTimeout(() => {
      processingSteps.value.thumbnail = 'completed'
      processingSteps.value.transcode = 'progress'

      // 模拟转码
      setTimeout(() => {
        processingSteps.value.transcode = 'completed'
        uploading.value = false
        console.log('上传成功', result)
      }, 1500)
    }, 1500)
  } catch (error) {
    clearInterval(progressInterval)
    uploading.value = false
    console.error('上传失败', error)
    alert('视频上传失败')
    // 重置状态
    selectedFile.value = null
    uploadProgress.value = 0
    processingSteps.value = {
      upload: 'pending',
      thumbnail: 'pending',
      transcode: 'pending'
    }
  }
}

// 判断上传是否完成
const uploadCompleted = computed(() => {
  return processingSteps.value.upload === 'completed' &&
      processingSteps.value.thumbnail === 'completed' &&
      processingSteps.value.transcode === 'completed'
})

const handleSubmit = async () => {
  if (!selectedFile.value) {
    alert('请先选择视频文件')
    return
  }
  if (!formData.value.title.trim()) {
    alert('请输入视频标题')
    return
  }

  // 调用发布接口
  try {
    const publishData = {
      id: formData.value.id,
      description: formData.value.description,
      category: formData.value.category,
      title: formData.value.title,
      filePath: formData.value.filePath,

    }

    // 需要在 @/api/io.js 中添加 postVideo 接口
    // import { upload, postVideo } from '@/api/io.js'
    const result = await postVideo(publishData)

    // 临时模拟请求
    console.log('发布视频:', publishData)

    // 这里可以调用实际的发布接口
    // const response = await fetch('http://localhost:8080/video/postVideo', {
    //   method: 'POST',
    //   headers: { 'Content-Type': 'application/json' },
    //   body: JSON.stringify(publishData)
    // })

    alert('视频发布成功！')

    // 重置表单
    handleCancel()
  } catch (error) {
    console.error('发布失败', error)
    alert('视频发布失败')
  }
}

const handleCancel = () => {
  if (uploadCompleted.value) {
    if (!confirm('视频已上传完成，确定要删除重传吗？')) {
      return
    }
  }

  selectedFile.value = null
  uploadProgress.value = 0
  uploading.value = false
  processingSteps.value = {
    upload: 'pending',
    thumbnail: 'pending',
    transcode: 'pending'
  }
  formData.value = {
    title: '',
    description: '',
    category: 'animation'
  }
}

const getStepStatus = (step) => {
  return processingSteps.value[step]
}

const getCategoryName = (category) => {
  const names = {
    animation: '动画',
    music: '音乐',
    game: '游戏',
    vlog: '生活',
    tech: '科技',
    entertainment: '娱乐'
  }
  return names[category] || category
}

const fileSizeMB = computed(() => {
  return selectedFile.value ? (selectedFile.value.size / (1024 * 1024)).toFixed(2) : 0
})

const remainingTime = computed(() => {
  return Math.ceil((100 - uploadProgress.value) / 10)
})

import WebHeader from "@/components/layout/WebHeader.vue";
</script>

<template>
  <WebHeader/>
  <div class="mockup-container">
    <div class="mockup mockup-desktop">
      <div class="h-full overflow-y-auto">
        <main class="max-w-7xl mx-auto px-6 py-8">
          <div class="mb-8">
            <h2 class="text-3xl font-bold mb-2">上传视频</h2>
            <p class="text-gray-400">分享你的精彩内容给大家</p>
          </div>

          <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
            <!-- 左侧：上传区域和表单 -->
            <div class="lg:col-span-2 space-y-6">
              <!-- 上传区域 -->
              <div class="bg-white/5 backdrop-blur-sm rounded-2xl p-8 border border-white/10">
                <div
                    :class="[
                'border-2 border-dashed rounded-xl p-12 text-center transition-colors',
                dragActive ? 'border-purple-500 bg-purple-500/10 cursor-pointer' : 'border-gray-600'
              ]"
                    @dragenter="handleBlock"
                    @dragleave="handleBlock"
                    @dragover="handleBlock"
                    @drop="handleDrop"
                    @click="$refs.fileInput.click()"
                    @mouseenter="handleBlock"
                    @mouseover="handleBlock"
                    @mouseleave="handleBlock"
                    @mouseout="handleBlock"
                >
                  <div v-if="!selectedFile" class="flex flex-col items-center gap-4">
                    <div class="w-20 h-20 rounded-full bg-gradient-to-br from-primary to-secondary flex items-center justify-center">
                      <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                        <polyline points="17 8 12 3 7 8"></polyline>
                        <line x1="12" y1="3" x2="12" y2="15"></line>
                      </svg>
                    </div>
                    <div>
                      <h3 class="text-xl font-bold mb-2">拖拽视频文件到这里</h3>
                      <p class="text-gray-400 mb-4">或点击选择文件</p>
                      <p class="text-sm text-gray-500">支持 MP4, MOV, AVI 格式，最大 2GB</p>
                    </div>
                    <button
                        class="px-8 py-3 bg-gradient-to-r from-primary to-secondary rounded-lg font-medium hover:opacity-90 transition cursor-pointer"
                    >
                      选择文件
                    </button>
                    <input
                        ref="fileInput"
                        type="file"
                        accept="video/mp4,video/quicktime,video/x-msvideo"
                        @change="handleFileInput"
                        class="hidden"
                    />
                  </div>
                  <div v-else class="flex items-center gap-4">
                    <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-purple-500">
                      <rect x="2" y="2" width="20" height="20" rx="2.18" ry="2.18"></rect>
                      <line x1="7" y1="2" x2="7" y2="22"></line>
                      <line x1="17" y1="2" x2="17" y2="22"></line>
                      <line x1="2" y1="12" x2="22" y2="12"></line>
                      <line x1="2" y1="7" x2="7" y2="7"></line>
                      <line x1="2" y1="17" x2="7" y2="17"></line>
                      <line x1="17" y1="17" x2="22" y2="17"></line>
                      <line x1="17" y1="7" x2="22" y2="7"></line>
                    </svg>
                    <div class="flex-1 text-left">
                      <h3 class="font-medium mb-1">{{ selectedFile.name }}</h3>
                      <p class="text-sm text-gray-400">{{ fileSizeMB }} MB</p>
                    </div>
                    <button
                        v-if="selectedFile"
                        @click.stop="handleCancel"
                        class="p-2 hover:bg-white/10 rounded-lg transition"
                    >
                      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <line x1="18" y1="6" x2="6" y2="18"></line>
                        <line x1="6" y1="6" x2="18" y2="18"></line>
                      </svg>
                    </button>
                  </div>
                </div>
              </div>
              <!-- 上传进度卡片 -->
              <div v-if="uploading || uploadCompleted" class="mt-6 bg-white/5 backdrop-blur-sm rounded-2xl p-8 border border-white/10">
                <div class="flex items-start gap-4 mb-6">
                  <div class="w-32 h-20 rounded-lg bg-slate-800 flex items-center justify-center flex-shrink-0">
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-gray-500">
                      <rect x="2" y="2" width="20" height="20" rx="2.18" ry="2.18"></rect>
                      <line x1="7" y1="2" x2="7" y2="22"></line>
                      <line x1="17" y1="2" x2="17" y2="22"></line>
                      <line x1="2" y1="12" x2="22" y2="12"></line>
                    </svg>
                  </div>
                  <div class="flex-1">
                    <h3 class="font-medium mb-1">{{ selectedFile?.name }}</h3>
                    <p class="text-sm text-gray-400">
                      {{ fileSizeMB }} MB • {{ getCategoryName(formData.category) }}分类
                    </p>
                  </div>
                </div>

                <div class="space-y-4">
                  <div>
                    <div class="flex justify-between text-sm mb-2">
                      <span>上传进度</span>
                      <span class="text-primary">{{ uploadProgress }}%</span>
                    </div>
                    <div class="h-2 bg-slate-800 rounded-full overflow-hidden">
                      <div
                          class="h-full bg-gradient-to-r from-primary to-secondary rounded-full transition-all duration-300"
                          :style="{ width: uploadProgress + '%' }"
                      />
                    </div>
                    <p v-if="uploadProgress < 100" class="text-xs text-gray-500 mt-2">
                      剩余时间: 约 {{ remainingTime }} 分钟
                    </p>
                  </div>

                  <div class="pt-4 border-t border-white/10">
                    <div class="flex items-center justify-between text-sm mb-2">
              <span class="flex items-center gap-2">
                <svg v-if="getStepStatus('upload') === 'completed'" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-green-500">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                  <polyline points="22 4 12 14.01 9 11.01"></polyline>
                </svg>
                <svg v-else-if="getStepStatus('upload') === 'progress'" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-purple-500 animate-spin">
                  <line x1="12" y1="2" x2="12" y2="6"></line>
                  <line x1="12" y1="18" x2="12" y2="22"></line>
                  <line x1="4.93" y1="4.93" x2="7.76" y2="7.76"></line>
                  <line x1="16.24" y1="16.24" x2="19.07" y2="19.07"></line>
                  <line x1="2" y1="12" x2="6" y2="12"></line>
                  <line x1="18" y1="12" x2="22" y2="12"></line>
                  <line x1="4.93" y1="19.07" x2="7.76" y2="16.24"></line>
                  <line x1="16.24" y1="7.76" x2="19.07" y2="4.93"></line>
                </svg>
                <svg v-else xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-gray-500">
                  <circle cx="12" cy="12" r="10"></circle>
                </svg>
                视频上传
              </span>
                      <span :class="getStepStatus('upload') === 'completed' ? 'text-green-500' : getStepStatus('upload') === 'progress' ? 'text-purple-500' : 'text-gray-500'">
                {{ getStepStatus('upload') === 'completed' ? '完成' : getStepStatus('upload') === 'progress' ? '进行中' : '等待中' }}
              </span>
                    </div>
                    <div class="flex items-center justify-between text-sm mb-2">
              <span class="flex items-center gap-2">
                <svg v-if="getStepStatus('thumbnail') === 'completed'" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-green-500">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                  <polyline points="22 4 12 14.01 9 11.01"></polyline>
                </svg>
                <svg v-else-if="getStepStatus('thumbnail') === 'progress'" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-purple-500 animate-spin">
                  <line x1="12" y1="2" x2="12" y2="6"></line>
                  <line x1="12" y1="18" x2="12" y2="22"></line>
                  <line x1="4.93" y1="4.93" x2="7.76" y2="7.76"></line>
                  <line x1="16.24" y1="16.24" x2="19.07" y2="19.07"></line>
                  <line x1="2" y1="12" x2="6" y2="12"></line>
                  <line x1="18" y1="12" x2="22" y2="12"></line>
                  <line x1="4.93" y1="19.07" x2="7.76" y2="16.24"></line>
                  <line x1="16.24" y1="7.76" x2="19.07" y2="4.93"></line>
                </svg>
                <svg v-else xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-gray-500">
                  <circle cx="12" cy="12" r="10"></circle>
                </svg>
                生成封面
              </span>
                      <span :class="getStepStatus('thumbnail') === 'completed' ? 'text-green-500' : getStepStatus('thumbnail') === 'progress' ? 'text-purple-500' : 'text-gray-500'">
                {{ getStepStatus('thumbnail') === 'completed' ? '完成' : getStepStatus('thumbnail') === 'progress' ? '进行中' : '等待中' }}
              </span>
                    </div>
                    <div class="flex items-center justify-between text-sm">
              <span class="flex items-center gap-2">
                <svg v-if="getStepStatus('transcode') === 'completed'" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-green-500">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                  <polyline points="22 4 12 14.01 9 11.01"></polyline>
                </svg>
                <svg v-else-if="getStepStatus('transcode') === 'progress'" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-purple-500 animate-spin">
                  <line x1="12" y1="2" x2="12" y2="6"></line>
                  <line x1="12" y1="18" x2="12" y2="22"></line>
                  <line x1="4.93" y1="4.93" x2="7.76" y2="7.76"></line>
                  <line x1="16.24" y1="16.24" x2="19.07" y2="19.07"></line>
                  <line x1="2" y1="12" x2="6" y2="12"></line>
                  <line x1="18" y1="12" x2="22" y2="12"></line>
                  <line x1="4.93" y1="19.07" x2="7.76" y2="16.24"></line>
                  <line x1="16.24" y1="7.76" x2="19.07" y2="4.93"></line>
                </svg>
                <svg v-else xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-gray-500">
                  <circle cx="12" cy="12" r="10"></circle>
                </svg>
                转码处理
              </span>
                      <span :class="getStepStatus('transcode') === 'completed' ? 'text-green-500' : getStepStatus('transcode') === 'progress' ? 'text-purple-500' : 'text-gray-500'">
                {{ getStepStatus('transcode') === 'completed' ? '完成' : getStepStatus('transcode') === 'progress' ? '进行中' : '等待中' }}
              </span>
                    </div>
                  </div>
                </div>
              </div>
              <!-- 视频信息表单 -->
              <div v-if="selectedFile" class="bg-white/5 backdrop-blur-sm rounded-2xl p-8 border border-white/10">
                <h3 class="text-xl font-bold mb-6">视频信息</h3>
                <div class="space-y-4">
                  <div>
                    <label class="block text-sm font-medium mb-2">标题 *</label>
                    <input
                        type="text"
                        v-model="formData.title"
                        placeholder="给你的视频起个标题"
                        :disabled="!uploadCompleted"
                        class="w-full px-4 py-3 bg-slate-800/50 border border-gray-600 rounded-lg focus:outline-none focus:border-purple-500 transition disabled:opacity-50"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium mb-2">简介</label>
                    <textarea
                        v-model="formData.description"
                        placeholder="介绍一下你的视频内容"
                        :disabled="!uploadCompleted"
                        rows="4"
                        class="w-full px-4 py-3 bg-slate-800/50 border border-gray-600 rounded-lg focus:outline-none focus:border-purple-500 transition disabled:opacity-50"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium mb-2">分类</label>
                    <select
                        v-model="formData.category"
                        :disabled="!uploadCompleted"
                        class="w-full px-4 py-3 bg-slate-800/50 border border-gray-600 rounded-lg focus:outline-none focus:border-purple-500 transition disabled:opacity-50"
                    >
                      <option value="1">动画</option>
                      <option value="2">音乐</option>
                      <option value="3">游戏</option>
                      <option value="4">生活</option>
                      <option value="5">科技</option>
                      <option value="6">娱乐</option>
                    </select>
                  </div>
                </div>
              </div>
            </div>

            <!-- 右侧：提示和操作 -->
            <div class="space-y-6">
              <div class="bg-white/5 backdrop-blur-sm rounded-2xl p-6 border border-white/10">
                <h3 class="font-bold mb-4">上传须知</h3>
                <ul class="space-y-3 text-sm text-gray-400">
                  <li class="flex items-start gap-2">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="mt-0.5 flex-shrink-0 text-green-500">
                      <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                      <polyline points="22 4 12 14.01 9 11.01"></polyline>
                    </svg>
                    <span>支持 MP4, MOV, AVI 格式</span>
                  </li>
                  <li class="flex items-start gap-2">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="mt-0.5 flex-shrink-0 text-green-500">
                      <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                      <polyline points="22 4 12 14.01 9 11.01"></polyline>
                    </svg>
                    <span>文件大小不超过 2GB</span>
                  </li>
                  <li class="flex items-start gap-2">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="mt-0.5 flex-shrink-0 text-green-500">
                      <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                      <polyline points="22 4 12 14.01 9 11.01"></polyline>
                    </svg>
                    <span>请确保内容符合社区规范</span>
                  </li>
                  <li class="flex items-start gap-2">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="mt-0.5 flex-shrink-0 text-green-500">
                      <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                      <polyline points="22 4 12 14.01 9 11.01"></polyline>
                    </svg>
                    <span>上传后会自动生成封面</span>
                  </li>
                </ul>
              </div>

              <button
                  v-if="selectedFile"
                  @click="handleSubmit"
                  :disabled="!uploadCompleted || !formData.title.trim()"
                  class="w-full py-4 bg-gradient-to-r from-primary to-secondary rounded-lg font-bold text-lg hover:opacity-90 transition disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {{ uploadCompleted ? '发布' : '处理中...' }}
              </button>
            </div>
          </div>


        </main>
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
  background: linear-gradient(135deg, #0a0a0f 0%, #1a1a2e 100%);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
}

.glass {
  background: rgba(22, 22, 42, 0.7);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.glass-light {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.mockup-container {
  display: flex;
  gap: 2rem;
  padding: 2rem;
  overflow-x: auto;
  min-height: 100vh;
}

.mockup {
  flex-shrink: 0;
  border: 2px solid rgba(255, 20, 147, 0.3);
  border-radius: 1rem;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
}

.mockup-desktop {
  width: 1440px;
  height: 900px;
}

.mockup-tablet {
  width: 768px;
  height: 1024px;
}

.mockup-mobile {
  width: 375px;
  height: 812px;
}

.gradient-text {
  background: linear-gradient(135deg, #ff1493 0%, #00d9ff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.upload-zone {
  border: 2px dashed rgba(255, 20, 147, 0.5);
  transition: all 0.3s ease;
}

.upload-zone:hover {
  border-color: #ff1493;
  background: rgba(255, 20, 147, 0.05);
}

.progress-bar {
  background: linear-gradient(90deg, #ff1493 0%, #00d9ff 100%);
  animation: progress 2s ease-in-out infinite;
}

@keyframes progress {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.7; }
}

.thumbnail-option {
  cursor: pointer;
  transition: all 0.3s ease;
}

.thumbnail-option:hover {
  transform: scale(1.05);
  border-color: #ff1493;
}

.thumbnail-option.selected {
  border-color: #ff1493;
  box-shadow: 0 0 20px rgba(255, 20, 147, 0.5);
}

input, textarea, select {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: white;
  transition: all 0.3s ease;
}

input:focus, textarea:focus, select:focus {
  outline: none;
  border-color: #ff1493;
  background: rgba(255, 255, 255, 0.08);
}

.tag-input {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  padding: 0.75rem;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 0.5rem;
}

.tag {
  background: linear-gradient(135deg, rgba(255, 20, 147, 0.2), rgba(0, 217, 255, 0.2));
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
</style>