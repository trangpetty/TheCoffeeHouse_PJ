<template>
  <div>
    <div class="box box-shadow mb-3">
      <el-form label-width="auto" :model="queryForm">
        <el-row>
          <el-col :span="8">
            <el-form-item label="Code">
              <el-input
                  v-model="queryForm.code"
                  class="input"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Status">
              <el-select v-model="queryForm.status" class="input">
                <el-option label="Active" :value="0"/>
                <el-option label="Inactive" :value="1"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="From">
              <el-date-picker
                  v-model="queryForm.applyFrom"
                  type="date"
                  style="width: 100%"
                  value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="To">
              <el-date-picker
                  v-model="queryForm.applyTo"
                  type="date"
                  style="width: 100%"
                  value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" @click="fetchData">Search</el-button>
          <el-button type="primary" @click="resetForm">Reset</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="box box-shadow">
      <el-table :data="tableData" stripe v-loading="ui.loading" @row-click="handleDetail" highlight-current-row>
        <el-table-column type="index" label="#"/>.
        <el-table-column prop="code" label="Code" />
        <el-table-column prop="totalValue" label="Total" />
        <el-table-column prop="value" label="Value" />
        <el-table-column label="status">
          <template #default={row}>
            <span :style="row.status === 0 ? 'color:#5daf34':'color:red'">
              {{ row.status === 0 ? 'Active' : 'Inactive' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="Create Time" />
        <el-table-column prop="modifyTime" label="Modify Time" />
        <el-table-column>
          <template #default={row}>
            <el-button type="primary" circle size="small" @click.stop="handleDetail(row)">
              <font-awesome-icon icon="fa-solid fa-info" />
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Dialog -->
      <el-dialog v-model="ui.dialogVisible" width="40%" class="dialog" title="Detail">
        <el-table :data="bill.products" style="width: 100%">
          <el-table-column prop="id" label="Product ID"></el-table-column>
          <el-table-column prop="productName" label="Product Name"></el-table-column>
          <el-table-column prop="quantity" label="Quantity"></el-table-column>
          <el-table-column prop="price" label="Price"></el-table-column>
        </el-table>
        <p class="text-end">Total: {{ bill.totalValue }}</p>
      </el-dialog>

      <!-- Pagination -->
      <el-pagination class="pagination" v-model:current-page="currentPage" layout="total, prev, pager, next" :total="total" @current-change="fetchData" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import {  ref } from 'vue';
import axios from 'axios';

const ui = ref({
  dialogVisible: false,
  loading: false,
  addRecord: false,
  userRole: false,
  adminRole: false,
  nameDisable: false,
});

const queryForm = ref({
  code: '',
  status: 0,
  applyFrom: null,
  applyTo: null
});

const bill = ref({})

const currentPage = ref(1);
const total = ref(0);
const tableData = ref([]);

const fetchData = async () => {
  try {
    ui.value.loading = true;
    let response: object
    response = await axios.get('http://localhost:8082/api/bills', {
      params: {
        code: queryForm.value.code,
        status: queryForm.value.status,
        applyFrom: queryForm.value.applyFrom,
        applyTo: queryForm.value.applyTo,
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

const resetForm = () => {
  queryForm.value.name = '';
  queryForm.value.status = null;
  queryForm.value.applyFrom = null;
  queryForm.value.applyTo = null;
  currentPage.value = 1;
  fetchData();
}

const handleDetail = async (row: object) => {
  ui.value.dialogVisible = true;
  bill.value = row
}

fetchData()

</script>

<style scoped>
/* Component-specific styles */
</style>
