<template>
  <div class="container form-login-container box box-shadow my-5 rounded" v-loading="ui.loading">
    <img :src="Logo" alt="" class="img-logo">

    <div v-if="activeTab === 'login'">
      <el-form :model="formLogin" :rules="loginRules" ref="loginForm" @submit.prevent="login" class="form-login">
        <el-form-item prop="email" class="border-orange">
          <font-awesome-icon icon="fa-solid fa-envelope" class="icon"/>
          <el-input v-model="formLogin.email" type="email" placeholder="Email" class="input-login" />
        </el-form-item>
        <el-form-item prop="password" class="border-orange">
          <font-awesome-icon icon="fa-solid fa-lock" class="icon"/>
          <el-input v-model="formLogin.password" type="password" placeholder="Password" class="input-login" />
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
    <el-form :model="formRegister" :rules="registerRules" ref="registerForm" @submit.prevent="register" v-else>
      <el-form-item class="row mb-3">
        <div class="w-50">
          <el-form-item prop="firstName">
            <el-input v-model="formRegister.firstName" placeholder="First Name" class="input-login border-orange" />
          </el-form-item>
        </div>
        <div class="w-50">
          <el-form-item prop="lastName">
            <el-input v-model="formRegister.lastName" placeholder="Last Name" class="input-login border-orange" />
          </el-form-item>
        </div>
      </el-form-item>
      <el-form-item prop="phoneNumber">
        <el-input v-model="formRegister.phoneNumber" placeholder="Phone Number" class="input-login border-orange" />
      </el-form-item>
      <el-form-item prop="email">
        <el-input v-model="formRegister.email" type="email" placeholder="Email" class="input-login border-orange" />
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="formRegister.password" type="password" placeholder="Password" class="input-login border-orange" />
      </el-form-item>
      <el-form-item prop="gender">
        <el-radio-group v-model="formRegister.gender">
          <el-radio v-for="(item, index) in gender" :key="index" :value="item.value" size="mini">{{item.label}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="dob">
        <label class="text-orange me-2 fs-6">Date of birth</label>
        <el-date-picker
            v-model="formRegister.dob"
            type="date"
            placeholder="DD/MM/YYYY"
            format="DD/MM/YYYY"
            value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <p>Đã có tài khoản? <a class="text-orange cursor-pointer" @click="switchTab('login')">Đăng nhập</a></p>
      <el-form-item>
        <button class="btn-add-item fs-6">Đăng ký</button>
      </el-form-item>
    </el-form>

    <!-- Error message display -->
    <p v-if="loginError" class="error-message">{{ loginError }}</p>
  </div>
</template>

<script setup lang="ts">
import {ref} from 'vue';
import { ElMessage } from 'element-plus';
import { useStore } from 'vuex';
import google from '@/assets/images/google.png';
import facebook from '@/assets/images/facebook.png';
import Logo from '@/assets/images/logo.png';
import router from "@/router";
import axiosClient from '@/utils/axiosConfig';

const activeTab = ref('login');

const loginForm = ref();
const registerForm = ref();

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
  gender: 0,
  dob: null,
  code: ''
});

const ui = ref({
  loading: false,
});

const store = useStore();
const loginError = ref('');

const gender = ref([
  {label: "Nữ", value: 0},
  {label: "Nam", value: 1}
]);

const switchTab = (tab: string) => {
  activeTab.value = tab;
};

function generateRandomString(): string {
  const letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
  const numbers = '0123456789';

  // Generate the first character (a letter)
  const firstChar = letters[Math.floor(Math.random() * letters.length)];

  // Generate the remaining nine characters (numbers)
  const remainingChars = Array.from({ length: 9 }, () => numbers[Math.floor(Math.random() * numbers.length)]).join('');

  // Combine the first character and the remaining characters
  return firstChar + remainingChars;
}

const login = async () => {
  try {
    await loginForm.value.validate();
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
    if (error.response && error.response.status === 401) {
      ElMessage.error('Invalid email or password.');
    } else {
      ElMessage.error('An error occurred during login.');
    }
  }
};

const register = async () => {
  try {
    await registerForm.value.validate();
    formRegister.value.code = generateRandomString();
    const response = await axiosClient.post('/auth/signup', formRegister.value);
    const result = response.data;

    store.dispatch('login', { token: result.token, refreshToken: result.refreshToken, user: result });

    if (response.status === 200) {
      ui.value.loading = true;
      ElMessage.success('Register successfully!');
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
    console.error('Error during account registration:', loginError.value);
  }
};

const loginRules = {
  email: [
    { required: true, message: 'Email is required', trigger: 'blur' },
    { type: 'email', message: 'Invalid email address', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Password is required', trigger: 'blur' },
  ]
};

const registerRules = {
  firstName: [
    { required: true, message: 'First name is required', trigger: 'blur' }
  ],
  lastName: [
    { required: true, message: 'Last name is required', trigger: 'blur' }
  ],
  phoneNumber: [
    { required: true, message: 'Phone number is required', trigger: 'blur' },
    { pattern: /^\d{10}$/, message: 'Phone number must be 10 digits', trigger: 'blur' }
  ],
  email: [
    { required: true, message: 'Email is required', trigger: 'blur' },
    { type: 'email', message: 'Invalid email address', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Password is required', trigger: 'blur' },
  ],
  dob: [
    { required: true, message: 'Date of birth is required', trigger: 'change' }
  ]
};
</script>

<style scoped>
.el-form-item__required::after {
  content: none;
}

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
