<template>
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
              <el-option v-for="month in Utils.months" :key="month.value" :label="month.label" :value="month.value"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item v-if="selectedReportType === 'monthly'" label="Week">
            <el-select v-model="selectedWeekTopProducts" style="width: 120px" @change="fetchTopProducts">
              <el-option v-for="week in Utils.weeks" :key="week" :label="'Week ' + week" :value="week"></el-option>
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
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import Highcharts from 'vue3-highcharts';
import * as Utils from '@/utils/index';
import axiosClient from "@/utils/axiosConfig";

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

const selectedReportType = ref('monthly');
const selectedMonthTopProducts = ref(Utils.getCurrentMonth());
const selectedWeekTopProducts = ref(Utils.getCurrentWeekInMonth());
const selectedDate = ref(null);
const products = ref([]);

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

onMounted(() => {
  fetchTopProducts();
})
</script>

<style scoped>
.box {
  padding: 1rem 1.5rem
}
</style>
