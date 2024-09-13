<template>
  <div class="py-5 container-lg container-fluid">
    <!-- Header -->
    <div class="mb-4">
      <div class="d-flex justify-content-center align-items-center">
        <font-awesome-icon icon="fa-solid fa-newspaper" class="icon"/>
        <span class="text-title">Tin tức mới nhất</span>
      </div>
    </div>
    <!-- Body -->
    <div>
      <div class="news-tab">
        <a v-for="(item, index) in tabs" :key="index" class="news-tab-text nav-link" :class="{ active: activeName === item.name }" @click="setActiveTab(item.name)">{{ item.label }}</a>
      </div>
      <transition name="fade-transform" mode="out-in">
        <div class="w-100" :key="activeName">
          <div>
            <div class="row">
              <div v-for="(item, index) in news" :key="index" class="col-12 col-sm-6 col-lg-3 mb-3 custom-item">
                <BlogCard :image="item.image" :title="item.title" :id="item.id" />
              </div>
            </div>
          </div>
        </div>
      </transition>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import axiosClient from "@/utils/axiosConfig";
import BlogCard from '@/components/order/news/BlogCardComponent.vue';

const tabs = ref([
  {name: '#CoffeeLover', label: '#CoffeeLover'},
  {name: 'Cập nhật từ Petty', label: 'Cập nhật từ Petty'},
  {name: 'Ưu đãi đặc biệt', label: 'Ưu đãi đặc biệt'}
]);

const activeName = ref(tabs.value[2].name);
const type = ref(activeName.value);
const news = ref([]);

const setActiveTab = (tabName) => {
  activeName.value = tabName;
  type.value = tabName;
  fetchData();
};

const fetchData = async () => {
  try {
    const response = await axiosClient.get(`/news/all`, {
      params: { type: type.value }
    });
    news.value = response.data;
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};

fetchData();
</script>

<style scoped>
/* Styles remain the same */
</style>
