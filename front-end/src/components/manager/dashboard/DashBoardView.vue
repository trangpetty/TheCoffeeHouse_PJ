<template>
  <div>
    <TodayStatistics/>
    <div class="d-flex flex-wrap mb-4">
      <RevenueChart/>
      <TopProductsChart/>
    </div>
    <div class="d-flex flex-wrap mb-4">
      <RevenueDailyChart/>
      <OrderStatusChart/>
    </div>
    <div class="d-flex flex-wrap mb-4">
      <AgeGroupChart/>
      <TopAccounts/>
    </div>
  </div>
</template>

<script setup lang="ts">
import TodayStatistics from "@/components/manager/dashboard/TodayStatistics.vue";
import RevenueChart from "@/components/manager/dashboard/RevenueChart.vue";
import TopProductsChart from "@/components/manager/dashboard/TopProductsChart.vue";
import RevenueDailyChart from "@/components/manager/dashboard/RevenueDailyChart.vue";
import OrderStatusChart from "@/components/manager/dashboard/OrderStatusChart.vue";
import TopAccounts from "@/components/manager/dashboard/TopAccounts.vue";
import AgeGroupChart from "@/components/manager/dashboard/AgeGroupChart.vue";

</script>

<style>
@media (min-width: 992px) {
  .chart {
    width: calc(50% - 20px)!important;
    float: left;
    margin: 0 10px;
  }
}

@media (max-width: 991px) {
  .chart {
    width: 100%;
  }
}
</style>
