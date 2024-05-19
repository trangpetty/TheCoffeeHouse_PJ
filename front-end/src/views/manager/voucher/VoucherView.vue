<template>
  <div>
    <div class="box box-shadow mb-3">
      <el-form label-width="auto" :model="queryForm">
        <el-row>
          <el-col :span="8">
            <el-form-item label="Name" style="margin-right: 100px">
              <el-input
                  v-model="queryForm.name"
                  size="mini"
                  class="input"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Type">
              <el-select v-model="queryForm.status" class="input" size="mini">
                <el-option :label="0" value="0" />
                <el-option :label="1" value="1" />
              </el-select>
            </el-form-item>
          </el-col>

        </el-row>
        <el-form-item>
          <el-button type="primary" @click="loadTableData" size="mini">Search</el-button>
          <el-button type="primary" @click="resetForm" size="mini">Reset</el-button>
          <el-button type="primary" @click="handleAdd" size="mini">
            <i class="el-icon-circle-plus-outline"/><span>Add</span>
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="box box-shadow">
      <el-table :data="tableData" stripe v-loading="ui.loading">
        <el-table-column type="index" label="#"/>.
        <el-table-column prop="name" label="Name" />
        <el-table-column prop="typeID" label="Type" />

        <el-table-column>
          <template #default={row}>
            <el-image :src="row.imageUrl" class="w-50"/>
          </template>
        </el-table-column>
        <el-table-column>
          <template #default={row}>
            <el-button type="success" size="small">
              Update
            </el-button>
            <el-button type="danger" size="small">
              Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          class="pagination"
          v-model:current-page="currentPage"
          layout="total, prev, pager, next"
          :total="total"
          @current-change="fetchData"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { defineComponent, ref, onMounted } from 'vue';
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
  name: '',
  status: 0,
  applyFrom: null,
  applyTo: null
})

const types = ref([{}])
const currentPage = ref(1);
const total = ref(0);
const tableData = ref([]);

const fetchData = async () => {
  try {
    ui.value.loading = true;
    let response: any
    response = await axios.get('http://localhost:8082/api/vouchers', {
      params: {
        name: queryForm.value.name,
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

const getTypes = async () => {
  const response = await axios.get('http://localhost:8082/api/product-type');
  if (response.success) {
    types.value = response.data
  }
}

fetchData()
getTypes()
console.log(types.value[0])

</script>

<style scoped>
/* Component-specific styles */
</style>
