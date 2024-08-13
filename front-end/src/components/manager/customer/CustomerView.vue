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
      <el-table :data="tableData" stripe v-loading="ui.loading" highlight-current-row>
        <el-table-column type="index" label="#"/>.
        <el-table-column prop="name" label="Name" />
        <el-table-column prop="phoneNumber" label="Phone Number" />
        <el-table-column prop="point" label="Point" />
        <el-table-column prop="createTime" label="Create Time" />
        <el-table-column prop="modifyTime" label="Modify Time" />
        <el-table-column label="Detail">
          <template #default={row}>
            <el-button type="primary" circle @click="showDetail(row.id)">i</el-button>
          </template>
        </el-table-column>
        <el-table-column>
          <template #default={row}>
            <el-button type="success" size="small" @click.stop="handleEditRow(row)">Update</el-button>
            <el-button type="danger" size="small" @click.stop="handleDeleteRow(row.id)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Dialog Update Or Add -->
      <el-dialog v-model="ui.dialogVisible" width="40%" class="dialog" v-loading="ui.loading" :title="ui.addRecord?'Add customer':'Update customer'">
        <el-form :model="formData" label-width="auto">
          <el-input v-model="customer_id" class="d-none" />
          <el-form-item label="Name">
            <el-input v-model="formData.name" />
          </el-form-item>
          <el-form-item label="Phone Number">
            <el-input v-model="formData.phoneNumber" />
          </el-form-item>
          <el-form-item label="Address">
            <el-input v-model="formData.address" />
          </el-form-item>
          <el-form-item label="Point" v-if="!ui.addRecord">
            <el-input v-model="formData.point" />
          </el-form-item>
        </el-form>
        <div class="text-end">
          <el-button @click="ui.dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleConfirm">Confirm</el-button>
        </div>
      </el-dialog>

      <!-- Dialog Detail -->
      <el-dialog v-model="ui.dialogDetail" width="40%" class="dialog" title="Detail">
        <el-table :data="contactDetails" stripe>
          <el-table-column prop="id" label="ID" width="40"/>
          <el-table-column prop="name" label="Name"/>
          <el-table-column prop="phoneNumber" label="Phone Number"/>
          <el-table-column prop="address" label="Address"/>
        </el-table>
      </el-dialog>

      <!-- Pagination -->
      <el-pagination class="pagination" v-model:current-page="currentPage" layout="total, prev, pager, next" :total="total" @current-change="fetchData" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import {  ref } from 'vue';
import axiosClient from '@/utils/axiosConfig';
import * as Utils from '@/utils';

const ui = ref({
  dialogVisible: false,
  loading: false,
  addRecord: false,
  dialogDetail: false
});

const queryForm = ref({
  name: '',
  phoneNumber: ''
});

const formData = ref({
  name: '',
  address: '',
  phoneNumber: '',
  point: 0
})

const currentPage = ref(1);
const total = ref(0);
const tableData = ref([]);
const customer_id = ref(0);
const contactDetails = ref([]);

const fetchData = async () => {
  try {
    ui.value.loading = true;
    let response: object
    response = await axiosClient.get('/customers', {
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

const handleConfirm = async () => {
  ui.value.dialogVisible = false;
  ui.value.loading = true;
  if (ui.value.addRecord) {
    await axiosClient.post('/customers', formData.value);
  } else {
    await axiosClient.put(`/customers/${customer_id.value}`, formData.value);
  }
  ui.value.loading = false;
  await fetchData();
};

const resetForm = () => {
  queryForm.value.name = '';
  queryForm.value.phoneNumber = '';
  currentPage.value = 1;
  fetchData();
}

const handleAdd = () => {
  ui.value.addRecord = true;
  ui.value.dialogVisible = true;
  formData.value.name = '';
  formData.value.phoneNumber = '';
  formData.value.address = '';
  formData.value.point = 0;
}

const handleEditRow = (row: object) => {
  ui.value.dialogVisible = true;
  ui.value.addRecord = false;
  customer_id.value = row.id
  formData.value.name = row.name;
  formData.value.phoneNumber = row.phoneNumber;
  formData.value.address = row.address;
  formData.value.point = row.point;
}

const handleDeleteRow = async (id: number) => {
  let confirmed = await Utils.confirm("This action will delete this record! Confirm to delete？", "Confirm");
  if(!confirmed) {
    return;
  }
  await axiosClient.delete(`/customers/${id}`);
  await fetchData();
}

const showDetail = async (id: number) => {
  ui.value.dialogDetail = true;
  const response = await axiosClient.get(`/customers/detail/${id}`);
  contactDetails.value = response.data;
}

fetchData()

</script>

<style scoped>
/* Component-specific styles */
</style>
