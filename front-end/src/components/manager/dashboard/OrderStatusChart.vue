<template>
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
              <el-option v-for="month in Utils.months" :key="month.value" :label="month.label" :value="month.value"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item v-if="selectedReportTypeOrders === 'monthly'" label="Week">
            <el-select v-model="selectedWeekOrders" style="width: 120px" @change="fetchOrderStatusData">
              <el-option v-for="week in Utils.weeks" :key="week" :label="'Week ' + week" :value="week"></el-option>
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
</template>

<script lang="ts" setup>
import Highcharts from 'vue3-highcharts';
import * as Utils from '@/utils/index';
import {onMounted, ref} from "vue";
import axiosClient from "@/utils/axiosConfig";

const selectedReportTypeOrders = ref('monthly');
const selectedMonthOrders = ref(Utils.getCurrentMonth());
const selectedWeekOrders = ref(Utils.getCurrentWeekInMonth());
const selectedDateOrders = ref(null);

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


const handleReportTypeOrdersChange = () => {
  if (selectedReportTypeOrders.value === 'monthly') {
    selectedWeekOrders.value = 1;
  } else if (selectedReportTypeOrders.value === 'daily') {
    selectedDateOrders.value = null;
  }
};

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

onMounted(() => {
  fetchOrderStatusData();
})
</script>

<style scoped>
.box {
  padding: 1rem 1.5rem
}
</style>