<template>
  <div>
    <div class="box box-shadow mb-3">
      <el-form label-width="auto" :model="queryForm">
        <el-row>
          <el-col :span="8">
            <el-form-item label="Name">
              <el-input
                  v-model="queryForm.name"
                  class="input"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Phone Number">
              <el-input
                  v-model="queryForm.phoneNumber"
                  class="input"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" @click="fetchData">Search</el-button>
          <el-button type="primary" @click="resetForm">Reset</el-button>
          <el-button type="primary" @click="handleAdd">
            <i class="el-icon-circle-plus-outline"/><span>Add</span>
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="box box-shadow">
      <el-table :data="tableData" stripe v-loading="ui.loading" @row-click="handleEditRow" highlight-current-row>
        <el-table-column type="index" label="#"/>.
        <el-table-column label="Name" >
          <template #default={row}>
            {{row.firstName}} {{row.lastName}}
          </template>
        </el-table-column>
        <el-table-column prop="role" label="Role" />
        <el-table-column prop="phoneNumber" label="phoneNumber" />
        <el-table-column label="Avatar">
          <template #default={row}>
            <el-image :src="row.avatar" class="w-50"/>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="Create Time" />
        <el-table-column prop="modifyTime" label="Modify Time" />
        <el-table-column>
          <template #default={row}>
            <el-button type="success" size="small" @click.stop="handleEditRow(row)">Update</el-button>
            <el-button v-if="row.removable" type="danger" size="small" @click.stop="handleDeleteRow(row.id)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog v-model="ui.dialogVisible" width="40%" class="dialog" v-loading="ui.loading" :title="ui.addRecord?'Add order':'Update order'">
        <el-form :model="formData" label-width="auto">
          <el-input v-model="user_id" class="d-none" />
          <el-form-item label="First Name">
            <el-input v-model="formData.firstName" />
          </el-form-item>
          <el-form-item label="Last Name">
            <el-input v-model="formData.lastName" />
          </el-form-item>
          <el-form-item label="Password" v-if="ui.addRecord">
            <el-input v-model="formData.password" />
          </el-form-item>
          <el-form-item label="Email">
            <el-input v-model="formData.email" />
          </el-form-item>
          <el-form-item label="Phone Number">
            <el-input v-model="formData.phoneNumber" />
          </el-form-item>
          <el-form-item label="Gender">
            <el-select v-model="formData.gender">
              <el-option label="Male" :value="1"/>
              <el-option label="Female" :value="0"/>
            </el-select>
          </el-form-item>
          <el-form-item label="Avatar">
            <el-upload action="#" list-type="picture-card" :auto-upload="false" :file-list="fileList" :on-change="handleUploadChange" :limit="1">
              <template v-if="fileList.length < 1" #trigger>
                <el-icon><Plus /></el-icon>
              </template>
              <template #file="{ file }">
                <div>
                  <img class="el-upload-list__item-thumbnail" :src="file.url" alt="" />
                  <span class="el-upload-list__item-actions">
                    <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
                      <el-icon><ZoomIn /></el-icon>
                    </span>
                    <span class="el-upload-list__item-delete" @click="openFileInput(file)">
                      <el-icon><Edit /></el-icon>
                    </span>
                  </span>
                </div>
              </template>
            </el-upload>

            <el-dialog v-model="dialogVisible">
              <img :src="dialogImageUrl" alt="" width="100%" />
            </el-dialog>
          </el-form-item>
        </el-form>
        <div class="text-end">
          <el-button @click="ui.dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleConfirm">Confirm</el-button>
        </div>
      </el-dialog>
      <!-- Pagination -->
      <el-pagination class="pagination" v-model:current-page="currentPage" layout="total, prev, pager, next" :total="total" @current-change="fetchData" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import {  ref } from 'vue';
import axios from 'axios';
import { Edit, Plus, ZoomIn} from "@element-plus/icons-vue";
import {uploadFileToFirebaseAndGetURL} from "@/utils";
import * as Utils from '@/utils';

const dialogImageUrl = ref('');
const dialogVisible = ref(false);
const fileList = ref([]);

const ui = ref({
  dialogVisible: false,
  loading: false,
  addRecord: false,
  userRole: false,
  adminRole: false,
  nameDisable: false,
});

