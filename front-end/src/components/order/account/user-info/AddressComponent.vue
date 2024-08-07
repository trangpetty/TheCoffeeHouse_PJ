<template>
  <h1 class="row user-information">
    Sổ địa chỉ
  </h1>

  <div class="card-cover" v-for="(item, index) in addresses" :key="index">
    <div class="cover-card-address">
      <div class="name mb-1">{{item.name}}</div>
      <div class="mb-1">
        <span class="title-data">Địa chỉ:</span>
        {{item.address}}
      </div>
      <div class="mb-1">
        <span class="title-data">Điện thoại:</span>
        {{user.phoneNumber}}
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {computed, onMounted, ref} from "vue";
import store from "@/store";
import axiosClient from '@/utils/axiosConfig'

const user = computed(() => store.getters.user);

const addresses = ref([]);

const getAddresses = async () => {
  const response = await axiosClient.get(`/users/address/${user.value.id}`);
  addresses.value = response.data;
}
onMounted(() => {
  getAddresses();
})
</script>

<style scoped>
.user-information {
  position: relative;
  text-align: left;
}

.user-information:before {
  border-bottom: 2px solid #fa8c16;
  bottom: -4px;
  content: "";
  left: 0;
  position: absolute;
  width: 30%;
}

.card-cover {
  margin: 6px 0;
  max-width: 450px;
  width: 100%;
  margin-left: -16px;
}

.cover-card-address {
  border: 1px solid #ededed;
  border-radius: 8px;
  box-sizing: border-box;
  padding: 16px;
  text-align: left;
  width: 100%;
}

.name {
  font-size: 16px;
  line-height: 24px;
  text-transform: uppercase;
}

.title-data {
  color: rgba(38, 38, 38, .5);
}
</style>