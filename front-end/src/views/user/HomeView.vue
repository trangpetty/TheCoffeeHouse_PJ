<template>
    <NavBar />
    <AddressDialog />
    <VoucherDialog />
    <router-view></router-view>
    <Footer />
</template>

<script lang="ts" setup>
import NavBar from "@/views/user/NavBar.vue";
import AddressDialog from '@/views/user/AddressDialog.vue';
import VoucherDialog from '@/views/user/VoucherDialog.vue';
import Footer from "@/views/user/Footer.vue";
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
});
// const fetchUserInformation = async () => {
//   try {
//     const response = await axios.get('http://localhost:8082/api/users/login/google');
//     console.log(response)
//     // const user = response.data as User;
//     //
//     // // Store user information in localStorage
//     // localStorage.setItem('user', JSON.stringify(user));
//     //
//     // // Redirect back to previous page after successful login
//     // window.history.back();
//   } catch (error) {
//     console.error('Error fetching user information:', error);
//   }
// };
//
// // Fetch user information immediately after component creation
// fetchUserInformation();

</script>
