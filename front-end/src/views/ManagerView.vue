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

    <!-- Reset password dialog component -->
    <ResetPasswordDialog :visible="dialogVisible" @update:visible="dialogVisible = $event" />
  </div>
</template>

<script lang="ts" setup>
import Sidebar from '@/components/manager/layout/Sidebar.vue';
import Logo from '@/assets/images/logo.png';
import noAvatar from '@/assets/images/user2.png';
import axiosClient from '@/utils/axiosConfig';
import { computed, ref } from 'vue';
import ResetPasswordDialog from '@/components/order/dialog/ResetPasswordDialog.vue';
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

const rules = {
  oldPassword: [{ required: true, message: 'Mật khẩu cũ là bắt buộc', trigger: 'blur' }],
  newPassword: [
    { required: true, message: 'Mật khẩu mới là bắt buộc', trigger: 'blur' },
    { min: 6, message: 'Mật khẩu mới phải có ít nhất 6 ký tự', trigger: 'blur' }
  ],
  newPasswordAgain: [
    { required: true, message: 'Nhập lại mật khẩu mới là bắt buộc', trigger: 'blur' },
    { validator: (rule, value, callback) => {
        if (value !== editForm.value.newPassword) {
          callback(new Error('Mật khẩu xác nhận không khớp'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ]
};

const formRef = ref(null);

const changePassword = async () => {
  if (editForm.value.newPassword !== editForm.value.newPasswordAgain) {
    ElMessage.error('Mật khẩu mới và xác nhận mật khẩu không khớp. Vui lòng kiểm tra lại.');
    return;
  }

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
    ui.value.dialogVisible = false;
    editForm.value.oldPassword = '';
    editForm.value.newPassword = '';
    editForm.value.newPasswordAgain = '';
  } catch (error) {
    ElMessage.error('Đổi mật khẩu thất bại. Vui lòng thử lại.');
  }
};

const logout = () => {
  store.dispatch('logout');
  router.push('/admin/login');
};
</script>
