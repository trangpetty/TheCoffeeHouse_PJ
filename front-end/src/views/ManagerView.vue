<template>
  <div>
    <el-row>
      <el-col :span="24" class="header">
        <div class="d-flex box-shadow justify-content-between align-items-center px-4">
          <div>
            <img :src="Logo" style="width: 80px;" />
          </div>
          <div>
            <el-dropdown trigger="click" ref="dropdown">
              <div>
                <img :src="user.avatar || noAvatar" style="width: 40px; height: 40px;" class="rounded-circle object-fit-cover me-2" />
                <span>{{ user.firstName }} {{ user.lastName }}</span>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="dialogVisible = true">
                    <div class="d-flex align-items-center">
                      <font-awesome-icon style="width: var(--space-24);" class="fs-5 me-2" icon="fa-solid fa-pen-to-square" />
                      <span>Đổi mật khẩu</span>
                    </div>
                  </el-dropdown-item>
                  <el-dropdown-item @click="logout">
                    <div class="d-flex align-items-center">
                      <font-awesome-icon style="width: var(--space-24);" class="fs-5 me-2" icon="fa-solid fa-arrow-right-from-bracket" />
                      <span>Thoát</span>
                    </div>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-col>
      <el-col :span="24" class="d-flex">
        <Sidebar class="h-100 box-shadow" style="width: 240px;" />
        <div class="p-3 w-100">
          <h2>{{ $route.name }}</h2>
          <router-view></router-view>
        </div>
      </el-col>
    </el-row>

    <el-dialog v-model="dialogVisible" width="40%" class="dialog" title="Đổi mật khẩu">
      <el-form :model="editForm" label-width="160px" label-position="right">
        <el-form-item label="Mật khẩu cũ" prop="oldPassword">
          <el-input type="password" v-model="editForm.oldPassword" />
        </el-form-item>
        <el-form-item label="Mật khẩu mới" prop="newPassword">
          <el-input type="password" v-model="editForm.newPassword" />
        </el-form-item>
        <el-form-item label="Nhập lại mật khẩu mới" prop="newPasswordAgain">
          <el-input type="password" v-model="editForm.newPasswordAgain" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Hủy</el-button>
        <el-button type="primary" @click="changePassword">Xác nhận</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import Sidebar from '@/components/manager/layout/Sidebar.vue';
import Logo from '@/assets/images/logo.png';
import noAvatar from '@/assets/images/user2.png';
import axiosClient from '@/utils/axiosConfig';
import { computed, ref } from 'vue';
import { ElMessage } from 'element-plus';
import store from "@/store";
import router from "@/router";

const dialogVisible = ref(false);
const user = computed(() => store.getters.user);

const editForm = ref({
  oldPassword: '',
  newPassword: '',
  newPasswordAgain: ''
});

const validatePasswordAgain = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('Vui lòng nhập lại mật khẩu mới'));
  } else if (value !== editForm.value.newPassword) {
    callback(new Error('Mật khẩu mới không khớp'));
  } else if (value === editForm.value.oldPassword) {
    callback(new Error('Mật khẩu mới không thể trùng với mật khẩu cũ'));
  } else {
    callback();
  }
};

const editFormRules = {
  oldPassword: [
    { required: true, message: 'Vui lòng nhập mật khẩu cũ', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: 'Vui lòng nhập mật khẩu mới', trigger: 'blur' }
  ],
  newPasswordAgain: [
    { required: true, message: 'Vui lòng nhập lại mật khẩu mới', trigger: 'blur' },
    { validator: validatePasswordAgain, trigger: 'blur' }
  ]
};


const changePassword = async () => {
  try {
    const token = sessionStorage.getItem('token');
    const response = await axiosClient.post('/auth/change-password', {
      oldPassword: editForm.value.oldPassword,
      newPassword: editForm.value.newPassword
    }, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });

    const newToken = response.data.token;
    if (newToken) {
      localStorage.setItem('token', newToken);
    }
    ElMessage.success('Đổi mật khẩu thành công!');
    dialogVisible.value = false;
  } catch (error) {
    ElMessage.error('Đổi mật khẩu thất bại. Vui lòng thử lại.');
  }
};

const logout = () => {
  store.dispatch('logout');
  router.push('/admin/login');
};
</script>
