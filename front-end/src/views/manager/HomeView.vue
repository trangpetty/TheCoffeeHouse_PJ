<template>
  <div>
    <div class="d-flex">
      <div class="box-shadow" style="width: 240px;">
          <img :src="Logo" alt="" class="w-100">
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
import Logo from '@/assets/images/logo.png'

</script>

<style>

</style>
