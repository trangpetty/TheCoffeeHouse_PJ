<template>
    <NavBar />
    <AddressDialog />
    <VoucherDialog />
    <router-view></router-view>
    <Footer />
</template>

<script lang="ts" setup>
import NavBar from "@/components/order/layout/NavBar.vue";
import AddressDialog from '@/components/order/dialog/AddressDialog.vue';
import VoucherDialog from '@/components/order/dialog/VoucherDialog.vue';
import Footer from "@/components/order/layout/Footer.vue";
import {onMounted} from 'vue';
import { useStore } from 'vuex';

const store = useStore();

onMounted(() => {
  store.dispatch('loadCart');
  store.dispatch('loadAddress');
  store.dispatch('loadVoucher');
  store.dispatch('loadErrorMessageVoucher');
  store.dispatch('loadUser');
  // const token = sessionStorage.getItem('token');
  // const user = JSON.parse(sessionStorage.getItem('user') || '{}');
  // const refreshToken = sessionStorage.getItem('refreshToken');
  //
  // if (token && user) {
  //   store.dispatch('login', { token, refreshToken, user });
  // }

});
// const fetchUserInformation = async () => {
//   try {
//     const response = await axiosClient.get('/users/login/google');
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
