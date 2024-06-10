<template>
    <NavBar />
    <AddressDialog />
    <VoucherDialog />
    <router-view></router-view>
</template>

<script lang="ts" setup>
import NavBar from "@/views/user/NavBar.vue";
import AddressDialog from '@/views/user/AddressDialog.vue';
import VoucherDialog from '@/views/user/VoucherDialog.vue';
import {onMounted} from 'vue';
import { useStore } from 'vuex';
const store = useStore();

onMounted(() => {
  store.dispatch('loadCart');
  store.dispatch('loadAddress');
  store.dispatch('loadVoucher');
  store.dispatch('loadErrorMessageVoucher');
});

</script>
