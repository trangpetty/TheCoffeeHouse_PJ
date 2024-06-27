<template>
  <div class="w-50">
    <Highcharts :options="chartOptions"></Highcharts>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import Highcharts from 'vue3-highcharts';
import axios from 'axios';

const chartOptions = ref({
  chart: {
    type: 'line'
  },
  title: {
    text: 'Monthly Revenue'
  },
  xAxis: {
    categories: [] // Initialize empty categories
  },
  yAxis: {
    title: {
      text: 'Revenue (VND)'
    },
    labels: {
      formatter: function() {
        return formatValueKMB(this.value); // Use the common formatting function for yAxis labels
      }
    }
  },
  series: [{
    name: 'Revenue',
    data: [], // Initialize empty data array
    dataLabels: {
      enabled: true, // Enable data labels
      formatter: function() {
        return formatValueKMB(this.y); // Use the common formatting function for data labels
      }
    }
  }]
});

const fetchMonthlyRevenue = async () => {
  try {
    const response = await axios.get('http://localhost:8082/api/bills/revenue');
    const monthlyData = response.data;

    // Assuming your API returns an array of revenues for each month in correct order
    const monthNames = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

    // Populate categories with month names
    chartOptions.value.xAxis.categories = monthNames.slice(0, monthlyData.length);

    // Update series data with fetched revenues
    chartOptions.value.series[0].data = monthlyData;

    // Render the chart with updated options
    renderChart();
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};

const renderChart = () => {
  // Use chart ref to update chart options
  const chartRef = Highcharts.instance;

  if (chartRef) {
    chartRef.update(chartOptions.value, true);
  }
};

const formatValueKMB = (value: number) => {
  if (value >= 1000000) {
    return (value / 1000000).toFixed(1) + 'M';
  } else if (value >= 1000) {
    return (value / 1000).toFixed(1) + 'K';
  } else {
    return value.toString();
  }
};

// Initialize chart after component is mounted
onMounted(() => {
  fetchMonthlyRevenue();
});
</script>

<style scoped>
/* Add custom styles if needed */
</style>
