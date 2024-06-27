<template>
    <NavBar />
    <AddressDialog />
    <VoucherDialog />
    <router-view></router-view>
    <Footer />
</template>

<script lang="ts" setup>
import NavBar from "@/views/order/layout/NavBar.vue";
import AddressDialog from '@/views/order/dialog/AddressDialog.vue';
import VoucherDialog from '@/views/order/dialog/VoucherDialog.vue';
import Footer from "@/views/order/layout/Footer.vue";
import {onMounted} from 'vue';
import { useStore } from 'vuex';
import axios from 'axios';

interface User {
  name: string;
  email: string;
  token: string;
}

const store = useStore();

onMounted(() => {
  store.dispatch('loadCart');
  store.dispatch('loadAddress');
  store.dispatch('loadVoucher');
  store.dispatch('loadErrorMessageVoucher');
  store.dispatch('loadUser');
});
// const fetchUserInformation = async () => {
//   try {
//     const response = await axios.get('http://localhost:8082/api/users/login/google');
//     console.log(response)
//     // const order = response.data as User;
//     //
//     // // Store order information in localStorage
//     // localStorage.setItem('order', JSON.stringify(order));
//     //
//     // // Redirect back to previous page after successful account
//     // window.history.back();
//   } catch (error) {
//     console.error('Error fetching order information:', error);
//   }
// };
//
// // Fetch order information immediately after component creation
// fetchUserInformation();

</script>
