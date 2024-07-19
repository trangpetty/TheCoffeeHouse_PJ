<template>
  <Carousel />
  <div class="container py-5">
    <div class="d-flex">
      <div class="menu_banner">
        <a href="#">
          <img :src="banner" alt="">
        </a>
      </div>
      <div v-for="(item, index) in newestProducts" :key="index" class="menu_item">
        <div class="menu_item_image">
          <router-link :to="`/product/${item.id}`">
            <img :src="item.images.length > 0 ? item.images[0].url : ''" alt="">
          </router-link>
        </div>
        <div class="menu_item_info">
          <h3>{{item.name}}</h3>
          <div class="price_product_item">{{Utils.formatPrice(item.price)}}</div>
        </div>
      </div>
    </div>
    <div class="news-tab">
      <a v-for="(item, index) in tabs" :key="index" class="news-tab-text nav-link" :class="{ active: activeName === item.name }" @click="setActiveTab(item.name)">{{item.label}}</a>
    </div>
    <div class="card-slider-container pt-4">
      <div class="card-slider">
        <div class="cards-wrapper">
          <div v-for="(item, index) in products" :key="index" class="menu_item">
            <div class="menu_item_image">
              <router-link :to="`/product/${item.id}`">
                <img :src="item.images.length > 0 ? item.images[0].url : ''" alt="">
              </router-link>
            </div>
            <div class="menu_item_info">
              <h3>{{item.name}}</h3>
              <div class="price_product_item">{{Utils.formatPrice(item.price)}}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import banner from "@/assets/images/banner-newest.jpg";
import * as Utils from "@/utils";
import Carousel from "@/components/Carousel.vue";
import {ref, watch} from "vue";
import axiosClient from '@/utils/axiosConfig';

const tabs = ref([
  { name: 'most-like', label: 'Yêu thích' },
  { name: 'best-seller', label: 'Bán chạy' },
  { name: 'highest-rate', label: 'Đánh giá' }
]);

const activeName = ref(tabs.value[0].name);
const type = ref(activeName.value);
const products = ref([]);
const newestProducts = ref([]);

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

watch(type, fetchData, { immediate: true });
getNewestProducts();

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

.menu_item {
  flex: 0 0 25%;
  box-sizing: border-box;
  padding: 10px;
}

.menu_item .menu_item_image > a {
  border-radius: 10px;
  overflow: hidden;
  display: block;
  width: 100%;
  padding-top: 100%;
  position: relative;
  box-shadow: 0px 0px 13px 0px #00000040;
}

.menu_item .menu_item_image > a > img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
}

.menu_item .menu_item_info {
  padding: var(--space-20) 0 0;
}

.menu_item .menu_item_info h3 {
  margin-top: 0;
  margin-bottom: 4px;
  font-weight: 600;
  color: #191919;
  font-size: 16px;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
}

.menu_item .price_product_item {
  font-size: 14px;
  color: #00000099;
  margin-bottom: 10px;
}

@media (min-width: 992px) {
  .news-tab {
    justify-content: center;
    width: 50%;
  }
}
</style>