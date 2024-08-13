<template>
  <div class="container py-5" v-loading="ui.loading">
    <el-form @submit.prevent="login" class="form-login box-shadow p-4" :rules="loginRules" ref="loginForm">
      <el-form-item class="border-orange" prop="email">
        <font-awesome-icon icon="fa-solid fa-envelope" class="icon"/>
        <el-input v-model="formLogin.email" type="email" placeholder="Email" class="input-login" />
      </el-form-item>
      <el-form-item class="border-orange" prop="password">
        <font-awesome-icon icon="fa-solid fa-lock" class="icon"/>
        <el-input
            v-model="formLogin.password"
            :type="passwordType"
            placeholder="Password"
            class="input-login">
          <template #append>
            <font-awesome-icon
                :icon="passwordType !== 'password' ? 'fa-solid fa-eye' : 'fa-solid fa-eye-slash'"
                class="password-toggle"
                @click="togglePasswordVisibility"/>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <button class="btn-add-item fs-6">Login</button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useStore } from 'vuex';
import router from "@/router";
import axiosClient from "@/utils/axiosConfig";
import { ElMessage } from "element-plus";

const store = useStore();
const loginForm = ref(null)

const formLogin = ref({
  email: '',
  password: ''
});

const ui = ref({
  loading: false
});

const loginRules = {
  email: [
    { required: true, message: 'Email is required', trigger: 'blur' },
    { type: 'email', message: 'Invalid email address', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Password is required', trigger: 'blur' },
  ]
};

// State for password visibility
const passwordType = ref('password');

// Toggle password visibility
const togglePasswordVisibility = () => {
  passwordType.value = passwordType.value === 'password' ? 'text' : 'password';
};

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
        router.push('/admin/dashboard');
      }, 2000);
    }
  } catch (error) {
    ElMessage.error("Invalid email or password.");
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
  background-color: #f5f5f5;
}

.password-toggle {
  cursor: pointer;
}

::v-deep .el-input-group__append {
  background-color: transparent!important;
  color: var(--orange-1)!important;
  box-shadow: none!important;
}
</style>
