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
          v-for="(route, index) in filteredRoutes"
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
import { useRouter, useRoute } from 'vue-router';
import { routes } from '@/router'; // Importing routes from routes.ts

const router = useRouter();
const route = useRoute();

const changeTab = (path: string) => {
  router.push(path);
};

// Filter out the "Product Type" route
const filteredRoutes = routes[0].children.filter(route => route.path !== '/product-type');
</script>

<style scoped>
.el-menu-item.is-active {
  background-color: #3e4a54 !important; /* Your desired active background color */
}
</style>
