<template>
  <div class="container text-center my-5">
    <el-card>
      <h1 class="fs-1">Thanh toán thất bại</h1>
      <p class="lead">Có lỗi xảy ra trong quá trình thanh toán. Vui lòng thử lại.</p>
      <button class="btn btn--orange-1" @click="goHome">Quay về trang chủ</button>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const goHome = () => {
  router.push('/order');
}
</script>

<style scoped>
/* Style riêng cho trang này nếu cần */
</style>
