<template>
  <h1 class="row user-information">
    Thông tin tài khoản
  </h1>
  <form class="mt-4">
    <div class="avatar-upload-container d-flex flex-column justify-content-center align-items-center py-3 mb-3" v-loading="ui.loading">
      <!-- Avatar display -->
      <el-avatar :size="100" :src="formData.avatar" class="avatar mb-2" />
      <!-- Upload button -->
      <el-upload
          class="avatar-upload"
          action="#"
          :auto-upload="false"
          :on-change="handleUploadChange"
          :show-file-list="false"
      >
        <el-button size="small" type="primary">Edit</el-button>
      </el-upload>
    </div>
    <div class="form-group row">
      <div class="col-6 d-flex flex-column align-items-start p-0 pe-2">
        <label>Tên khách hàng</label>
        <input type="text" name="" id="" class="form-control" v-model="formData.firstName">
      </div>
      <div class="col-6 d-flex flex-column align-items-start p-0 ps-2">
        <label class="text-white">.</label>
        <input type="text" name="" id="" class="form-control" v-model="formData.lastName">
      </div>
    </div>
    <div class="form-group row">
      <label class="p-0 mb-1">Số điện thoại</label>
      <input type="text" :value="user.phoneNumber" class="form-control" disabled>
    </div>
    <div class="form-group row" v-if="user.dob">
      <label class="p-0 mb-1">Sinh nhật (Bạn không thể thay đổi sau khi đã lựa chọn)</label>
      <input type="text" :value="user.dob" class="form-control" disabled>
    </div>
    <div v-else class="form-group row">
      <label class="p-0 mb-1">Sinh nhật</label>
      <el-date-picker
          v-model="formData.dob"
          type="date"
          placeholder="DD/MM/YYYY"
          format="DD/MM/YYYY"
          value-format="YYYY-MM-DD"
          class="w-100 p-0"
      />
    </div>
    <div class="form-group row">
      <label class="p-0 mb-1">Email</label>
      <input type="text" :value="user.email" class="form-control" disabled>
    </div>
    <div class="row d-flex justify-content-start">
      <div class="radio-input me-3">
        <el-radio type="radio" class="custom-control-input me-2" v-model="user.gender" :value="1">
          <label for="">Nam</label>
        </el-radio>
      </div>
      <div class="radio-input">
        <el-radio type="radio" class="custom-control-input me-2" v-model="user.gender" :value="0">
          <label for="">Nu</label>
        </el-radio>
      </div>
    </div>
    <div class="row d-flex justify-content-end mt-3">
      <button class="btn-update btn btn--orange-1 w-auto px-4 py-2 me-3" @click="handleResetPassword">Đổi mật khẩu</button>
      <button class="btn-update btn btn--orange-1 w-auto px-4 py-2" @click="handleUpdate">Cập nhật</button>
    </div>
  </form>

  <!-- Reset password dialog component -->
  <ResetPasswordDialog :visible="ui.dialogVisible" @update:visible="ui.dialogVisible = $event" />
</template>

<script lang="ts" setup>
import { computed, onMounted, ref } from 'vue';
import { useStore } from 'vuex';
import axiosClient from '@/utils/axiosConfig';
import noAvatar from '@/assets/images/no-avatar.png';
import ResetPasswordDialog from '@/components/order/dialog/ResetPasswordDialog.vue';
import { uploadFileToFirebaseAndGetURL } from '@/utils';

const store = useStore();

const ui = ref({
  loading: false,
  dialogVisible: false
})

const user = computed(() => store.getters.user);

const formData = ref({
  firstName: '',
  lastName: '',
  dob: null,
  avatar: ''
});

onMounted(() => {
  formData.value.firstName = user.value.firstName;
  formData.value.lastName = user.value.lastName;
  formData.value.avatar = user.value.avatar || noAvatar;
})

const handleUpdate = async (event) => {
  event.preventDefault(); // Ngăn hành vi gửi form mặc định
  try {
    await axiosClient.put(`/users/${user.value.id}`, formData.value);
    await loadUserData();
  } catch (error) {
    console.error('Update failed:', error);
  }
};

const loadUserData = async () => {
  try {
    const response = await axiosClient.get(`/users/${user.value.id}`);
    store.commit('updateUser', response.data);
  } catch (error) {
    console.error('Failed to load user data:', error);
  }
};

const handleUploadChange = async (file) => {
  const url = await uploadFileToFirebaseAndGetURL(file.file);
  formData.value.avatar = url;
};

const handleResetPassword = () => {
  ui.value.dialogVisible = true;
};
</script>

<style scoped>

.user-information {
  position: relative;
  text-align: left;
}

.user-information:before {
  border-bottom: 2px solid #fa8c16;
  bottom: -4px;
  content: "";
  left: 0;
  position: absolute;
  width: 30%;
}

.form-group {
  margin-bottom: 1rem;
}

.radio-input {
  margin: var(--space-6) var(--space-22);
  padding: 0 var(--space-16);
  cursor: pointer;
  width: auto;
  -webkit-print-color-adjust: exact;
  z-index: 1;
  min-height: 1.5rem;
  display: flex;
  align-items: center;
}

.custom-control-input {
  position: static;
  box-sizing: border-box;
  padding: 0 var(--space-16);
  height: var(--space-24);
  width: var(--space-24);
  z-index: -1;
}

.custom-control-input:checked
{
  background: var(--orange-2)!important;
  border-color: var(--orange-2)!important;
}

::v-deep .el-radio__inner {
  width: 1rem!important;
  height: 1rem!important;
}

::v-deep .el-radio__label {
  font-size: 1rem!important;

}

.btn-update {
  border-radius: var(--space-100);
  color: var(--white);
  text-align: center;
}

.avatar-upload-container {
  border-bottom: 1px solid var(--orange-1);
}

.avatar-upload {
  display: inline-block;
}

.avatar {
  cursor: pointer; /* Makes the avatar look clickable */
}

::v-deep .el-button--primary {
  background-color: var(--orange-1)!important;
  border-color: var(--orange-1)!important;
}

</style>