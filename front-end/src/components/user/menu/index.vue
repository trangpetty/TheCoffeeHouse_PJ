<!-- Home.vue -->
<template>
  <div class="collection_menu_wrap">
    <div class="container">
      <div class="row">
        <div class="col-lg-3 col-md-3 col-sm-12 stikySidebar">
          <Sidebar @tab-change="updateCurrentTabLabel" />
        </div>
        <div class="col-lg-9 col-md-9 col-sm-12 border_right_before">
          <h3>{{ currentTabLabel }}</h3>
          <div class="menu_lists d-flex flex-wrap">
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
  </div>
</template>

<script lang="ts" setup>
import { ref, watch } from 'vue';
import Sidebar from '@/components/user/menu/Sidebar.vue';
import axiosClient from "@/utils/axiosConfig";
import * as Utils from "@/utils";
import { useRoute } from 'vue-router';

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

.menu_item {
  flex: 0 0 25%;
  box-sizing: border-box;
  margin: 0 15px 40px;
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

  .menu_item {
    flex: 0 0 calc(33.333% - 30px);
  }
}
</style>