const queryForm = ref({
  name: '',
  phoneNumber: ''
});

const formData = ref({
  firstName: '',
  lastName: '',
  password: '',
  email: '',
  phoneNumber: '',
  avatar: '',
  gender: 0,
  roleName: 'USER'
})

const currentPage = ref(1);
const total = ref(0);
const tableData = ref([]);
const user_id = ref(0);

const fetchData = async () => {
  try {
    ui.value.loading = true;
    let response: object
    response = await axios.get('http://localhost:8082/api/users', {
      params: {
        name: queryForm.value.name,
        phoneNumber: queryForm.value.phoneNumber,
        pageNo: currentPage.value
      }
    });
    tableData.value = response.data.content;
    total.value = response.data.totalElements;
    ui.value.loading = false;
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};

const handleUploadChange = (file, newFileList) => {
  fileList.value = newFileList.map(f => ({
    ...f,
    uid: f.uid || Date.now() // Ensure each file has a unique identifier
  }));
  console.log(fileList.value)
}

const handlePictureCardPreview = (file) => {
  dialogImageUrl.value = file.url;
  dialogVisible.value = true;
};

const handleConfirm = async () => {
  ui.value.dialogVisible = false;
  ui.value.loading = true;
  if (ui.value.addRecord) {
    for (const file of fileList.value) {
      try {
        formData.value.avatar = await uploadFileToFirebaseAndGetURL(file.raw, 'users');
      } catch (error) {
        console.error('Error uploading file:', error);
      }
    }
    console.log(formData.value)
    await axios.post('http://localhost:8082/api/users', formData.value);
  } else {
    await axios.put(`http://localhost:8082/api/users/${user_id.value}`, formData.value);
  }
  ui.value.loading = false;
  await fetchData();
};

const resetForm = () => {
  queryForm.value.name = '';
  queryForm.value.status = null;
  queryForm.value.applyFrom = null;
  queryForm.value.applyTo = null;
  currentPage.value = 1;
  fetchData();
}

const handleAdd = () => {
  ui.value.addRecord = true;
  ui.value.dialogVisible = true;
  formData.value.firstName = '';
  formData.value.lastName = '';
  formData.value.email = '';
  formData.value.phoneNumber = '';
  formData.value.roleName = 'USER';
  formData.value.gender = 0;
  formData.value.avatar = '';
  fileList.value = [];
}

const handleEditRow = (row: object) => {
  ui.value.dialogVisible = true;
  ui.value.addRecord = false;
  user_id.value = row.id
  formData.value.firstName = row.firstName;
  formData.value.lastName = row.lastName;
  formData.value.email = row.email;
  formData.value.phoneNumber = row.phoneNumber;
  formData.value.roleName = row.roleName;
  formData.value.avatar = row.avatar;
  formData.value.gender = row.gender;
  fileList.value = [{
    url: formData.value.avatar
  }]
}

const handleDeleteRow = async (id: number) => {
  let confirmed = await Utils.confirm("This action will delete this record! Confirm to delete？", "Confirm");
  if(!confirmed) {
    return;
  }
  await axios.delete(`http://localhost:8082/api/users/${id}`);
  await fetchData();
}

const openFileInput = (file) => {
  const input = document.createElement('input');
  input.type = 'file';
  input.accept = 'image/*';
  input.style.display = 'none';
  input.addEventListener('change', (event) => {
    handleFileInputChange(event, file);
  });
  document.body.appendChild(input);
  input.click();
  document.body.removeChild(input);
};

const handleFileInputChange = async (event, fileToEdit) => {
  const input = event.target;
  if (input.files && input.files.length > 0) {
    const newFile = input.files[0];
    const downloadURL = await uploadFileToFirebaseAndGetURL(newFile, 'users');
    formData.value.avatar = downloadURL;
    const fileIndex = fileList.value.findIndex(f => f.uid === fileToEdit.uid);
    if (fileIndex !== -1) {
      fileList.value[fileIndex] = { ...fileToEdit, raw: newFile, url: downloadURL };
    }
  }
};

fetchData()

</script>

<style scoped>
/* Component-specific styles */
</style>
