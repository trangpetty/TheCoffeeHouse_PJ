<template>
  <div v-if="!ui.addRecord">
    <div class="box box-shadow mb-3 px-4">
      <el-form label-width="auto">
        <el-row>
          <el-col :span="6">
            <el-form-item label="Type">
              <el-select v-model="type" @change="fetchData">
                <el-option label="All" value=""/>
                <el-option label="#CoffeeLover" value="#CoffeeLover"/>
                <el-option label="Cập nhật từ Nhà" value="Cập nhật từ Nhà"/>
                <el-option label="Ưu đãi đặc biệt" value="Ưu đãi đặc biệt"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" class="ms-3">
            <el-form-item>
              <el-button type="primary" @click="handleAdd">
                <font-awesome-icon icon="fa-solid fa-circle-plus" class="me-1"/><span>Add</span>
              </el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="box box-shadow mb-3 px-4">
      <el-table :data="tableData" stripe v-loading="ui.loading" @row-click="handleEditRow" highlight-current-row>
        <el-table-column type="index" label="#" />
        <el-table-column prop="userId" label="User" />
        <el-table-column label="Title">
          <template #default="{ row }">
            <div class="text-truncate">{{ row.title }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="Type" />
        <el-table-column prop="createTime" label="Create Time" />
        <el-table-column prop="modifyTime" label="Modify Time" />
        <el-table-column>
          <template #default="{ row }">
            <el-button type="success" size="small" @click.stop="handleEditRow(row)">Update</el-button>
            <el-button v-if="row.removable" type="danger" size="small" @click.stop="handleDeleteRow(row.id)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <el-pagination class="pagination" v-model:current-page="currentPage" layout="total, prev, pager, next" :total="total" @current-change="fetchData" />
    </div>
  </div>
  <AddNews v-else :news="currentNews" v-model:visible="ui.addRecord" @update:visible="ui.addRecord = false" @refreshData="fetchData"/>
</template>

<script lang="ts" setup>
import AddNews from '@/components/manager/news/NewsView.vue'
import { ref } from 'vue';
import axiosClient from '@/utils/axiosConfig';

const ui = ref({
  addRecord: false,
  loading: false
});

const type = ref('');
const currentPage = ref(1);
const total = ref(0);
const tableData = ref([]);
const currentNews = ref<object | null>(null);

const fetchData = async () => {
  try {
    ui.value.loading = true;
    const response = await axiosClient.get(`/news`, {
      params: {
        type: type.value,
        pageNo: currentPage.value
      }
    });
    tableData.value = response.data.content;
    ui.value.loading = false;
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};

const handleEditRow = (row: object) => {
  currentNews.value = row;
  ui.value.addRecord = true;
};

const handleAdd = () => {
  currentNews.value = null;
  ui.value.addRecord = true;
};

fetchData();
</script>
