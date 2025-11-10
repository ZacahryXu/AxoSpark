<script setup>
import {ref, computed, onMounted} from 'vue'
import {postVideo, upload} from '@/api/io.js'
import {getVideoAllCategories} from '@/api/catrgories.js'
import {X} from 'lucide-vue-next'
import {useRouter} from 'vue-router'
import SparkMd5 from "spark-md5";
const CHUNK_SIZE = 10*1024*1024;
const fileHash  = ref('');
const fileName = ref('');
const router = useRouter()
const selectedFile = ref(null)
const uploading = ref(false)
const uploadProgress = ref(0)
const processingSteps = ref({
  upload: 'pending',
  thumbnail: 'pending',
  transcode: 'pending'
})
const publishing = ref(false)
const publishSuccess = ref(false)

const formData = ref({
  id: null,
  title: '',
  description: '',
  category: null,
  filePath: '',
  tagsNames: [],
  createTime: null
})

const dragActive = ref(false)
const fileInputRef = ref(null)
const categories = ref([])
const inputValue = ref('')
const maxTags = 10
const createChunks = (file)=>{
  let cur = 0;
  let chunks = [];
  while(cur < file.size){
    const blob = file.slice(cur, cur + CHUNK_SIZE);
    chunks.push(blob);
    cur += CHUNK_SIZE;
  }
  return chunks;
}
//计算hash值
const calculateHash = (chunks)=>{
  return new Promise(resolve => {
    //1.第一个和最后一个切片全部参与计算
    //2.中间的切片只计算前面两个字节、中间两个字节、最后两个字节
    const targets = []; //存储所有参与计算的切片
    const spark = new SparkMD5.ArrayBuffer();
    const fileReader = new FileReader();

    chunks.forEach((chunk,index) => {
      if(index===0||index===chunks.length-1){
        //1.第一个和最后一个切片全部参与计算
        targets.push(chunk);
      }else{
        //2.中间的切片只计算前面两个字节、中间两个字节、最后两个字节
        targets.push(chunk.slice(0,2))
        targets.push(chunk.slice(CHUNK_SIZE/2,CHUNK_SIZE/2+2))
        targets.push(chunk.slice(CHUNK_SIZE-2,CHUNK_SIZE))
      }
    })
    fileReader.readAsArrayBuffer(new Blob(targets))
    fileReader.onload = (e) => {
      spark.append(e.target.result)
      // console.log('hash:'+spark.end())
      resolve(spark.end());
    }
  })
}



const fetchCategories = async () => {
  try {
    const result = await getVideoAllCategories()
    categories.value = result.data
  } catch (error) {
    console.error('获取分类失败', error)
    categories.value = [
      {id: 1, name: '动画'},
      {id: 2, name: '音乐'},
      {id: 3, name: '游戏'},
      {id: 4, name: '生活'},
      {id: 5, name: '科技'},
      {id: 6, name: '娱乐'}
    ]
  }
}

const handleBlock = (e) => {
  e.preventDefault()
  e.stopPropagation()
  if (e.type === "dragenter" || e.type === "dragover") {
    dragActive.value = true
  } else if (e.type === "dragleave") {
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
  await fetchCategories()

  // 立即开始上传
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
    formData.value = { ...formData.value, ...result.data }

    clearInterval(progressInterval)
    uploadProgress.value = 100
    processingSteps.value.upload = 'completed'

    // 后台自动开始转码和生成缩略图
    processingSteps.value.thumbnail = 'progress'
    setTimeout(() => {
      processingSteps.value.thumbnail = 'completed'
      processingSteps.value.transcode = 'progress'

      setTimeout(() => {
        processingSteps.value.transcode = 'completed'
        uploading.value = false
        console.log('视频处理完成', result)
      }, 1500)
    }, 1500)
  } catch (error) {
    clearInterval(progressInterval)
    uploading.value = false
    console.error('上传失败', error)
    alert('视频上传失败，请重试')
    selectedFile.value = null
    uploadProgress.value = 0
    processingSteps.value = {
      upload: 'pending',
      thumbnail: 'pending',
      transcode: 'pending'
    }
  }
}

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
  if (!formData.value.category) {
    alert('请选择视频分类')
    return
  }

  publishing.value = true

  try {
    const publishData = {
      id: formData.value.id,
      description: formData.value.description,
      category: formData.value.category,
      title: formData.value.title,
      filePath: formData.value.filePath,
      tagsNames: formData.value.tagsNames,
      createTime: formData.value.createTime
    }

    await postVideo(publishData)

    // 显示发布成功
    publishSuccess.value = true

    // 2秒后跳转到视频列表
    setTimeout(() => {
      router.push('/my-videos')
    }, 2000)
  } catch (error) {
    console.error('发布失败', error)
    alert('视频发布失败，请重试')
    publishing.value = false
  }
}

