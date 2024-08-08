<template>
  <div>
    <div class="flex flex-wrap mb-4 overflow-hidden">
      <el-card class="card-item rounded text-white" shadow="always" style="background-color: #16A2B9">
        <div>
          <h1>{{ formatValueKMB(statistics.totalRevenue) }}</h1>
          <h5>Doanh thu hôm nay</h5>
        </div>
        <font-awesome-icon class="fs-1" icon="fa-solid fa-money-bills" />
      </el-card>
      <el-card class="card-item rounded text-white" shadow="always" style="background-color: #29A744">
        <div>
          <h1>{{ statistics.totalProductsSold }}</h1>
          <h5>Sản phẩm bán hôm nay</h5>
        </div>
        <font-awesome-icon class="fs-1" icon="fa-solid fa-cart-plus" />
      </el-card>
      <el-card class="card-item rounded text-white" shadow="always" style="background-color: #FEC106">
        <div>
          <h1>{{ statistics.totalOrders }}</h1>
          <h5>Đơn đặt hôm nay</h5>
        </div>
        <font-awesome-icon class="fs-1" icon="fa-solid fa-bag-shopping" />
      </el-card>
      <el-card class="card-item rounded text-white" shadow="always" style="background-color: #DC3546">
        <div>
          <h1>{{ statistics.newCustomers }}</h1>
          <h5>Khách hàng mới hôm nay</h5>
        </div>
        <font-awesome-icon class="fs-1" icon="fa-solid fa-user-plus" />
      </el-card>
    </div>
    <div class="d-flex flex-wrap mb-4">
      <div class="box-shadow box chart">
        <el-form-item label="Report Type">
          <el-select v-model="selectedReport" style="width: 120px">
            <el-option label="Monthly" value="monthly"></el-option>
            <el-option label="Weekly" value="weekly"></el-option>
            <el-option label="Yearly" value="yearly"></el-option>
            <el-option label="By Product Type" value="productType"></el-option>
  <!--          <el-option label="Daily" value="daily"></el-option>-->
          </el-select>
        </el-form-item>
        <Highcharts :options="chartOptions"></Highcharts>
      </div>
      <div class="box-shadow box chart">
        <h3>Top 5 products best seller</h3>
        <div class="d-flex justify-content-between align-items-center">
          <el-form-item label="Report Type">
            <el-select v-model="selectedReportType" style="width: 120px" @change="handleReportTypeChange">
              <el-option label="Monthly" value="monthly"></el-option>
              <el-option label="Yearly" value="yearly"></el-option>
              <el-option label="Daily" value="daily"></el-option>
            </el-select>
          </el-form-item>

          <div>
            <div class="d-flex justify-content-between">
              <el-form-item v-if="selectedReportType === 'monthly'" label="Month" class="me-4">
                <el-select v-model="selectedMonthTopProducts" style="width: 120px" @change="fetchTopProducts">
                  <el-option v-for="month in months" :key="month.value" :label="month.label" :value="month.value"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item v-if="selectedReportType === 'monthly'" label="Week">
                <el-select v-model="selectedWeekTopProducts" style="width: 120px" @change="fetchTopProducts">
                  <el-option v-for="week in weeks" :key="week" :label="'Week ' + week" :value="week"></el-option>
                </el-select>
              </el-form-item>
            </div>

            <el-form-item v-if="selectedReportType === 'daily'" label="Date">
              <el-date-picker v-model="selectedDate" type="date" placeholder="Select Date" @change="fetchTopProducts"></el-date-picker>
            </el-form-item>
          </div>
        </div>
        <Highcharts :options="topProductsChartOptions"></Highcharts>
      </div>
    </div>
    <div class="d-flex flex-wrap mb-4">
      <div class="box-shadow box chart">
        <div class="d-flex">
          <el-form-item label="Month" class="me-4">
            <el-select v-model="selectedMonth" style="width: 120px">
              <el-option v-for="month in months" :key="month.value" :label="month.label" :value="month.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Week">
            <el-select v-model="selectedWeek" style="width: 120px">
              <el-option v-for="week in weeks" :key="week" :label="'Week ' + week" :value="week"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <Highcharts :options="dailyChartOptions"></Highcharts>
      </div>
      <div class="box-shadow box chart">
        <h3>Order Status Statistics</h3>
        <div class="d-flex justify-content-between align-items-center">
          <el-form-item label="Report Type">
            <el-select v-model="selectedReportTypeOrders" style="width: 120px" @change="handleReportTypeOrdersChange">
              <el-option label="Monthly" value="monthly"></el-option>
              <el-option label="Yearly" value="yearly"></el-option>
              <el-option label="Daily" value="daily"></el-option>
            </el-select>
          </el-form-item>

          <div>
            <div class="d-flex justify-content-between">
              <el-form-item v-if="selectedReportTypeOrders === 'monthly'" label="Month" class="me-4">
                <el-select v-model="selectedMonthOrders" style="width: 120px" @change="fetchOrderStatusData">
                  <el-option v-for="month in months" :key="month.value" :label="month.label" :value="month.value"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item v-if="selectedReportTypeOrders === 'monthly'" label="Week">
                <el-select v-model="selectedWeekOrders" style="width: 120px" @change="fetchOrderStatusData">
                  <el-option v-for="week in weeks" :key="week" :label="'Week ' + week" :value="week"></el-option>
                </el-select>
              </el-form-item>
            </div>

            <el-form-item v-if="selectedReportTypeOrders === 'daily'" label="Date">
              <el-date-picker v-model="selectedDateOrders" type="date" placeholder="Select Date" @change="fetchOrderStatusData"></el-date-picker>
            </el-form-item>
          </div>
        </div>
        <Highcharts :options="orderStatusChartOptions"></Highcharts>
      </div>
    </div>
    <div class="box-shadow box chart">
      <h3>Phân loại nhóm tuổi người dùng</h3>
      <Highcharts :options="ageGroupChartOptions"></Highcharts>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue';
