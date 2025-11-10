<template>
  <header class="glass rounded-2xl px-6 py-4 mb-6">
    <div class="flex items-center justify-between">
      <div class="flex items-center gap-8">
        <AppLogo size="lg" />

        <nav class="flex gap-6">
          <a
              v-for="item in navItems"
              :key="item.id"
              href="#"
              :class="item.active ? 'text-primary font-medium' : 'text-gray-400 hover:text-white transition'"
          >
            {{ item.name }}
          </a>
        </nav>
      </div>

      <div class="flex items-center gap-4">
        <SearchBar />

        <button class="glass rounded-xl p-2 hover:bg-dark-700 transition">
          <Bell :size="20" />
        </button>

        <button class="glass rounded-xl px-4 py-2 flex items-center gap-2 hover:bg-dark-700 transition cursor-pointer"
                @click="goUpload">
          <Upload :size="20" />
          <span class="text-sm ">投稿</span>
        </button>

        <UserAvatar size="lg" />
      </div>
    </div>
  </header>
</template>

<script setup>
import { useRouter } from "vue-router";
const router = useRouter();
import { Bell, Upload } from 'lucide-vue-next'
import AppLogo from '../common/AppLogo.vue'
import SearchBar from '../common/SearchBar.vue'
import UserAvatar from '../common/UserAvatar.vue'

const navItems = [
  { id: 'home', name: '首页', active: true },
  { id: 'anime', name: '动画', active: false },
  { id: 'bangumi', name: '番剧', active: false },
  { id: 'live', name: '直播', active: false }
]
const goUpload = () => {
  //路由到creation页面
  router.push({ name: 'creation' });
}
</script>

