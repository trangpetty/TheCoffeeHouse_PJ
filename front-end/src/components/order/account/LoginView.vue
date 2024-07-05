<template>
  <div class="container form-login-container box box-shadow my-5 rounded">
    <img :src="Logo" alt="" class="img-logo">
    <el-form @submit.prevent="login" class="form-login">
      <el-form-item class="border-orange">
        <font-awesome-icon icon="fa-solid fa-envelope" class="icon"/>
        <el-input v-model="formData.email" type="email" placeholder="Email" class="input-login" required />
      </el-form-item>
      <el-form-item class="border-orange">
        <font-awesome-icon icon="fa-solid fa-lock" class="icon"/>
        <el-input v-model="formData.password" type="password" placeholder="Password" class="input-login" required />
      </el-form-item>
      <el-form-item>
        <button class="btn-add-item fs-6">Login</button>
      </el-form-item>
    </el-form>
    <p v-if="loginError" class="error-message">{{ loginError }}</p>
    <div class="text-center">
      <p class="mb-2">Đăng nhập với</p>
      <div class="d-flex justify-content-between align-items-center">
        <button class="btn btn-light border-0 rounded d-flex align-items-center">
          <img :src="google" style="width:2rem" class="me-2">
          <span>Sign in with Google</span>
        </button>
        <span>Or</span>
        <button class="btn btn-light border-0 rounded d-flex align-items-center">
          <img :src="facebook" style="width:2rem" class="me-2">
          <span>Sign in with Facebook</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useStore } from 'vuex';
import axios from 'axios';
import google from '@/assets/images/google.png'
import facebook from '@/assets/images/facebook.png'
import Logo from '@/assets/images/logo.png'

const formData = ref({
  email: '',
  password: ''
});
const store = useStore();
const loginError = ref('');

const login = async () => {
  try {
    const response = await axios.post('http://localhost:8082/api/auth/signin', formData.value);
    const result = response.data;

    // Save order information and tokens to Vuex store
    store.dispatch('login', { token: result.token, refreshToken: result.refreshToken, user: result });

    // Redirect to home page or handle success message
    console.log('Login successful:', result);
  } catch (error) {
    if (error.response) {
      loginError.value = error.response.data;
    } else {
      loginError.value = error.message;
    }
    console.error('Error during account:', loginError.value);
  }
};
</script>

<style scoped>
.box {
  height: 100%;
}

.form-login-container {
  width: 560px;
  padding: 2rem;
  text-align: center;
}

.form-login {
  width: 400px;
  margin: 0 auto;
}

.img-logo {
  width: 200px;
}

.error-message {
  color: red;
  margin-top: 10px;
}

::v-deep .input-login .el-input__wrapper {
  box-shadow: none!important;
  border-radius: 0!important;
}

::v-deep .input-login .el-input__inner::placeholder,
.icon {
  color: var(--orange-1);
}

::v-deep .input-login .el-input__inner {
  font-size: 1rem!important;
}

::v-deep .el-input {
  width: calc(100% - 30px);
}

.border-orange {
  border-bottom: 1px solid var(--orange-1);
}

.icon {
  width: 30px;
  font-size: 1.25rem;
}

.btn-light {
  padding: 0.5rem 1.5rem 0.5rem 0.75rem;
;
  background-color: #f5f5f5;
}
</style>