import Highcharts from 'vue3-highcharts';
import axiosClient from '@/utils/axiosConfig';
import moment from 'moment';

// Hàm để lấy tháng hiện tại và tuần hiện tại trong tháng
const getCurrentMonth = () => new Date().getMonth() + 1;
const getCurrentWeekInMonth = () => {
  const today = moment();
  const startOfMonth = moment().startOf('month');

  // Tính số tuần của ngày hôm nay trong tháng
  const weekOfMonth = today.week() - startOfMonth.week() + 1;
  return weekOfMonth;
};

const chartOptions = ref({
  chart: { type: 'line' },
  title: { text: 'Revenue Report' },
  xAxis: { categories: [] },
  yAxis: { title: { text: 'Revenue (VND)' }, labels: { formatter: function() { return formatValueKMB(this.value); } } },
  series: [{ name: 'Revenue', data: [], dataLabels: { enabled: true, formatter: function() { return formatValueKMB(this.y); } } }]
});

const dailyChartOptions = ref({
  chart: { type: 'column' },
  title: { text: 'Weekly Revenue' },
  xAxis: { categories: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'] },
  yAxis: { title: { text: 'Revenue (VND)' }, labels: { formatter: function() { return formatValueKMB(this.value); } } },
  series: [{ name: 'Revenue', data: [0, 0, 0, 0, 0, 0, 0], dataLabels: { enabled: true, formatter: function() { return formatValueKMB(this.y); } } }]
});

const topProductsChartOptions = ref({
  chart: {
    type: 'pie'
  },
  title: {
    text: 'Top 5 Best Selling Products'
  },
  series: [{
    name: 'Quantity Sold',
    data: []
  }],
  plotOptions: {
    pie: {
      allowPointSelect: true,
      cursor: 'pointer',
      dataLabels: {
        enabled: true,
        format: '<b>{point.name}</b>: {point.percentage:.1f} %'
      }
    }
  }
});

const selectedReport = ref('monthly');
const selectedMonth = ref(getCurrentMonth());
const selectedWeek = ref(getCurrentWeekInMonth());
const selectedReportType = ref('monthly');
const selectedReportTypeOrders = ref('monthly');
const selectedMonthTopProducts = ref(getCurrentMonth());
const selectedMonthOrders = ref(getCurrentMonth());
const selectedWeekTopProducts = ref(getCurrentWeekInMonth());
const selectedWeekOrders = ref(getCurrentWeekInMonth());
const selectedDate = ref(null);
const selectedDateOrders = ref(null);
const products = ref([]);

const months = [
  { value: 1, label: 'January' },
  { value: 2, label: 'February' },
  { value: 3, label: 'March' },
  { value: 4, label: 'April' },
  { value: 5, label: 'May' },
  { value: 6, label: 'June' },
  { value: 7, label: 'July' },
  { value: 8, label: 'August' },
  { value: 9, label: 'September' },
  { value: 10, label: 'October' },
  { value: 11, label: 'November' },
  { value: 12, label: 'December' }
];

const weeks = [1, 2, 3, 4, 5];

const statistics = ref({
  totalRevenue: 0,
  totalProductsSold: 0,
  totalOrders: 0,
  newCustomers: 0,
});

const ageGroupData = ref({});

const ageGroupChartOptions = ref({
  chart: {
    type: 'pie'
  },
  title: {
    text: 'Phân loại nhóm tuổi người dùng'
  },
  series: [{
    name: 'Số lượng người dùng',
    data: Object.entries(ageGroupData.value).map(([ageGroup, count]) => ({
      name: ageGroup,
      y: count
    }))
  }],
  plotOptions: {
    pie: {
      allowPointSelect: true,
      cursor: 'pointer',
      dataLabels: {
        enabled: true,
        format: '<b>{point.name}</b>: {point.percentage:.1f} %'
      }
    }
  }
});

const orderStatusChartOptions = ref({
  chart: {
    type: 'pie'
  },
  title: {
    text: 'Order Status Statistics'
  },
  series: [{
    name: 'Orders',
    data: []
  }],
  plotOptions: {
    pie: {
      allowPointSelect: true,
      cursor: 'pointer',
      dataLabels: {
        enabled: true,
        format: '<b>{point.name}</b>: {point.percentage:.1f} %'
      }
    }
  }
});

const fetchOrderStatusData = async () => {
  try {
    let params: any = { reportType: selectedReportTypeOrders.value, year: new Date().getFullYear() };
    if (selectedReportTypeOrders.value === 'monthly') {
      params.month = selectedMonthOrders.value;
      params.week = selectedWeekOrders.value;
    } else if (selectedReportTypeOrders.value === 'daily') {
      params.date = selectedDateOrders.value ? selectedDateOrders.value.toISOString().split('T')[0] : null;
    }

    const response = await axiosClient.get('/bills/status-summary', {params});
    const data = response.data;

    // Format data for Highcharts pie chart
    const statusCounts = { success: 0, fail: 0, cancel: 0 };

// Process the data to count the orders for each status
    data.forEach(item => {
      if (item.status in statusCounts) {
        statusCounts[item.status] = item.orders;
      }
    });

// Format data for Highcharts pie chart
    orderStatusChartOptions.value.series[0].data = [
      { name: 'Success', y: statusCounts.success, color: '#28a745' },
      { name: 'Fail', y: statusCounts.fail, color: '#dc3545' },
      { name: 'Cancel', y: statusCounts.cancel, color: '#ffc107' }
    ];
  } catch (error) {
    console.error('Error fetching order status data:', error);
  }
};

const fetchTodayStatistics = async () => {
  try {
    const response = await axiosClient.get('/bills/today-statistics');
    statistics.value = response.data;
  } catch (error) {
    console.error('Error fetching today statistics:', error);
  }
};

const handleReportTypeChange = () => {
  if (selectedReportType.value === 'monthly') {
    selectedWeekTopProducts.value = 1;
    fetchTopProducts();
  } else if (selectedReportType.value === 'daily') {
    selectedDateOrders.value = null;
    fetchTopProducts();
  }
};

const handleReportTypeOrdersChange = () => {
  if (selectedReportTypeOrders.value === 'monthly') {
    selectedWeekOrders.value = 1;
    fetchTopProducts();
  } else if (selectedReportTypeOrders.value === 'daily') {
    selectedDate.value = null;
    fetchTopProducts();
  }
};

const fetchTopProducts = async () => {
  try {
    let params: any = { reportType: selectedReportType.value, year: new Date().getFullYear() };
    if (selectedReportType.value === 'monthly') {
      params.month = selectedMonthTopProducts.value;
      params.week = selectedWeekTopProducts.value;
    } else if (selectedReportType.value === 'daily') {
      params.date = selectedDate.value ? selectedDate.value.toISOString().split('T')[0] : null;
    }

    const response = await axiosClient.get('/products/top-products', { params });
    console.log('Top Products Response:', response.data); // Check if data is received correctly
    products.value = response.data;

    // Format data for Highcharts pie chart with colors
    const colors = ['#FF474C', '#FFA500', '#FFFF00', '#90EE90', '#3a9cbd'];
    topProductsChartOptions.value.series[0].data = response.data.map((product: any, index: number) => ({
      name: product.productName,
      y: product.totalQuantity,
      color: colors[index % colors.length] // Use colors array to assign colors
    }));
  } catch (error) {
    console.error('Error fetching top products:', error);
  }
};

const fetchRevenueData = async () => {
  try {
    const response = await axiosClient.get('/bills/revenue', { params: { type: selectedReport.value } });
    const data = response.data;
    chartOptions.value.xAxis.categories = data.map(item => item.label);
    chartOptions.value.series[0].data = data.map(item => item.value);
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};

const fetchDailyRevenueData = async () => {
  if (!selectedMonth.value || !selectedWeek.value) return;
  try {
    const response = await axiosClient.get('/bills/revenue', { params: { type: 'daily', week: selectedWeek.value, month: selectedMonth.value } });
    const data = response.data;
    const daysOfWeek = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
    const dailyData = Array(7).fill(0);
    data.forEach(item => {
      const dayIndex = daysOfWeek.indexOf(item.label);
      if (dayIndex !== -1) {
        dailyData[dayIndex] = item.value;
      }
    });
    dailyChartOptions.value.series[0].data = dailyData;
  } catch (error) {
    console.error('Error fetching daily dashboard data:', error);
  }
};

const fetchAgeGroupData = async () => {
  try {
    const response = await axiosClient.get('/users/age-groups');
    const data = response.data;

    // Cập nhật dữ liệu phân loại nhóm tuổi
    ageGroupData.value = data;

    // Cập nhật tùy chọn biểu đồ
    ageGroupChartOptions.value.series[0].data = Object.entries(ageGroupData.value).map(([ageGroup, count]) => ({
      name: ageGroup,
      y: count
    }));
  } catch (error) {
    console.error('Error fetching age group data:', error);
  }
};

const formatValueKMB = (value: number) => {
  if (value >= 1000000) return (value / 1000000).toFixed(1) + 'M';
  if (value >= 1000) return (value / 1000).toFixed(1) + 'K';
  return value.toString();
};

watch(selectedReport, fetchRevenueData);
watch([selectedMonth, selectedWeek], fetchDailyRevenueData);

onMounted(() => {
  fetchRevenueData();
  fetchDailyRevenueData();
  fetchTopProducts();
  fetchTodayStatistics();
  fetchOrderStatusData();
  fetchAgeGroupData();
});
</script>

<style scoped>
.box {
  padding: 1rem 1.5rem
}

.card-item {
  width: calc(50% - 40px)!important;
  float: left;
  margin: 0 10px;
}

::v-deep .el-card__body {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

@media (min-width: 992px) {
  .card-item {
    width: calc(25% - 20px) !important;
  }

  .chart {
    width: calc(50% - 20px)!important;
    float: left;
    margin: 0 10px;
  }
}

@media (max-width: 991px) {
  .card-item {
    height: 140px;
    margin-bottom: 1rem;
  }

  .chart {
    width: 100%;
  }
}
</style>
