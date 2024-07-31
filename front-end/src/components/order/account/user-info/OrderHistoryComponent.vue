<template>
  <div>
    <ul>
      <div class="card-history-item" v-for="(item, index) in paginatedBills" :key="index">
        <div class="card-history-head d-flex justify-content-between">
          <p class="card-history-ref">
            {{ item.code }}
          </p>
          <div>
            <el-tag v-if="item.paymentStatus" type="danger" effect="dark">UNPAID</el-tag>
            <el-tag v-else type="success" effect="dark">PAID</el-tag>
          </div>
        </div>
        <div class="card-history-body">
          <div class="product-info my-1">
            <span>{{ item.products[0].productName }}</span>
          </div>
          <span class="total-price">{{ Utils.formatPrice(item.value) }}</span>
        </div>
        <div class="card-history-footer">
          <div class="date-order-wrap">
            <span>{{ new Date(item.modifyTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: false }) }}</span> -
            <span>{{ new Date(item.modifyTime).toLocaleDateString('vi-VN') }}</span>
          </div>
          <div class="user-bean-footer" v-if="item.valueOfCustomerPoint > 0 && !item.paymentStatus">
            + {{ item.valueOfCustomerPoint }} points
          </div>
        </div>
      </div>
    </ul>

    <el-pagination
        @current-change="handlePageChange"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="bills.length"
        layout="total, prev, pager, next"
        class="justify-content-center"
    ></el-pagination>
  </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref } from 'vue';
import store from '@/store';
import axiosClient from '@/utils/axiosConfig';
import * as Utils from '@/utils/index';

const user = computed(() => store.getters.user);
const bills = ref<any[]>([]);
const currentPage = ref(1);
const pageSize = ref(5); // Số lượng mục mỗi trang

const paginatedBills = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return bills.value.slice(start, end);
});

const getBills = async () => {
  const response = await axiosClient.get(`/users/bills/${user.value.id}`);
  bills.value = response.data;
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
};

onMounted(() => {
  getBills();
});
</script>

<style scoped>
.card-history-item {
  background: #fff;
  border: 1px solid #ededed;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  margin-top: 16px;
  padding: 12px 17px;
}

.card-history-head {
  margin-bottom: -14px;
}

.card-history-ref {
  background: rgba(255, 231, 186, .6);
  border-radius: 4px;
  color: #f5222d;
  font-size: 12px;
  font-weight: 600;
  padding: 3px;
  text-align: center;
  width: 100px;
}
.product-info,
.total-price {
  color: #262626;
  font-size: 16px;
}

.total-price {
  font-weight: 600;
}

.card-history-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 4px;
}

.date-order-wrap {
  color: rgba(38, 38, 38, .5);
  font-size: 12px;
}

.user-bean-footer {
  color: #52c41a;
  font-size: 12px;
}
</style>