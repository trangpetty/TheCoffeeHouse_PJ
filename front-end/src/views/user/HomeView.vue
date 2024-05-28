<template>
    <NavBar />
    <router-view></router-view>
</template>

<script lang="ts" setup>
import NavBar from "@/views/user/NavBar.vue";
import { onMounted } from 'vue';
import { useStore } from 'vuex';;
const store = useStore();

onMounted(() => {
  store.dispatch('loadCart');
});

</script>

<style>

</style>
