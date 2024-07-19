<template>
  <div class="card-history-item">

  </div>

</template>

<script lang="ts" setup>
import {computed, onMounted, ref} from "vue";
import store from "@/store";
import axiosClient from '@/utils/axiosConfig'

const user = computed(() => store.getters.user);

const bills = ref([]);

const getBills = async () => {
  const response = await axiosClient.get(`/users/bills/${user.value.id}`);
  bills.value = response.data;
}
onMounted(() => {
  getBills();
})
</script>

<style scoped>
.card-history-item {
  background: #fff;
  border: 1px solid #ededed;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  margin-top: 16px;
  padding: 12px 17px;
}
</style>