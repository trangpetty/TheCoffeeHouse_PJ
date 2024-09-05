<template>
  <div class="box-shadow box chart">
    <el-form-item label="Report Type">
      <el-select v-model="selectedReport" style="width: 120px">
        <el-option label="Monthly" value="monthly"></el-option>
        <el-option label="Weekly" value="weekly"></el-option>
        <el-option label="Yearly" value="yearly"></el-option>
      </el-select>
    </el-form-item>
    <Highcharts :options="chartOptions"></Highcharts>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref, watch} from 'vue';
import Highcharts from 'vue3-highcharts';
import moment from "moment/moment";
import axiosClient from "@/utils/axiosConfig";
import * as Utils from '@/utils/index'

const getCurrentMonth = () => new Date().getMonth() + 1;
const getCurrentWeekInMonth = () => {
  const today = moment();
  const startOfMonth = moment().startOf('month');

  // Tính số tuần của ngày hôm nay trong tháng
  const weekOfMonth = today.week() - startOfMonth.week() + 1;
  return weekOfMonth;
};

const selectedReport = ref('monthly');
const selectedMonth = ref(getCurrentMonth());
const selectedWeek = ref(getCurrentWeekInMonth());


const chartOptions = ref({
  chart: { type: 'line' },
  title: { text: 'Revenue Report' },
  xAxis: { categories: [] },
  yAxis: { title: { text: 'Revenue (VND)' }, labels: { formatter: function() { return Utils.formatValueKMB(this.value); } } },
  series: [{ name: 'Revenue', data: [], dataLabels: { enabled: true, formatter: function() { return Utils.formatValueKMB(this.y); } } }]
});

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

watch(selectedReport, fetchRevenueData);

onMounted(() => {
  fetchRevenueData();
})
</script>

<style scoped>
.box {
  padding: 1rem 1.5rem
}
</style>
