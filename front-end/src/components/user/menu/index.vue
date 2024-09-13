<!-- Home.vue -->
<template>
  <div class="collection_menu_wrap">
    <div class="container">
      <div class="row">
        <div class="col-lg-3 col-md-3 col-sm-12 stikySidebar">
          <Sidebar @tab-change="updateCurrentTabLabel" class="custom-sidebar"/>
        </div>
        <div class="col-lg-9 col-md-9 col-sm-12 border_right_before">
          <h3>{{ currentTabLabel }}</h3>
          <div class="menu_lists d-flex flex-wrap">
            <ProductCard v-for="(item, index) in products" :key="index" :product="item"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, watch } from 'vue';
import Sidebar from '@/components/user/menu/Sidebar.vue';
import axiosClient from "@/utils/axiosConfig";
import { useRoute } from 'vue-router';
import ProductCard from '@/components/user/main/ProductCard.vue';

const route = useRoute();

const currentTabLabel = ref('Tất cả');
const products = ref([]);

watch(
    () => route.params.tabName,
    async (newTabName) => {
      currentTabLabel.value = newTabName || 'Tất cả'; // Cập nhật tiêu đề tab
      const response = await axiosClient.get(`/products/type?name=${newTabName || ''}`);
      products.value = response.data;
    },
    { immediate: true }
);

const updateCurrentTabLabel = async (item) => {
  currentTabLabel.value = item.label;
  const response = await axiosClient.get(`/products/type?name=${item.type}`);
  products.value = response.data;
};
</script>

<style scoped>
.collection_menu_wrap {
  padding: 40px 0 50px;
}

.stikySidebar {
  top: 70px;
  position: sticky;
  left: 0;
  padding-right: 53px;
}

.menu_lists {
  margin-left: -15px;
  margin-right: -15px;
}

@media (min-width: 992px) {
  .stikySidebar + .border_right_before {
    width: 80.5%;
    padding-left: 72px;
  }

  .stikySidebar {
    width: 19.5%;
  }

  .border_right_before {
    border-left: 2px solid var(--orange-2);
  }
}

@media (max-width: 769px) {
  .custom-sidebar {
    display: none;
  }
}
</style>
