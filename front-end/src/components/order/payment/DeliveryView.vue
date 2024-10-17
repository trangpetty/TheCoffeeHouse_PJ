<template>
  <div>
    <div class="container-lg container-fluid">
      <div class="row justify-content-center">
        <div class="col-12 col-lg-10">
          <div class="mb-5">
            <h1 class="text-center h4 mb-0" style="font-weight: 600;">
              Đơn hàng của bạn
            </h1>
          </div>
          <div class="order-card mb-4">
            <h5>Đơn hàng #{{ bill.code }}</h5>
            <p>Tên khách hàng: {{ bill.contactDetail.name }}</p>
            <p>Địa chỉ: {{ bill.contactDetail.address }}</p>
            <p>Số điện thoại: {{ bill.contactDetail.phoneNumber }}</p>
            <p>Giá trị đơn hàng: {{ bill.paymentStatus ? Utils.formatPrice(bill.value) : Utils.formatPrice(0) }}</p>
            <p>Trạng thái: {{ getStatusText(bill.deliveryStatus) }}</p>
            <div class="btn-group">
              <button v-for="status in deliveryStatusOptions" :key="status.value"
                      :class="['btn', 'btn-status', { 'btn-active': bill.deliveryStatus === status.value }]"
                      @click="updateDeliveryStatus(status.value)">
                {{ status.label }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, defineProps } from 'vue';
import { ElMessage } from 'element-plus';
import axiosClient from '@/utils/axiosConfig';
import {connectWebSocket, sendMessage} from "@/utils/websocket";
import * as Utils from '@/utils/index'

const props = defineProps(['code']);

const bill = ref<any>({
  contactDetail: {
    name: '',
    phoneNumber: '',
    address: ''
  },
  products: [],
  paymentMethod: '',
  deliveryStatus: '',
  code: ''
});

const deliveryStatusOptions = [
  { value: 'Ordered', label: 'Đã đặt' },
  { value: 'Preparing', label: 'Đang chuẩn bị' },
  { value: 'Transmit', label: 'Đang giao' },
  { value: 'Delivered', label: 'Đã giao' },
];

const getStatusText = (status) => {
  switch (status) {
    case 'Ordered':
      return 'Đã đặt';
    case 'Preparing':
      return 'Đang chuẩn bị';
    case 'Transmit':
      return 'Đang giao';
    case 'Delivered':
      return 'Đã giao';
    default:
      return 'Không xác định';
  }
};


const updateDeliveryStatus = async (status: string) => {
  try {
    sendMessage('/app/updateStatus', { code: bill.value.code, status });

    const response = await axiosClient.put(`/bills/delivery/${bill.value.code}?deliveryStatus=${status}`);
    bill.value.deliveryStatus = status;

    ElMessage.success('Cập nhật trạng thái thành công!');
  } catch (error) {
    ElMessage.error('Lỗi khi cập nhật trạng thái.');
  }
};


onMounted(async () => {
  const response = await axiosClient.get(`/bills/${props.code}`);
  bill.value = response.data;

  connectWebSocket((message) => {
    console.log('Received WebSocket message:', message.body);
    try {
      const data = JSON.parse(message.body);
      if (data.code === bill.value.code) {
        bill.value.deliveryStatus = data.status;
      }
    } catch (e) {
      console.error('Failed to parse WebSocket message:', e);
    }
  });
});
</script>
<style scoped>
.order-card {
  border: 1px solid #e5e5e5;
  padding: 1rem;
  border-radius: 5px;
}

.btn-group {
  margin-top: 1rem;
}

.btn-status {
  margin-right: 0.5rem;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.btn-status:hover {
  opacity: 0.8;
}

.btn-active {
  background-color: #fa8c16;
  color: white;
}
</style>
