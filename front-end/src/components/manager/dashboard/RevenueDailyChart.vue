<template>
  <div class="box-shadow box chart">
    <div class="d-flex">
      <el-form-item label="Month" class="me-4">
        <el-select v-model="selectedMonth" style="width: 120px">
          <el-option v-for="month in Utils.months" :key="month.value" :label="month.label" :value="month.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Week">
        <el-select v-model="selectedWeek" style="width: 120px">
          <el-option v-for="week in Utils.weeks" :key="week" :label="'Week ' + week" :value="week"></el-option>
        </el-select>
      </el-form-item>
    </div>
    <Highcharts :options="dailyChartOptions"></Highcharts>
  </div>
</template>

<script setup lang="ts">
import Highcharts from 'vue3-highcharts';
import * as Utils from '@/utils/index';
import {onMounted, ref, watch} from "vue";
import axiosClient from "@/utils/axiosConfig";

const dailyChartOptions = ref({
  chart: { type: 'column' },
  title: { text: 'Weekly Revenue' },
  xAxis: { categories: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'] },
  yAxis: { title: { text: 'Revenue (VND)' }, labels: { formatter: function() { return Utils.formatValueKMB(this.value); } } },
  series: [{ name: 'Revenue', data: [0, 0, 0, 0, 0, 0, 0], dataLabels: { enabled: true, formatter: function() { return Utils.formatValueKMB(this.y); } } }]
});

const selectedMonth = ref(Utils.getCurrentMonth());
const selectedWeek = ref(Utils.getCurrentWeekInMonth());

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

watch([selectedMonth, selectedWeek], fetchDailyRevenueData);

onMounted(() => {
  fetchDailyRevenueData();
})
</script>

<style scoped>
.box {
  padding: 1rem 1.5rem
}
</style>