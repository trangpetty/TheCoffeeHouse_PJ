<template>
  <div class="banner_home">
    <el-carousel :interval="3000" arrow="always" indicator-position="outside" height="560px">
      <el-carousel-item v-for="(item, index) in images" :key="index">
        <img :src="item" class="d-block w-100" alt="...">
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script lang="ts" setup>
import banner1 from '@/assets/images/banner1.webp'
import banner2 from '@/assets/images/banner2.webp'
import banner3 from '@/assets/images/banner3.webp'
import banner4 from '@/assets/images/banner-combo.jpg'
import banner5 from '@/assets/images/banner-newest.jpg'
import {ref} from "vue";

const images = ref([banner1, banner2, banner3, banner4, banner5])
</script>

<style scoped>
.banner_home {
  transform: translateY(-60px);
  position: relative;
}
</style>