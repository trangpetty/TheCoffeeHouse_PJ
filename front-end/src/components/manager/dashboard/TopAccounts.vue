<template>
  <div class="box-shadow box chart">
    <div class="mb-3">
      <h3>Top 5 tài khoản mua nhiều nhất</h3>
      <div v-for="user in usersBuy" :key="user.user.id" class="p-2 mb-2 shadow-sm">
        <div class="d-flex">
          <h5 class="user-title">{{ user.user.firstName }} {{ user.user.lastName }}</h5>
          <div class="progress">
            <div
                class="progress-bar bg-success"
                :style="{ width: `${user.successPercentage}%` }"
                role="progressbar"
                aria-label="Success"
                :aria-valuenow="user.successPercentage"
                aria-valuemin="0"
                aria-valuemax="100"
            >
              {{ user.successPercentage.toFixed(2) }}% Success
            </div>
            <div
                class="progress-bar bg-warning"
                :style="{ width: `${user.cancelPercentage}%` }"
                role="progressbar"
                aria-label="Cancel"
                :aria-valuenow="user.cancelPercentage"
                aria-valuemin="0"
                aria-valuemax="100"
            >
              {{ user.cancelPercentage.toFixed(2) }}% Cancel
            </div>
            <div
                class="progress-bar bg-danger"
                :style="{ width: `${user.failPercentage}%` }"
                role="progressbar"
                aria-label="Fail"
                :aria-valuenow="user.failPercentage"
                aria-valuemin="0"
                aria-valuemax="100"
            >
              {{ user.failPercentage.toFixed(2) }}% Fail
            </div>
          </div>
        </div>
      </div>
    </div>
    <div>
      <h3>Top 5 tài khoản hủy nhiều nhất</h3>
      <div v-for="user in usersCancel" :key="user.user.id" class="p-2 mb-2 shadow-sm">
        <div class="d-flex">
          <h5 class="user-title">{{ user.user.firstName }} {{ user.user.lastName }}</h5>
          <div class="progress">
            <div
                class="progress-bar bg-success"
                :style="{ width: `${user.successPercentage}%` }"
                role="progressbar"
                aria-label="Success"
                :aria-valuenow="user.successPercentage"
                aria-valuemin="0"
                aria-valuemax="100"
            >
              {{ user.successPercentage.toFixed(2) }}% Success
            </div>
            <div
                class="progress-bar bg-warning"
                :style="{ width: `${user.cancelPercentage}%` }"
                role="progressbar"
                aria-label="Cancel"
                :aria-valuenow="user.cancelPercentage"
                aria-valuemin="0"
                aria-valuemax="100"
            >
              {{ user.cancelPercentage.toFixed(2) }}% Cancel
            </div>
            <div
                class="progress-bar bg-danger"
                :style="{ width: `${user.failPercentage}%` }"
                role="progressbar"
                aria-label="Fail"
                :aria-valuenow="user.failPercentage"
                aria-valuemin="0"
                aria-valuemax="100"
            >
              {{ user.failPercentage.toFixed(2) }}% Fail
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import axiosClient from "@/utils/axiosConfig";

const usersBuy = ref([]);
const usersCancel = ref([]);

const calculatePercentages = (user) => {
  const totalOrders = user.totalOrders || 1;
  return {
    successPercentage: ((user.success || 0) / totalOrders) * 100,
    cancelPercentage: ((user.cancel || 0) / totalOrders) * 100,
    failPercentage: ((user.fail || 0) / totalOrders) * 100
  };
};

const fetchTopUsers = async () => {
  try {
    const response = await axiosClient.get('/users/top-users-summary');
    if (response.data && Array.isArray(response.data.topUsersBuy)) {
      usersBuy.value = response.data.topUsersBuy.map(user => {
        return {
          ...user,
          ...calculatePercentages(user)
        };
      }).sort((a, b) => b.successPercentage - a.successPercentage);

      usersCancel.value = response.data.topUsersCancel.map(user => {
        return {
          ...user,
          ...calculatePercentages(user)
        };
      }).sort((a, b) => b.cancelPercentage - a.cancelPercentage);
    } else {
      console.error('Data format is incorrect:', response.data);
    }
  } catch (error) {
    console.error('Error fetching age group data:', error);
  }
}

onMounted(() => {
  fetchTopUsers();
})
</script>

<style scoped>
.box {
  padding: 1rem 1.5rem
}

::v-deep .el-card__body {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.progress {
  width: 100%;
  height: 30px;
}

.progress-bar {
  line-height: 30px;
  color: white;
  text-align: center;
}

.user-title {
  width: 180px;
}

</style>
