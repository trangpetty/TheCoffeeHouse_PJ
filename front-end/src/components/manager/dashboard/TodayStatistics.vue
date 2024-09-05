<template>
  <div class="flex flex-wrap mb-4 overflow-hidden">
    <CountUpCard
        :value="statistics.totalRevenue"
        title="Doanh thu hôm nay"
        icon="fa-solid fa-money-bills"
        cardColor="#16A2B9"
    />
    <CountUpCard
        :value="statistics.totalProductsSold"
        title="Sản phẩm bán hôm nay"
        icon="fa-solid fa-cart-plus"
        cardColor="#29A744"
    />
    <CountUpCard
        :value="statistics.totalOrders"
        title="Đơn đặt hôm nay"
        icon="fa-solid fa-bag-shopping"
        cardColor="#FEC106"
    />
    <CountUpCard
        :value="statistics.newCustomers"
        title="Khách hàng mới hôm nay"
        icon="fa-solid fa-user-plus"
        cardColor="#DC3546"
    />
  </div>
</template>
<script setup lang="ts">
import CountUpCard from "@/components/manager/dashboard/CountUpCard.vue";
import {onMounted, ref} from "vue";
import axiosClient from "@/utils/axiosConfig";
import {connectWebSocket} from "@/services/websocketService";


const statistics = ref({
  totalRevenue: 0,
  totalProductsSold: 0,
  totalOrders: 0,
  newCustomers: 0,
});

const fetchTodayStatistics = async () => {
  try {
    const response = await axiosClient.get('/bills/today-statistics');
    statistics.value = response.data;
  } catch (error) {
    console.error('Error fetching today statistics:', error);
  }
};


onMounted(() => {
  fetchTodayStatistics();
});
const updateStatistics = (message: any) => {
  let parsedData;

  console.log("update statistics: ", message)
  // Kiểm tra nếu message body là dạng Uint8Array
  if (message._binaryBody instanceof Uint8Array) {
    // Sử dụng TextDecoder để giải mã Uint8Array thành chuỗi
    const decodedText = new TextDecoder().decode(message._binaryBody);

    try {
      // Parse chuỗi JSON
      parsedData = JSON.parse(decodedText);
      console.log("Parsed JSON: ", parsedData);
    } catch (error) {
      console.error("Lỗi khi parse JSON:", error);
      return;
    }
  } else {
    // Nếu không phải Uint8Array, sử dụng message.body trực tiếp
    parsedData = message.body || message; // Trường hợp dữ liệu đã là JSON
  }

  // Cập nhật statistics.value với dữ liệu đã parse
  statistics.value = parsedData;
  console.log("newStatistics: ", parsedData);
};

// Đăng ký WebSocket với các chủ đề
connectWebSocket({
  '/topic/statistics': updateStatistics,
  // Thêm các chủ đề khác nếu cần
});


</script>

<style scoped>

@keyframes countUp {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.count-up {
  display: inline-block;
  animation: countUp 0.5s ease-out;
}

</style>