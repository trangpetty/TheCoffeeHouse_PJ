<template>
  <div class="box box-shadow">
    <el-button type="primary" @click="handleAdd">
      <font-awesome-icon icon="fa-solid fa-circle-plus" />
      Add
    </el-button>
  </div>
  <div class="box box-shadow">
    <!-- Table -->
    <el-table :data="tableData" stripe v-loading="ui.loading">
      <el-table-column type="index" label="#" />
      <el-table-column prop="name" label="Name" />
      <el-table-column label="Image" width="200">
        <template #default={row}>
          <el-image :src="row.image" class="w-75"/>
        </template>
      </el-table-column>
      <el-table-column label="Action">
        <template #default="{ row }">
          <el-button type="success" size="small" @click="handleEditRow(row)">Update</el-button>
          <el-button type="danger" size="small" @click="handleDeleteRow(row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- Dialog -->

    <el-dialog v-model="ui.dialogVisible" width="40%" class="dialog" :title="ui.addRecord?'Add product':'Update product'">
      <el-form :model="formData" label-width="auto">
        <el-input v-model="product_id" class="d-none" />
        <el-form-item label="Name">
          <el-input v-model="formData.name" />
        </el-form-item>
        <el-form-item label="Image">
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
  </div>
</template>
<script setup lang="ts">
import {Delete, Edit, Plus, ZoomIn} from "@element-plus/icons-vue";
import {ref} from "vue";
import axiosClient from '@/utils/axiosConfig';
import {uploadFileToFirebaseAndGetURL} from "@/utils";

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

const formData = ref({
  name: '',
  image: ''
})

const tableData = ref([]);
const productType_id = ref(null);

const fetchData = async () => {
  try {
    ui.value.loading = true;
    const response = await axiosClient.get('/product-type');
    tableData.value = response.data;
    ui.value.loading = false;
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};

const handleConfirm = async () => {
  ui.value.dialogVisible = false;
  ui.value.loading = true;
  if (ui.value.addRecord) {
    for (const file of fileList.value) {
      try {
        formData.value.image = await uploadFileToFirebaseAndGetURL(file.raw, 'product-type');
      } catch (error) {
        console.error('Error uploading file:', error);
      }
    }
    console.log(typeof(formData.value.applyFrom))
    await axiosClient.post('/product-type', formData.value);
  } else {
    await axiosClient.put(`/product-type/${productType_id.value}`, formData.value);
  }
  ui.value.loading = false;
  await fetchData();
};

const handleUploadChange = (file, newFileList) => {
  fileList.value = newFileList.map(f => ({
    ...f,
    uid: f.uid || Date.now() // Ensure each file has a unique identifier
  }));
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
    const downloadURL = await uploadFileToFirebaseAndGetURL(newFile, 'product-type');
    formData.value.image = downloadURL;
    const fileIndex = fileList.value.findIndex(f => f.uid === fileToEdit.uid);
    if (fileIndex !== -1) {
      fileList.value[fileIndex] = { ...fileToEdit, raw: newFile, url: downloadURL };
    }
  }
};

const handlePictureCardPreview = (file) => {
  dialogImageUrl.value = file.url;
  dialogVisible.value = true;
};

const handleAdd = () => {
  ui.value.dialogVisible = true;
  ui.value.addRecord = true;
  formData.value.name = '';
  formData.value.image = '';
  fileList.value = [];
};

const handleEditRow = async (row: object) => {
  ui.value.dialogVisible = true;
  ui.value.addRecord = false;
  productType_id.value = row.id;
  formData.value.name = row.name;
  formData.value.image = row.image;

  fileList.value = [{
    url: formData.value.image
  }]
};

const handleDeleteRow = async (id: number) => {
  await axiosClient.delete(`/product-type/${id}`);
  await fetchData();
};

fetchData()
</script>