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
          v-for="(tab, index) in tabs"
          :key="index"
          @click="changeTab(index)"
          :index="tab.path"
      >
        <span>{{ tab.label }}</span>
      </el-menu-item>
    </el-menu>
  </aside>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const tabs = ref([
  { label: 'Product', path: '/product' },
  { label: 'Voucher', path: '/voucher' },
  // Add more tabs here
]);

const router = useRouter();
// const route = useRoute();

const changeTab = (index: number) => {
  router.push(tabs.value[index].path);
};
</script>

<style scoped>
/* Component-specific styles */
</style>
