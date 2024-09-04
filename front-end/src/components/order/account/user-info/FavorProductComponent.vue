<template>
  <h1 class="row user-information">
    Sản phẩm yêu thích
  </h1>

  <div class="d-flex flex-wrap">
    <ProductCard v-for="(item, index) in products" :key="index" :product="item"/>
  </div>
</template>

<script lang="ts" setup>
import ProductCard from '@/components/user/main/ProductCard.vue';
import {computed, onMounted, ref} from "vue";
import store from "@/store";
import axiosClient from '@/utils/axiosConfig'

const user = computed(() => store.getters.user);

const products = ref([]);

const getAddresses = async () => {
  const response = await axiosClient.get(`/products/favor?userID=${user.value.id}`);
  products.value = response.data;
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