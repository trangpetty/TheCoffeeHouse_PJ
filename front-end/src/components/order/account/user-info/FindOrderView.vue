<template>
  <div class="container-fluid container-lg">
    <div class="history-wrapper">
      <h1 class="tracking-title mb-0 d-flex align-items-center justify-content-center">
        <font-awesome-icon class="icon" icon="fa-solid fa-clock-rotate-left" />
        <span class="text-title">Tra cứu đơn hàng</span>
      </h1>
      <div>
        <div>
          <div class="card-product-note-item">
            <input type="text" v-model="phoneNumber" class="card-product-text" placeholder="Tra cứu đơn hàng theo số điện thoaị">
            <font-awesome-icon icon="fa-solid fa-magnifying-glass" class="card-product-note-icon" @click="searchBills"/>
          </div>
          <div>
            <div class="card-history-item" v-for="(item, index) in bills" :key="index" @click="handleChooseCard(item.code)">
              <div class="card-history-head d-flex justify-content-between">
                <p class="card-history-ref">
                  {{ item.code }}
                </p>
                <div>
                  <el-tag v-if="item.status == 'success'" type="success" effect="dark">SUCCESS</el-tag>
                  <el-tag v-else-if="item.status == 'pending'" type="warning" effect="dark">PENDING</el-tag>
                  <el-tag v-else-if="item.status == 'fail'" type="danger" effect="dark">FAIL</el-tag>
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
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import * as Utils from "@/utils";
import {ref} from "vue";
import axiosClient from "@/utils/axiosConfig";
import {ElMessage} from "element-plus";
import router from "@/router";

const phoneNumber = ref('');
const bills = ref([]);

const searchBills = async () => {
  const phoneRegex = /^[0]{1}[0-9]{9}$/;
  if (!phoneRegex.test(phoneNumber.value)) {
    ElMessage({
      message: 'Số điện thoại không hợp lệ. Vui lòng nhập lại',
      type: 'warning',
    })
    return;
  }
  try {
    const response = await axiosClient.get(`/bills/search?phoneNumber=${phoneNumber.value}`);
    bills.value = response.data;
  } catch (error) {
    console.error('Error fetching bills:', error);
  }
}

const handleChooseCard = (code: string) => {
  router.push({ name: 'payment-success', params: { code: code } });
}
</script>

<style scoped>
.history-wrapper {
  margin: auto;
  min-height: 500px;
  width: 450px;
}

.tracking-title {
  margin-top: 50px;
}

.tracking-title .icon {
  padding: var(--space-8);
  width: var(--space-24);
  height: var(--space-24);
  border-radius: 50%;
  background-color: var(--yellow-1);
  color: var(--white);
  margin-right: var(--space-16);
}

.text-title {
  font-size: 28px;
  font-weight: 600;
}

.card-product-note-item {
  background: var(--white-1);
  border: 1px solid var(--smoky-gray-4);
  border-radius: var(--space-4);
  display: flex;
  height: var(--space-44);
  margin-top: var(--space-20);
}

.card-product-text {
  border: none;
  flex: 1;
  font-size: var(--space-16);
  height: 100%;
  outline: none;
  padding: 0 var(--space-16);
  width: 80%;
  width: var(--space-200);
}

.card-product-note-icon {
  height: 20px;
  margin: 12px 11px;
  width: 18px;
  color: gray;
  cursor: pointer
}

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

.star-rating span {
  font-size: 1.5rem;
  margin-right: 5px;
}

.star-rating span.filled {
  color: var(--orange-2);
}

@media (max-width: 769px) {
  .history-wrapper {
    width: 100%;
  }
}
</style>