const handleCancel = () => {
  if (selectedFile.value) {
    if (!confirm('确定要取消上传吗？')) {
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
    id: null,
    title: '',
    description: '',
    category: null,
    filePath: '',
    tagsNames: [],
    createTime: null
  }
}

const getStepStatus = (step) => {
  return processingSteps.value[step]
}

const fileSizeMB = computed(() => {
  return selectedFile.value ? (selectedFile.value.size / (1024 * 1024)).toFixed(2) : 0
})

const remainingTime = computed(() => {
  const remaining = 100 - uploadProgress.value
  return Math.ceil(remaining / 10)
})

const addTag = () => {
  const trimmed = inputValue.value.trim()
  if (!trimmed) return

  if (formData.value.tagsNames.length >= maxTags) {
    alert(`最多只能添加 ${maxTags} 个标签`)
    return
  }
  if (formData.value.tagsNames.includes(trimmed)) {
    alert('标签已存在')
    return
  }
  if (trimmed.length > 20) {
    alert('标签长度不能超过 20 个字符')
    return
  }

  formData.value.tagsNames.push(trimmed)
  inputValue.value = ''
}

const removeTag = (index) => {
  formData.value.tagsNames.splice(index, 1)
}

onMounted(async () => {
  await fetchCategories()
})
</script>

<template>
  <!-- 发布成功遮罩 -->
  <div v-if="publishSuccess" class="fixed inset-0 bg-black/80 backdrop-blur-sm z-50 flex items-center justify-center">
    <div class="text-center">
      <div class="w-24 h-24 mx-auto mb-6 rounded-full bg-gradient-to-br from-green-500 to-emerald-500 flex items-center justify-center animate-bounce">
        <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round">
          <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
          <polyline points="22 4 12 14.01 9 11.01"></polyline>
        </svg>
      </div>
      <h2 class="text-3xl font-bold mb-3">发布成功！</h2>
      <p class="text-gray-400 text-lg">正在跳转到我的视频...</p>
    </div>
  </div>

  <div class="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900/20 to-slate-900">
    <!-- Header -->
    <header class="bg-white/5 backdrop-blur-md sticky top-0 z-40 border-b border-white/10">
      <div class="max-w-7xl mx-auto px-6 py-4">
        <div class="flex items-center justify-between">
          <div class="flex items-center gap-8">
            <h1 class="text-2xl font-bold bg-gradient-to-r from-primary to-secondary bg-clip-text text-transparent">AxoSpark</h1>
            <nav class="flex items-center gap-6">
              <a href="/" class="text-gray-400 hover:text-white transition">首页</a>
              <a href="/creation" class="text-white font-medium">创作中心</a>
              <a href="/my-videos" class="text-gray-400 hover:text-white transition">我的视频</a>
            </nav>
          </div>
        </div>
      </div>
    </header>

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
                  'border-2 border-dashed rounded-xl p-12 text-center transition-all cursor-pointer',
                  dragActive ? 'border-purple-500 bg-purple-500/10' : 'border-gray-600 hover:border-gray-500'
                ]"
                @dragenter="handleBlock"
                @dragleave="handleBlock"
                @dragover="handleBlock"
                @drop="handleDrop"
                @click="!selectedFile && $refs.fileInput.click()"
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
                <button class="px-8 py-3 bg-gradient-to-r from-primary to-secondary rounded-lg font-medium hover:opacity-90 transition">
                  选择文件
                </button>
              </div>
              <div v-else class="flex items-center gap-4" @click.stop>
                <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-purple-500">
                  <rect x="2" y="2" width="20" height="20" rx="2.18" ry="2.18"></rect>
                  <line x1="7" y1="2" x2="7" y2="22"></line>
                  <line x1="17" y1="2" x2="17" y2="22"></line>
                  <line x1="2" y1="12" x2="22" y2="12"></line>
                </svg>
                <div class="flex-1 text-left">
                  <h3 class="font-medium mb-1">{{ selectedFile.name }}</h3>
                  <p class="text-sm text-gray-400">{{ fileSizeMB }} MB</p>
                </div>
                <button @click.stop="handleCancel" class="p-2 hover:bg-white/10 rounded-lg transition">
                  <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <line x1="18" y1="6" x2="6" y2="18"></line>
                    <line x1="6" y1="6" x2="18" y2="18"></line>
                  </svg>
                </button>
              </div>
            </div>
            <input ref="fileInput" type="file" accept="video/mp4,video/quicktime,video/x-msvideo" @change="handleFileInput" class="hidden" />
          </div>

          <!-- 上传进度卡片 -->
          <div v-if="selectedFile && (uploading || uploadCompleted)" class="bg-white/5 backdrop-blur-sm rounded-2xl p-8 border border-white/10">
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
                <h3 class="font-medium mb-1">{{ selectedFile.name }}</h3>
                <p class="text-sm text-gray-400">{{ fileSizeMB }} MB</p>
              </div>
            </div>

            <div class="space-y-4">
              <div v-if="uploadProgress < 100">
                <div class="flex justify-between text-sm mb-2">
                  <span>上传进度</span>
                  <span class="text-purple-400">{{ uploadProgress }}%</span>
                </div>
                <div class="h-2 bg-slate-800 rounded-full overflow-hidden">
                  <div class="h-full bg-gradient-to-r from-purple-500 to-pink-500 rounded-full transition-all duration-300" :style="{ width: uploadProgress + '%' }"></div>
                </div>
                <p class="text-xs text-gray-500 mt-2">剩余时间: 约 {{ remainingTime }} 分钟</p>
              </div>

              <div class="pt-4 border-t border-white/10 space-y-2">
                <div class="flex items-center justify-between text-sm">
                  <span class="flex items-center gap-2">
                    <svg v-if="getStepStatus('upload') === 'completed'" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-green-500">
                      <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                      <polyline points="22 4 12 14.01 9 11.01"></polyline>
                    </svg>
                    <svg v-else-if="getStepStatus('upload') === 'progress'" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-purple-500 animate-spin">
                      <line x1="12" y1="2" x2="12" y2="6"></line>
                      <line x1="12" y1="18" x2="12" y2="22"></line>
                      <line x1="4.93" y1="4.93" x2="7.76" y2="7.76"></line>
                      <line x1="16.24" y1="16.24" x2="19.07" y2="19.07"></line>
                      <line x1="2" y1="12" x2="6" y2="12"></line>
                      <line x1="18" y1="12" x2="22" y2="12"></line>
                    </svg>
                    <svg v-else xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-gray-500">
                      <circle cx="12" cy="12" r="10"></circle>
                    </svg>
                    视频上传
                  </span>
                  <span :class="getStepStatus('upload') === 'completed' ? 'text-green-500' : getStepStatus('upload') === 'progress' ? 'text-purple-500' : 'text-gray-500'">
                    {{ getStepStatus('upload') === 'completed' ? '完成' : getStepStatus('upload') === 'progress' ? '进行中' : '等待中' }}
                  </span>
                </div>
                <div class="flex items-center justify-between text-sm">
                  <span class="flex items-center gap-2">
                    <svg v-if="getStepStatus('thumbnail') === 'completed'" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-green-500">
                      <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                      <polyline points="22 4 12 14.01 9 11.01"></polyline>
                    </svg>
                    <svg v-else-if="getStepStatus('thumbnail') === 'progress'" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-purple-500 animate-spin">
                      <line x1="12" y1="2" x2="12" y2="6"></line>
                      <line x1="12" y1="18" x2="12" y2="22"></line>
                      <line x1="4.93" y1="4.93" x2="7.76" y2="7.76"></line>
                    </svg>
                    <svg v-else xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-gray-500">
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
                    <svg v-if="getStepStatus('transcode') === 'completed'" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-green-500">
                      <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                      <polyline points="22 4 12 14.01 9 11.01"></polyline>
                    </svg>
                    <svg v-else-if="getStepStatus('transcode') === 'progress'" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-purple-500 animate-spin">
                      <line x1="12" y1="2" x2="12" y2="6"></line>
                      <line x1="12" y1="18" x2="12" y2="22"></line>
                      <line x1="4.93" y1="4.93" x2="7.76" y2="7.76"></line>
                    </svg>
                    <svg v-else xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-gray-500">
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
                <input type="text" v-model="formData.title" placeholder="给你的视频起个标题" class="w-full px-4 py-3 bg-slate-800/50 border border-gray-600 rounded-lg focus:outline-none focus:border-purple-500 transition" />
              </div>
              <div>
                <label class="block text-sm font-medium mb-2">简介</label>
                <textarea v-model="formData.description" placeholder="介绍一下你的视频内容" rows="4" class="w-full px-4 py-3 bg-slate-800/50 border border-gray-600 rounded-lg focus:outline-none focus:border-purple-500 transition"></textarea>
              </div>
              <div>
                <label class="block text-sm font-medium mb-2">分类 *</label>
                <select v-model="formData.category" class="w-full px-4 py-3 bg-slate-800/50 border border-gray-600 rounded-lg focus:outline-none focus:border-purple-500 transition">
                  <option :value="null" disabled>请选择分类</option>
                  <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium mb-2">标签</label>
                <div class="flex flex-wrap gap-2 p-3 bg-slate-800/50 border border-gray-600 rounded-lg">
                  <span v-for="(tag, index) in formData.tagsNames" :key="index" class="inline-flex items-center gap-1 px-3 py-1 bg-gradient-to-r from-purple-500/20 to-pink-500/20 rounded-full text-sm">
                    {{ tag }}
                    <X class="w-4 h-4 cursor-pointer hover:text-red-400" @click="removeTag(index)" />
                  </span>
                  <input type="text" v-model="inputValue" @keydown.enter.prevent="addTag" @blur="addTag" placeholder="添加标签..." class="flex-1 bg-transparent border-none outline-none min-w-[120px]" />
                </div>
                <p class="text-xs text-gray-500 mt-1">按回车添加标签，最多 {{ maxTags }} 个</p>
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
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="mt-0.5 flex-shrink-0 text-green-500">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                  <polyline points="22 4 12 14.01 9 11.01"></polyline>
                </svg>
                <span>支持 MP4, MOV, AVI 格式</span>
              </li>
              <li class="flex items-start gap-2">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="mt-0.5 flex-shrink-0 text-green-500">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                  <polyline points="22 4 12 14.01 9 11.01"></polyline>
                </svg>
                <span>文件大小不超过 2GB</span>
              </li>
              <li class="flex items-start gap-2">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="mt-0.5 flex-shrink-0 text-green-500">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                  <polyline points="22 4 12 14.01 9 11.01"></polyline>
                </svg>
                <span>视频上传时可同时填写信息</span>
              </li>
              <li class="flex items-start gap-2">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="mt-0.5 flex-shrink-0 text-green-500">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                  <polyline points="22 4 12 14.01 9 11.01"></polyline>
                </svg>
                <span>转码和审核在后台自动进行</span>
              </li>
            </ul>
          </div>

          <div v-if="selectedFile" class="bg-white/5 backdrop-blur-sm rounded-2xl p-6 border border-white/10">
            <div class="flex items-start gap-3 mb-4">
              <div class="w-10 h-10 rounded-full bg-gradient-to-br from-primary to-secondary flex items-center justify-center flex-shrink-0">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <circle cx="12" cy="12" r="10"></circle>
                  <line x1="12" y1="16" x2="12" y2="12"></line>
                  <line x1="12" y1="8" x2="12.01" y2="8"></line>
                </svg>
              </div>
              <div>
                <h4 class="font-medium mb-2">温馨提示</h4>
                <ul class="text-sm text-gray-400 space-y-1">
                  <li>• 视频正在后台处理中</li>
                  <li>• 填写完信息即可发布</li>
                  <li>• 发布后会继续转码和审核</li>
                </ul>
              </div>
            </div>
          </div>

          <button
              v-if="selectedFile"
              @click="handleSubmit"
              :disabled="!formData.title.trim() || !formData.category || publishing"
              class="w-full py-4 bg-gradient-to-r from-primary to-secondary rounded-lg font-bold text-lg hover:opacity-90 transition disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <span v-if="publishing" class="flex items-center justify-center gap-2">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="animate-spin">
                <line x1="12" y1="2" x2="12" y2="6"></line>
                <line x1="12" y1="18" x2="12" y2="22"></line>
                <line x1="4.93" y1="4.93" x2="7.76" y2="7.76"></line>
                <line x1="16.24" y1="16.24" x2="19.07" y2="19.07"></line>
                <line x1="2" y1="12" x2="6" y2="12"></line>
                <line x1="18" y1="12" x2="22" y2="12"></line>
              </svg>
              发布中...
            </span>
            <span v-else>{{ uploadCompleted ? '立即发布' : '处理中，请稍候...' }}</span>
          </button>

          <p v-if="selectedFile && !formData.title.trim()" class="text-sm text-amber-400 text-center">
            请填写视频标题
          </p>
          <p v-if="selectedFile && formData.title.trim() && !formData.category" class="text-sm text-amber-400 text-center">
            请选择视频分类
          </p>
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
* {
  scrollbar-width: thin;
  scrollbar-color: rgba(139, 92, 246, 0.3) transparent;
}

*::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

*::-webkit-scrollbar-track {
  background: transparent;
}

*::-webkit-scrollbar-thumb {
  background: rgba(139, 92, 246, 0.3);
  border-radius: 3px;
}

*::-webkit-scrollbar-thumb:hover {
  background: rgba(139, 92, 246, 0.5);
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.animate-bounce {
  animation: bounce 1s ease-in-out infinite;
}
</style>