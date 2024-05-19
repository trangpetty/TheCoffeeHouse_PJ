<template>
  <div>
    <div class="d-flex">
      <div class="box-shadow">
          <img src="../../assets/images/logo.jpg" alt="" class="img-logo">
          <Sidebar class="h-100"/>
      </div>
      <div class="p-3 w-100">
          <h2>{{ $route.name }}</h2>
          <router-view></router-view>

      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import Sidebar from '@/views/manager/Sidebar.vue';

</script>

<style>
.img-logo {
  width: 200px;
  padding: 0 30px;
}
</style>
