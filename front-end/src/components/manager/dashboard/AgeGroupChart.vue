<template>
  <div class="box-shadow box chart">
    <h3>Phân loại nhóm tuổi người dùng</h3>
    <Highcharts :options="ageGroupChartOptions"></Highcharts>
  </div>
</template>

<script lang="ts" setup>
import Highcharts from 'vue3-highcharts';
import axiosClient from "@/utils/axiosConfig";
import {onMounted, ref} from "vue";

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

onMounted(() => {
  fetchAgeGroupData();
})
</script>

<style scoped>
.box {
  padding: 1rem 1.5rem
}
</style>