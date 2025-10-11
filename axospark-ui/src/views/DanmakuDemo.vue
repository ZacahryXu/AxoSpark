<template>
  <div class="danmaku-demo">
    <div class="container">
      <!-- 视频播放区域 -->
      <div class="video-area" ref="videoArea">
        <div class="video-placeholder">🎬 弹幕演示区域</div>
        <!-- 弹幕层 -->
        <div class="danmaku-container" ref="danmakuContainer"></div>
      </div>

      <!-- 控制面板 -->
      <div class="control-panel">
        <div class="status">
          <div class="status-dot" :class="{ connected: isConnected }"></div>
          <span>{{ isConnected ? '已连接' : '未连接' }}</span>
        </div>

        <div class="input-area">
          <input 
            type="text" 
            class="danmaku-input" 
            v-model="danmakuInput" 
            placeholder="输入弹幕内容..." 
            @keypress="handleKeyPress"
          />
          
          <button 
            class="send-btn" 
            :disabled="!isConnected" 
            @click="sendDanmaku"
          >
            发送
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

// 简化的响应式数据
const isConnected = ref(false)
const danmakuInput = ref('')
const danmakuContainer = ref(null)

// WebSocket连接
let websocket = null

// 连接WebSocket
const connect = () => {
  try {
    // 使用您现有的WebSocket端点
    websocket = new WebSocket('ws://localhost:8080/myHandler')
    
    websocket.onopen = () => {
      isConnected.value = true
      console.log('WebSocket连接成功')
    }
    
    websocket.onmessage = (event) => {
      // 接收弹幕消息
      const data = JSON.parse(event.data)
      if (data.type === 'danmaku') {
        displayDanmaku(data.content)
      }
    }
    
    websocket.onclose = () => {
      isConnected.value = false
      console.log('WebSocket连接关闭')
    }
    
    websocket.onerror = (error) => {
      console.error('WebSocket错误:', error)
      isConnected.value = false
    }
  } catch (error) {
    console.error('连接失败:', error)
    isConnected.value = false
  }
}

// 发送弹幕
const sendDanmaku = () => {
  const content = danmakuInput.value.trim()
  if (!content || !isConnected.value) return

  // 发送弹幕消息
  const message = {
    type: 'danmaku',
    content: content,
    timestamp: Date.now()
  }
  
  websocket.send(JSON.stringify(message))
  
  // 立即显示自己的弹幕
  displayDanmaku(content)
  
  // 清空输入框
  danmakuInput.value = ''
}

// 显示弹幕
const displayDanmaku = (content) => {
  if (!danmakuContainer.value) return

  const danmakuEl = document.createElement('div')
  danmakuEl.className = 'danmaku-item'
  danmakuEl.textContent = content
  danmakuEl.style.color = '#FFFFFF'

  const containerHeight = danmakuContainer.value.offsetHeight
  const containerWidth = danmakuContainer.value.offsetWidth

  // 随机Y轴位置（分6条轨道）
  const tracks = 6
  const trackHeight = containerHeight / tracks
  const randomTrack = Math.floor(Math.random() * tracks)
  danmakuEl.style.top = (randomTrack * trackHeight) + 'px'

  // 起始位置在右侧外面
  danmakuEl.style.left = containerWidth + 'px'

  danmakuContainer.value.appendChild(danmakuEl)

  // 计算动画时间
  const duration = 8000 // 8秒滚动时间

  // 应用动画
  danmakuEl.style.animation = `scroll-danmaku ${duration}ms linear`

  // 动画结束后移除元素
  setTimeout(() => {
    if (danmakuEl.parentNode) {
      danmakuEl.remove()
    }
  }, duration)
}

// 处理按键事件
const handleKeyPress = (e) => {
  if (e.key === 'Enter') {
    sendDanmaku()
  }
}

// 生命周期
onMounted(() => {
  connect()
})

onUnmounted(() => {
  if (websocket) {
    websocket.close()
  }
})
</script>

<style scoped>
.danmaku-demo {
  height: 100vh;
  overflow: hidden;
}

.container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  font-family: 'Microsoft YaHei', Arial, sans-serif;
  background: #0a0a0a;
  color: #fff;
}

/* 视频播放区域 */
.video-area {
  position: relative;
  flex: 1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.video-placeholder {
  font-size: 24px;
  opacity: 0.7;
}

/* 弹幕容器 */
.danmaku-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  overflow: hidden;
}

.danmaku-item {
  position: absolute;
  white-space: nowrap;
  font-size: 22px;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.8);
  will-change: transform;
}

/* 滚动弹幕动画 */
@keyframes scroll-danmaku {
  from {
    transform: translateX(0);
  }
  to {
    transform: translateX(-100%);
  }
}

/* 简化的控制面板 */
.control-panel {
  background: #1a1a1a;
  padding: 15px;
  border-top: 2px solid #333;
}

.status {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
  font-size: 14px;
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #ff4444;
}

.status-dot.connected {
  background: #00ff00;
}

.input-area {
  display: flex;
  gap: 10px;
  align-items: center;
}

.danmaku-input {
  flex: 1;
  padding: 10px 15px;
  background: #2a2a2a;
  border: 2px solid #444;
  border-radius: 20px;
  color: #fff;
  font-size: 14px;
}

.danmaku-input:focus {
  outline: none;
  border-color: #00a1d6;
}

.send-btn {
  padding: 10px 20px;
  background: #00a1d6;
  border: none;
  border-radius: 20px;
  color: #fff;
  font-size: 14px;
  cursor: pointer;
}

.send-btn:disabled {
  background: #444;
  cursor: not-allowed;
}
</style>
