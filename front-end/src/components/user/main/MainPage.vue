<template>
  <Carousel />
  <div class="container py-5">
    <div class="d-flex">
      <div class="menu_banner">
        <a href="#">
          <img :src="banner" alt="">
        </a>
      </div>
      <ProductCard v-for="(item, index) in newestProducts" :key="index" :product="item"/>
    </div>
    <div v-if="discountProduct" class="mb-3">
      <h3 class="title">
        Sản phẩm khuyến mãi
      </h3>
      <div class="card-slider-container">
        <div class="card-slider">
          <div class="cards-wrapper">
            <ProductCard v-for="(item, index) in discountProduct" :key="index" :product="item"/>
          </div>
        </div>
      </div>
    </div>
    <div class="news-tab">
      <a v-for="(item, index) in tabs" :key="index" class="news-tab-text nav-link" :class="{ active: activeName === item.name }" @click="setActiveTab(item.name)">{{item.label}}</a>
    </div>
    <div class="card-slider-container pt-4">
      <div class="card-slider">
        <div class="cards-wrapper">
          <ProductCard v-for="(item, index) in products" :key="index" :product="item"/>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import banner from "@/assets/images/banner-newest.jpg";
import Carousel from "@/components/Carousel.vue";
import {onMounted, ref, watch} from "vue";
import axiosClient from '@/utils/axiosConfig';
import ProductCard from '@/components/user/main/ProductCard.vue';

const tabs = ref([
  { name: 'most-like', label: 'Yêu thích' },
  { name: 'best-seller', label: 'Bán chạy' },
  { name: 'highest-rate', label: 'Đánh giá' }
]);

const activeName = ref(tabs.value[0].name);
const type = ref(activeName.value);
const products = ref([]);
const newestProducts = ref([]);
const discountProduct = ref([]);

const setActiveTab = (tabName) => {
  activeName.value = tabName;
  type.value = tabName;
  fetchData()
}

const fetchData = async () => {
  try {
    const response = await axiosClient.get(`/products/${type.value}`);
    products.value = response.data;
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};

const getNewestProducts = async () => {
  const response = await axiosClient.get(`/products/newest`);
  newestProducts.value = response.data;
}

const getDiscountProducts = async () => {
  const response = await axiosClient.get(`/products/has-discount`);
  discountProduct.value = response.data;
}

onMounted(() => {
  getNewestProducts();
  fetchData();
  getDiscountProducts();
})

</script>

<style scoped>
.header-meta-list {
  display: flex;
  margin: 0;
  padding: 10px 0;
  justify-content: center;
}

.menu_banner {
  flex: 0 0 calc(50% - 30px);
  margin: 0 15px 40px 15px;
}

.menu_banner > a {
  display: block;
  padding-top: 65%;
  position: relative;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0px 4px 16px 0px #0000001F;
}

.menu_banner > a > img {
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  position: absolute;
}

.news-tab {
  align-items: center;
  background: #fff;
  border: 1px solid #ededed;
  border-radius: 100px;
  cursor: pointer;
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  margin: auto;
  overflow-x: auto;
  padding: 10px 30px;
}

.news-tab-text {
  color: rgba(38, 38, 38, .5);
  flex-shrink: 0;
  font-size: 12px;
  font-weight: 600;
  line-height: 30px;
  margin-right: 30px;
  padding: 0 10px;
  text-align: center;
  text-transform: uppercase;
}

.news-tab-text.active {
  align-items: center;
  background: radial-gradient(100% 488.28% at 0 100%, #e87800 0, #fa8c16 100%);
  border-radius: 100px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, .12);
  color: #fff;
  height: 32px;
}

.card-slider-container {
  position: relative;
}

.card-slider {
  overflow: hidden;
  width: 100%;
}

.cards-wrapper {
  display: flex;
  flex-wrap: wrap;
  transition: transform 0.3s ease-in-out;
}

.title {
  font-size: 24px;
  margin: 24px 0;
  padding-left: 12px;
  border-left: 4px solid var(--orange-5);
}

@media (min-width: 992px) {
  .news-tab {
    justify-content: center;
    width: 50%;
  }
}
</style>