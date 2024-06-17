<template>
  <div>
    <h1>Register</h1>
    <form @submit.prevent="register">
      <input v-model="name" type="text" placeholder="Name" required />
      <input v-model="email" type="email" placeholder="Email" required />
      <input v-model="password" type="password" placeholder="Password" required />
      <input v-model="phoneNumber" type="text" placeholder="Phone Number" required />
      <button type="submit">Register</button>
    </form>
  </div>
</template>

<script>
import axiosClient from '@/utils/axiosClient';

export default {
  data() {
    return {
      name: '',
      email: '',
      password: '',
      phoneNumber: '',
    };
  },
  methods: {
    async register() {
      try {
        const response = await axiosClient.post('http://localhost:8082/users/register', {
          name: this.name,
          email: this.email,
          password: this.password,
          phoneNumber: this.phoneNumber,
        });
        localStorage.setItem('accessToken', response.data.token);
        localStorage.setItem('refreshToken', response.data.user.refreshToken);
        this.$router.push('/');
      } catch (error) {
        console.error('Error registering:', error);
      }
    },
  },
};
</script>
