<template>
  <div class="container text-center mt-5">
    <el-card>
      <h1 class="display-1">404</h1>
      <p class="lead">Xin lỗi, trang bạn đang tìm kiếm không tồn tại.</p>
      <button class="btn btn--orange-1" @click="goHome">Quay về trang chủ</button>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'NotFoundView',
  methods: {
    goHome() {
      this.$router.push('/order');
    }
  }
}
</script>

<style scoped>
.container {
  max-width: 600px;
}
</style>
