<template>
  <aside>
    <el-menu
        active-text-color="#ffd04b"
        background-color="#545c64"
        class="el-menu-vertical-demo h-100"
        :default-active="$route.path"
        text-color="#fff"
    >
      <template v-for="(route, index) in filteredRoutes" :key="index">
        <el-sub-menu v-if="route.children && route.children.length" :index="route.path">
          <template #title>
            <span>{{ route.name }}</span>
          </template>
          <template v-for="(child, childIndex) in route.children" :key="childIndex">
            <el-menu-item
                :index="`${child.path}`"
                @click="changeTab(`${child.path}`)"
            >
              <span>{{ child.name }}</span>
            </el-menu-item>
          </template>
        </el-sub-menu>
        <el-menu-item v-else @click="changeTab(route.path)" :index="route.path">
          <span>{{ route.name }}</span>
        </el-menu-item>
      </template>
    </el-menu>
  </aside>
</template>

<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router';

const router = useRouter();
const route = useRoute();

const changeTab = (path: string) => {
  router.push(path);
};

const filteredRoutes = route.matched[0].children.filter(route => route.path);
</script>

<style scoped>
.el-menu-item.is-active {
  background-color: #3e4a54 !important; /* Your desired active background color */
}
</style>
