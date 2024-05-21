<template>
  <aside>
    <el-menu
        active-text-color="#ffd04b"
        background-color="#545c64"
        class="el-menu-vertical-demo h-100"
        :default-active="$route.path"
        text-color="#fff"
    >
      <el-menu-item
          v-for="(route, index) in routes[0].children"
          :key="index"
          @click="changeTab(route.path)"
          :index="route.path"
      >
        <span>{{ route.name }}</span>
      </el-menu-item>
    </el-menu>
  </aside>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { routes } from '@/router'; // Importing routes from routes.ts

const router = useRouter();

const changeTab = (path: string) => {
  router.push(path);
};
</script>

<style scoped>
/* Component-specific styles */
</style>
