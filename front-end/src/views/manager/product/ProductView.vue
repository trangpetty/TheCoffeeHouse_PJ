<template>
  <div class="w-100">
    <div class="box box-shadow mb-3">
      <el-form label-width="auto" :model="queryForm">
        <el-row>
          <el-col :span="8">
            <el-form-item label="Name" style="margin-right: 100px">
              <el-input v-model="queryForm.name" @change="fetchData"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Type">
              <el-select v-model="queryForm.typeID" @change="fetchData">
                <el-option v-for="(item, index) in types" :key="index" :label="item.name" :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" @click="fetchData">Search</el-button>
          <el-button type="primary" @click="resetForm">Reset</el-button>
          <el-button type="primary" @click="handleAdd">
            <font-awesome-icon icon="fa-solid fa-circle-plus" />
            Add
          </el-button>
          <el-button type="primary" @click="()=>$router.push('/product-type')">
            Type
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="box box-shadow">
      <!-- Table -->
      <el-table :data="tableData" stripe v-loading="ui.loading">
        <el-table-column type="index" label="#" />
        <el-table-column prop="name" label="Name" />
        <el-table-column prop="price" label="Price" />
        <el-table-column prop="createTime" label="Create Time" />
        <el-table-column prop="modifyTime" label="Modify Time" />
        <el-table-column label="Image">
          <template #default="{ row }">
            <el-image :src="row.images[0].url" class="w-50" />
          </template>
        </el-table-column>
        <el-table-column label="Action">
          <template #default="{ row }">
            <el-button type="success" size="small" @click="handleEditRow(row.id)">Update</el-button>
            <el-button type="danger" size="small" @click="handleDeleteRow(row.id)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- Dialog -->

      <el-dialog v-model="ui.dialogVisible" width="40%" class="dialog" v-loading="ui.loading" :title="ui.addRecord?'Add product':'Update product'">
        <el-form :model="formData" label-width="auto">
          <el-input v-model="product_id" class="d-none" />
          <el-form-item label="Name">
            <el-input v-model="formData.name" />
          </el-form-item>
          <el-form-item label="Price">
            <el-input v-model="formData.price" :formatter="(value) => `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')" :parser="(value) => value.replace(/\$\s?|(,*)/g, '')" />
          </el-form-item>
          <el-form-item label="Type">
            <el-select v-model="formData.typeID" class="input">
              <el-option v-for="(item, index) in types" :key="index" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Description">
            <el-input type="textarea" v-model="formData.description" />
          </el-form-item>
          <el-form-item>
            <el-table :data="sizeData" style="width: 100%">
              <el-table-column label="Size" width="100">
                <template #default="{ row }">
                  <el-checkbox v-model="row.checked" :label="row.size" />
                </template>
              </el-table-column>
              <el-table-column label="Surcharge">
                <template #default="{ row }">
                  <el-input-number v-model="row.surcharge" :min="0" :step="1000" @change="handleSurchargeChange(row)" />
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
          <el-form-item label="Image">
            <el-upload action="#" list-type="picture-card" :auto-upload="false" :file-list="fileList" :on-change="handleUploadChange">
              <el-icon><Plus /></el-icon>
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
                    <span class="el-upload-list__item-delete" @click="handleRemove(file)">
                      <el-icon><Delete /></el-icon>
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
import { ref } from 'vue';
import axios from 'axios';
import { uploadFileToFirebaseAndGetURL } from "@/utils";
import { Delete, Edit, Plus, ZoomIn } from '@element-plus/icons-vue';

const dialogImageUrl = ref('');
const dialogVisible = ref(false);
const fileList = ref([]);
const removedImages = ref([]);

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
  typeID: null
});

const sizeData = ref([
  { id: null, size: 'S', checked: false, surcharge: 0 },
  { id: null, size: 'M', checked: false, surcharge: 0 },
  { id: null, size: 'L', checked: false, surcharge: 0 }
]);

const removedProductSizes = ref([]);

const formData = ref({
  name: '',
  description: '',
  images: [],
  price: 0,
  productSizes: [],
  typeID: 1
});

const product_id = ref(0);

const types = ref([{}]);
const currentPage = ref(1);
const total = ref(0);
const tableData = ref([]);

