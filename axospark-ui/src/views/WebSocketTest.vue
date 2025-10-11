<script setup>
import { onMounted, onBeforeUnmount, ref } from 'vue'

const wsUrl = `${import.meta.env.VITE_WS_BASE_URL || 'ws://localhost:8080'}/myHandler`
const socket = ref(null)
const connected = ref(false)
const logs = ref([])
const inputMsg = ref('')

function appendLog(type, payload) {
  const time = new Date().toLocaleTimeString()
  logs.value.push(`[${time}] ${type}: ${payload}`)
}

function connect() {
  if (socket.value && (socket.value.readyState === WebSocket.OPEN || socket.value.readyState === WebSocket.CONNECTING)) {
    return
  }
  const ws = new WebSocket(wsUrl)
  socket.value = ws

  ws.onopen = () => {
    connected.value = true
    appendLog('OPEN', '已连接')
  }
  ws.onmessage = (evt) => {
    appendLog('MESSAGE', evt.data)
  }
  ws.onerror = (err) => {
    appendLog('ERROR', JSON.stringify(err))
  }
  ws.onclose = () => {
    connected.value = false
    appendLog('CLOSE', '连接已关闭')
  }
}

function disconnect() {
  if (socket.value) {
    socket.value.close()
    socket.value = null
  }
}

function sendMessage() {
  if (socket.value && socket.value.readyState === WebSocket.OPEN) {
    socket.value.send(inputMsg.value || 'hello')
    appendLog('SEND', inputMsg.value || 'hello')
    inputMsg.value = ''
  } else {
    appendLog('WARN', '未连接 WebSocket')
  }
}

onMounted(() => {
  connect()
})

onBeforeUnmount(() => {
  disconnect()
})
</script>

<template>

  <div style="padding:16px; max-width: 800px; margin: 0 auto;">
    <h2>WebSocket 测试</h2>
    <p>URL: {{ wsUrl }}</p>
    <div style="margin-bottom: 12px;">
      <button @click="connect" :disabled="connected">连接</button>
      <button @click="disconnect" :disabled="!connected" style="margin-left:8px;">断开</button>
    </div>
    <div style="display:flex; gap: 8px; margin-bottom: 12px;">
      <input v-model="inputMsg" placeholder="输入要发送的消息" style="flex:1;" />
      <button @click="sendMessage">发送</button>
    </div>
    <div style="border:1px solid #ddd; padding:8px; height:240px; overflow:auto; background:#fafafa;">
      <div v-for="(l, idx) in logs" :key="idx">{{ l }}</div>
    </div>
  </div>

</template>

<style scoped>
</style>


