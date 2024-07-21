<template>
  <div class="container text-center mt-5">
    <el-card>
      <h1 class="fs">Thanh toán thành công!</h1>
      <p class="lead">Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.</p>
      <button class="btn btn--orange-1" @click="goHome">Quay về trang chủ</button>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
const goHome = () => {
  this.$router.push('/order');
}
</script>
