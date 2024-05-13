<template>
  <aside>
    <el-menu
        active-text-color="#ffd04b"
        background-color="#545c64"
        class="el-menu-vertical-demo h-100"
        default-active="1"
        text-color="#fff"
    >
      <el-menu-item v-for="(tab, index) in tabs" :key="index" @click="changeTab(index)" :index="(index + 1).toString()">
        <span>{{ tab.label }}</span>
      </el-menu-item>
    </el-menu>
  </aside>
</template>

<script lang="ts" setup>
import { ref, defineEmits } from 'vue';

// eslint-disable-next-line vue/valid-define-emits
const emit = defineEmits();
const tabs = ref([
  { label: 'Product', component: 'Product'},
  { label: 'Voucher', component: 'Voucher'},
  // Add more tabs here
]);

const activeTabIndex = ref(0);

const changeTab = (index: number) => {
  activeTabIndex.value = index;
  // eslint-disable-next-line no-undef
  emit('changeTab', tabs.value[index].component);
};
</script>

<style scoped>
/* Component-specific styles */
</style>
