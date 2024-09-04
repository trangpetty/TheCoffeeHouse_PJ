<template>
  <div class="box box-shadow mb-3">
    <el-form label-width="auto" :model="queryForm">
      <el-row>
        <el-col :span="8">
          <el-form-item label="Name" style="margin-right: 100px">
            <el-input v-model="queryForm.name" @change="fetchData"/>
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
      </el-form-item>
    </el-form>
  </div>
  <div class="box box-shadow">
    <!-- Table -->
    <el-table :data="tableData" stripe v-loading="ui.loading" @row-click="handleEditRow">
      <el-table-column type="index" label="#" />
      <el-table-column prop="name" label="Name" />
      <el-table-column label="Action">
        <template #default="{ row }">
          <el-button type="success" size="small" @click.stop="handleEditRow(row)">Update</el-button>
          <el-button type="danger" size="small" @click.stop="handleDeleteRow(row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- Dialog -->

    <el-dialog v-model="ui.dialogVisible" width="40%" class="dialog" v-loading="ui.loading" :title="ui.addRecord?'Add hashtag':'Update hashtag'">
      <el-form :model="formData" label-width="auto">
        <el-input v-model="hashtag_id" class="d-none" />
        <el-form-item label="Name">
          <el-input v-model="formData.name" />
        </el-form-item>
        <el-form-item label="Type">
          <el-select v-model="selectedProductType" @change="getProducts">
            <el-option v-for="item in productTypes" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Products">
          <el-select v-model="selectedProducts" multiple>
            <el-option v-for="item in products" :key="item.id" :label="item.id + ' - ' + item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div class="text-end">
        <el-button @click="ui.dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleConfirm">Confirm</el-button>
      </div>
    </el-dialog>
    <!-- Pagination -->
<!--    <el-pagination class="pagination" v-model:current-page="currentPage" layout="total, prev, pager, next" :total="total" @current-change="fetchData" />-->
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import axiosClient from "@/utils/axiosConfig";

const ui = ref({
  dialogVisible: false,
  loading: false,
  addRecord: false,
});

const queryForm = ref({
  name: '',
});

const formData = ref({
  name: '',
});

const tableData = ref([]);
const hashtag_id = ref(null);
const productTypes = ref([]);
const products = ref([]);
const selectedProductType = ref({});
const selectedProducts = ref([]);

const fetchData = async () => {
  try {
    ui.value.loading = true;
    const response = await axiosClient.get('/hashtags');
    tableData.value = response.data;
    ui.value.loading = false;
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};

const handleAdd = async () => {
  ui.value.dialogVisible = true;
  ui.value.addRecord = true;
  formData.value.name = '';
  selectedProducts.value = [];
};

const handleEditRow = async (row: object) => {
  ui.value.dialogVisible = true;
  ui.value.addRecord = false;
  hashtag_id.value = row.id;
  formData.value.name = row.name;
  selectedProducts.value = row.products;
};

const handleDeleteRow = async (id: number) => {
  await axiosClient.delete(`/hashtags/${id}`);
  await fetchData();
};

const handleConfirm = async () => {
  ui.value.dialogVisible = false;
  ui.value.loading = true;
  if (ui.value.addRecord) {
    await axiosClient.post('/hashtags', {
      name: formData.value.name,
      products: selectedProducts.value
    });
  } else {
    await axiosClient.put(`/hashtags/${hashtag_id.value}`, {
      id: hashtag_id.value,
      name: formData.value.name,
      products: selectedProducts.value
    });
  }
  ui.value.loading = false;
  await fetchData();
};

const resetForm = () => {
  queryForm.value.name = '';
  fetchData();
};

const getProductTypes = async () => {
  const response = await axiosClient.get('/product-type');
  productTypes.value = response.data;
};

const getProducts = async () => {
  try {
    if (selectedProductType.value) {
      const result = await axiosClient.get(`/products/name/${selectedProductType.value}`);
      if (result.status === 200) {
        products.value = result.data;
        console.log('Products fetched:', products.value);  // Debugging line
      }
    }
  } catch (error) {
    console.error('Failed to fetch products:', error);
  }
};

onMounted(() => {
  fetchData();
  getProductTypes();
})

</script>

<style scoped>

</style>