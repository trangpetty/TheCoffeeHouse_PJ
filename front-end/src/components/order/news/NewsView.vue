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
.icon {
  color: #faa515;
  font-size: var(--space-24);
  line-height: 1;
  margin-right: var(--space-12);
}

.text-title {
  font-size: 2rem;
  font-weight: 600;
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

.fade-transform-leave-active {
  transition: opacity 0.5s, transform 1s;
}

.fade-transform-enter-active {
  transition: opacity .2s, transform 1s;
}

.fade-transform-enter {
  opacity: 0;
  transform: translateX(20%);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(-20%);
}

.custom-item {
  margin-top: 40px;
}

.blog-card {
  background-color: var(--white);
  border-radius: var(--space-8);
  box-shadow: var(--product-box-shadow);
  height: 100%;
  width: 100%;
}

.blog-card .blog-card__image {
  background-size: cover !important;
  border-top-left-radius: var(--space-8);
  border-top-right-radius: var(--space-8);
  flex-shrink: 0;
  height: 150px;
  padding-top: 53%;
  width: 100%;
}

.blog-card .blog-card__content {
  flex: 1;
  padding: var(--space-16);
  padding-top: var(--space-12);
}

.blog-card .blog-card__content .blog-card__content__header {
  flex: 1;
}

.blog-card .blog-card__content .blog-card__title {
  color: var(--black-3);
  display: -webkit-box;
  font-size: var(--space-16);
  height: calc(var(--space-24)* 2);
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  font-weight: 600;
  line-height: var(--space-24);
  overflow: hidden;
}

.btn .text {
  line-height: var(--space-20);
  font-size: var(--space-12);
  color: var(--white);
}

.btn .text:hover {
  text-decoration: underline;
}

@media (min-width: 992px) {
  .news-tab {
    justify-content: center;
    width: 50%;
  }
}

</style>
