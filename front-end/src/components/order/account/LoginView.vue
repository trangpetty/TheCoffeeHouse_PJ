<template>
  <div class="container form-login-container box box-shadow my-5 rounded" v-loading="ui.loading">
    <img :src="Logo" alt="" class="img-logo">
    <!-- Other login form elements -->
    <div v-if="activeTab === 'login'">
      <el-form @submit.prevent="login" class="form-login">
        <el-form-item class="border-orange">
          <font-awesome-icon icon="fa-solid fa-envelope" class="icon"/>
          <el-input v-model="formLogin.email" type="email" placeholder="Email" class="input-login" required />
        </el-form-item>
        <el-form-item class="border-orange">
          <font-awesome-icon icon="fa-solid fa-lock" class="icon"/>
          <el-input v-model="formLogin.password" type="password" placeholder="Password" class="input-login" required />
        </el-form-item>
        <p>Đã có tài khoản? <a class="text-orange cursor-pointer" @click="switchTab('register')">Đăng ký</a></p>
        <el-form-item>
          <button class="btn-add-item fs-6">Login</button>
        </el-form-item>
      </el-form>

      <!-- Social login buttons -->
      <div class="text-center">
        <p class="mb-2">Đăng nhập với</p>
        <div class="d-flex justify-content-between align-items-center">
          <button class="btn btn-light border-0 rounded d-flex align-items-center justify-content-center w-100">
            <img :src="google" style="width:2rem" class="me-2">
            <span>Google</span>
          </button>
          <span class="mx-3">Or</span>
          <button class="btn btn-light border-0 rounded d-flex align-items-center justify-content-center w-100">
            <img :src="facebook" style="width:2rem" class="me-2">
            <span>Facebook</span>
          </button>
        </div>
      </div>
    </div>

    <!-- Register form -->
    <form @submit.prevent="register" v-else>
      <el-form-item class="row mb-3">
        <div class="w-50">
          <el-form-item>
            <el-input v-model="formRegister.firstName" placeholder="First Name" class="input-login border-orange" required />
          </el-form-item>
        </div>
        <div class="w-50">
          <el-form-item>
            <el-input v-model="formRegister.lastName" placeholder="Last Name" class="input-login border-orange" required />
          </el-form-item>
        </div>
      </el-form-item>
      <el-form-item>
        <el-input v-model="formRegister.phoneNumber" placeholder="Phone Number" class="input-login border-orange" required />
        <p v-if="ui.isPhoneNumberValid" class="text-danger text-start m-0"><font-awesome-icon icon="fa-solid fa-circle-exclamation" /> Số điện thoại phải là 10 số.</p>
      </el-form-item>
      <el-form-item>
        <el-input v-model="formRegister.email" type="email" placeholder="Email" class="input-login border-orange" required />
        <p v-if="ui.isEmailValid" class="text-danger text-start m-0"><font-awesome-icon icon="fa-solid fa-circle-exclamation" /> Email không hợp lệ.</p>
      </el-form-item>
      <el-form-item class="mb-3">
        <el-input v-model="formRegister.password" type="password" placeholder="Password" class="input-login border-orange" required />
      </el-form-item>
      <el-form-item class="mb-3">
        <el-radio-group v-model="formRegister.gender">
          <el-radio v-for="(item, index) in gender" :key="index" :value="item.value" size="mini">{{item.label}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <p>Đã có tài khoản? <a class="text-orange cursor-pointer" @click="switchTab('login')">Đăng nhập</a></p>
      <button class="btn-add-item fs-6">Đăng ký</button>
    </form>

    <!-- Error message display -->
    <p v-if="loginError" class="error-message">{{ loginError }}</p>
  </div>
</template>

<script setup lang="ts">
import {ref, watch} from 'vue';
import { ElMessage } from 'element-plus';
import { useStore } from 'vuex';
import google from '@/assets/images/google.png';
import facebook from '@/assets/images/facebook.png';
import Logo from '@/assets/images/logo.png';
import router from "@/router";
import axiosClient from '@/utils/axiosConfig';

const activeTab = ref('login');
const formLogin = ref({
  email: '',
  password: ''
});

const formRegister = ref({
  firstName: '',
  lastName: '',
  email: '',
  password: '',
  phoneNumber: '',
  gender: 0
});

const ui = ref({
  loading: false,
  isPhoneNumberValid: true,
  isEmailValid: true,
});
const store = useStore();
const loginError = ref('');

const gender = ref([
  {label: "Nữ", value: 0},
  {label: "Nam", value: 1}
])

const switchTab = (tab: string) => {
  activeTab.value = tab;
};

const login = async () => {
  try {
    const response = await axiosClient.post('/auth/signin', formLogin.value);
    const result = response.data;

    store.dispatch('login', { token: result.token, refreshToken: result.refreshToken, user: result });

    if (response.status === 200) {
      ui.value.loading = true;
      ElMessage.success('Login successfully!');
      setTimeout(() => {
        router.push('/order');
      }, 2000);
    }
  } catch (error) {
    if (error.response) {
      loginError.value = error.response.data;
    } else {
      loginError.value = error.message;
    }
    console.error('Error during account login:', loginError.value);
  }
};

const register = async () => {
  try {
    if (this.isPhoneNumberValid && this.isEmailValid) {
      const response = await axiosClient.post('/auth/signup', formRegister.value);
      const result = response.data;

      store.dispatch('login', { token: result.token, refreshToken: result.refreshToken, user: result });

      if (response.status === 200) {
        ui.value.loading = true;
        ElMessage.success('Login successfully!');
        setTimeout(() => {
          router.push('/order');
        }, 2000);
      }
    }
    else return;
  } catch (error) {
    if (error.response) {
      loginError.value = error.response.data;
    } else {
      loginError.value = error.message;
    }
    console.error('Error during account login:', loginError.value);
  }
};

const validatePhoneNumber = () => {
  const phoneNumberRegex = /^\d{10}$/;
  ui.value.isPhoneNumberValid = phoneNumberRegex.test(formRegister.value.phoneNumber);
}
const validateEmail = () => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  ui.value.isEmailValid = emailRegex.test(formRegister.value.email);
}

watch(() => {
    validatePhoneNumber();
    validateEmail();
})
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
