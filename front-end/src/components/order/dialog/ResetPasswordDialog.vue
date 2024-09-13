<template>
  <el-dialog v-model="props.visible" width="40%" class="dialog" title="Đổi mật khẩu">
    <el-form :model="form" :rules="rules" ref="formRef" label-width="200px" label-position="right">
      <el-form-item label="Mật khẩu cũ" prop="oldPassword">
        <el-input type="password" v-model="form.oldPassword" />
      </el-form-item>
      <el-form-item label="Mật khẩu mới" prop="newPassword">
        <el-input type="password" v-model="form.newPassword" />
      </el-form-item>
      <el-form-item label="Nhập lại mật khẩu mới" prop="newPasswordAgain">
        <el-input type="password" v-model="form.newPasswordAgain" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">Hủy</el-button>
      <el-button type="primary" @click="changePassword">Xác nhận</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, defineProps, defineEmits } from 'vue';
import { ElMessage } from 'element-plus';
import axiosClient from '@/utils/axiosConfig';

const props = defineProps<{
  visible: boolean;
}>();

const emit = defineEmits<{
  (event: 'update:visible', value: boolean): void;
}>();

const form = ref({
  oldPassword: '',
  newPassword: '',
  newPasswordAgain: ''
});

const rules = {
  oldPassword: [{ required: true, message: 'Mật khẩu cũ là bắt buộc', trigger: 'blur' }],
  newPassword: [
    { required: true, message: 'Mật khẩu mới là bắt buộc', trigger: 'blur' },
  ],
  newPasswordAgain: [
    { required: true, message: 'Nhập lại mật khẩu mới là bắt buộc', trigger: 'blur' },
    { validator: (rule, value, callback) => {
        if (value !== form.value.newPassword) {
          callback(new Error('Mật khẩu xác nhận không khớp'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ]
};

const close = () => {
  emit('update:visible', false);
};

const changePassword = async () => {
  if (form.value.newPassword !== form.value.newPasswordAgain) {
    ElMessage.error('Mật khẩu mới và xác nhận mật khẩu không khớp. Vui lòng kiểm tra lại.');
    return;
  }

  try {
    const token = sessionStorage.getItem('token');
    const response = await axiosClient.post('/auth/change-password', {
      oldPassword: form.value.oldPassword,
      newPassword: form.value.newPassword
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
    close();
    form.value.oldPassword = '';
    form.value.newPassword = '';
    form.value.newPasswordAgain = '';
  } catch (error) {
    ElMessage.error('Đổi mật khẩu thất bại. Vui lòng thử lại.');
  }
};
</script>

<style scoped>
.dialog {
  /* Add any custom styles for the dialog here */
}
</style>