const fetchData = async () => {
  try {
    ui.value.loading = true;
    const response = await axios.get('http://localhost:8082/api/products', {
      params: {
        name: queryForm.value.name,
        typeID: queryForm.value.typeID,
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

const getTypes = async () => {
  const response = await axios.get('http://localhost:8082/api/product-type');
  types.value = response.data;
};

const handleAdd = () => {
  ui.value.dialogVisible = true;
  ui.value.addRecord = true;
  formData.value.name = '';
  formData.value.description = '';
  formData.value.image = '';
  formData.value.price = 0;
  formData.value.productSizes = [];
  formData.value.typeID = 1;
  fileList.value = []; // Clear the fileList when adding a new product
};

const handleEditRow = async (id: number) => {
  ui.value.dialogVisible = true;
  ui.value.addRecord = false;
  product_id.value = id;
  const result = await axios.get(`http://localhost:8082/api/products/${id}`);
  if (result.status === 200) {
    formData.value.name = result.data.name;
    formData.value.description = result.data.description;
    formData.value.typeID = result.data.typeID;
    formData.value.price = result.data.price;
    formData.value.productSizes = result.data.productSizes;
    formData.value.images = result.data.images;
    sizeData.value.forEach(size => {
      size.checked = formData.value.productSizes.some(ps => ps.size === size.size);
      if (size.checked) {
        const productSize = formData.value.productSizes.find(ps => ps.size === size.size);
        size.surcharge = productSize.surcharge;
        size.id = productSize.id;
      }
    });
    fileList.value = formData.value.images.map(image => ({
      url: image.url,
      name: image.url.split('/').pop(),
      uid: image.id // Assuming images have unique ids
    }));
  }
};

const handleDeleteRow = async (id: number) => {
  await axios.delete(`http://localhost:8082/api/products/${id}`);
  await fetchData();
};

const handleSurchargeChange = (row: object) => {
  row.checked = row.surcharge >= 0
}

const handlePictureCardPreview = (file) => {
  dialogImageUrl.value = file.url;
  dialogVisible.value = true;
};

const handleRemove = (file) => {
  if(file.status == "success") {
    removedImages.value.push({id: file.uid, url: file.url});
  }
  fileList.value = fileList.value.filter(f => f.uid !== file.uid);
};

const handleUploadChange = (file, newFileList) => {
  fileList.value = newFileList.map(f => ({
    ...f,
    uid: f.uid || Date.now() // Ensure each file has a unique identifier
  }));
};

const handleConfirm = async () => {
  ui.value.dialogVisible = false;
  ui.value.loading = true;
  if (ui.value.addRecord) {
    for (const file of fileList.value) {
      try {
        const downloadURL = await uploadFileToFirebaseAndGetURL(file.raw, 'products');
        formData.value.images.push({ url: downloadURL });
      } catch (error) {
        console.error('Error uploading file:', error);
      }
    }
    formData.value.productSizes = sizeData.value.filter(row => row.checked).map(row => ({
      size: row.size,
      surcharge: row.surcharge
    }));
    console.log(formData.value)
    await axios.post('http://localhost:8082/api/products', formData.value);
  } else {
    for (const file of fileList.value) {
      if (file.status === "ready") {
        const downloadURL = await uploadFileToFirebaseAndGetURL(file.raw, 'products');
        formData.value.images.push({id:null, url: downloadURL });
      }
    }
    formData.value.productSizes = sizeData.value.filter(row => row.checked).map(row => ({
      id: row.id,
      size: row.size,
      surcharge: row.surcharge
    }));

    removedProductSizes.value = sizeData.value.filter(row => !row.checked).map(row => ({
      id: row.id,
      size: row.size,
      surcharge: row.surcharge
    }));
    console.log("size data: ", sizeData.value)
    console.log("formData: ", formData.value.productSizes)
    console.log("remove; ", removedProductSizes.value)
    await axios.put(`http://localhost:8082/api/products/${product_id.value}`, {
      ...formData.value,
      removedImages: removedImages.value,
      removedProductSizes: removedProductSizes.value
    });
  }
  ui.value.loading = false;
  await fetchData();
};

const resetForm = () => {
  queryForm.value.name = '';
  queryForm.value.typeID = null;
  currentPage.value = 1;
  fetchData();
};

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
    const downloadURL = await uploadFileToFirebaseAndGetURL(newFile, 'products');
    const fileIndex = fileList.value.findIndex(f => f.uid === fileToEdit.uid);
    if (fileIndex !== -1) {
      fileList.value[fileIndex] = { ...fileToEdit, raw: newFile, url: downloadURL };
      const imageIndex = formData.value.images.findIndex(img => img.id === fileToEdit.uid);
      if (imageIndex !== -1) {
        formData.value.images[imageIndex].url = downloadURL;
      }
    }
  }
};

fetchData();
getTypes();
</script>
