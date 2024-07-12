<template>
  <Navbar />
  <router-view></router-view>
  <Footer />
</template>

<script setup lang="ts">
import Navbar from '@/components/user/layout/Navbar.vue';
import Footer from '@/components/order/layout/Footer.vue';
</script>

