<template>
  <h1 class="row user-information">
    Sản phẩm yêu thích
  </h1>

  <div class="row mb-4 mb-lg-5">
    <div class="col-md-12 col-lg-3 mt-lg-3" v-for="(item, index) in products" :key="index">
      <ProductCard
          :product="item"
          @add-to-cart="showProductModal"
      />
    </div>
  </div>
<!--   Product Dialog-->
  <ProductDialog
      :selectedProduct="selectedProduct"
      :visible="ui.dialogVisible"
      :addCart="true"
      :userId="user.id"
      @close="closeDialog"
  />
</template>

<script lang="ts" setup>
import ProductCard from '@/components/order/order/ProductCardComponent.vue';
import ProductDialog from '@/components/order/dialog/ProductDialog.vue';
import {computed, onMounted, ref} from "vue";
import store from "@/store";
import axiosClient from '@/utils/axiosConfig';

const ui = ref({
  dialogVisible: computed(() => store.state.dialogProduct) || false
})

const user = computed(() => store.getters.user);
const selectedProduct = ref({});

const products = ref([]);

const getFavorProducts = async () => {
  const response = await axiosClient.get(`/products/favor?userID=${user.value.id}`);
  products.value = response.data;
}

onMounted(() => {
  getFavorProducts();
})

const showProductModal = async (product) => {
  selectedProduct.value = product;
  store.dispatch('setProductDialog', product);
};

const closeDialog = () => {
  store.dispatch('closeProductDialog');
};
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