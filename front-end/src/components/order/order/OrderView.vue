<template>
  <main>
    <Carousel />
    <!-- Products -->
    <div class="box">
      <div class="container-fluid container-lg">
        <div class="box-title d-flex justify-content-center align-items-center">
          <span class="icon"><font-awesome-icon icon="fa-solid fa-trophy" /></span>
          <span class="text">Sản phẩm từ Nhà</span>
        </div>
        <!--   Type of Drink     -->
        <ul class="category-list d-flex flex-wrap justify-content-center">
          <li v-for="(item, index) in types" :key="index" class="category-item mx-2 mb-2 nav-item" @click="event => handleChangeType(item.id, item.name, event)">
            <a href="#" class="nav-link m-0 border-0 py-2 px-3" :class="{ active: activeId === item.id }">
              <div class="d-flex flex-column">
                <div class="category-image d-flex align-items-center justify-content-center">
                  <img :src="item.image" alt="">
                </div>
                <div class="mt-2">
                  <h5 class="category-text text-center">{{ item.name }}</h5>
                </div>
              </div>
            </a>
          </li>
        </ul>

        <!--    List Product    -->
        <div>
          <div class="row mb-4 mb-lg-5">
            <div class="col-12 col-md-6 col-xl-2 col-lg-3 mt-lg-3" v-for="(item, index) in products" :key="index">
              <div class="product-card p-2 m-2 box-shadow d-flex flex-lg-column">
                <div class="product-card_image h-auto">
                  <img :src="item.image ? item.image : item.images[0].url" alt="" class="w-100 rounded">
                </div>
                <div class="d-flex flex-column pt-3">
                  <div class="mb-1 mb-lg-3 product-title_name">
                    <div class="mb-0">{{ item.name }}</div>
                  </div>
                  <div class="d-flex align-items-center justify-content-between">
                    <p class="mb-0 d-flex align-items-center">
                      <span class="d-block me-2" :class="{ 'price-crossed': item.hasDiscount }" style="font-size: 0.875rem">{{ formatPrice(item.price) }}</span>
                      <span class="d-block" style="font-size: 0.875rem" v-if="item.hasDiscount">{{ formatPrice(item.discountPrice) }}</span>
                    </p>
                    <div class="btn btn--orange-1 add-to-cart d-flex align-items-center justify-content-center rounded-circle text-white" @click="() => showProductModal(item)">
                      <font-awesome-icon icon="fa-solid fa-plus" />
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- Product Dialog -->
          <ProductDialog
              :selectedProduct="selectedProduct"
              :visible="ui.dialogVisible"
              :addCart="true"
              :userId="user.id"
              @close="closeDialog"
          />
        </div>
      </div>
    </div>

    <!-- News -->
    <div class="box">
      <div class="container-fluid container-lg">
        <div class="box-title d-flex justify-content-center align-items-center">
          <span class="icon"><font-awesome-icon icon="fa-solid fa-newspaper"/></span>
          <span class="text">Tin Tức</span>
        </div>
        <div class="row">
          <div v-for="(item, index) in news" :key="index" class="col-12 col-sm-6 col-lg-3 mb-3 custom-item">
            <div class="blog-card d-flex flex-column">
              <div class="blog-card__image" :style="{ backgroundImage: `url('${item.image}')` }"></div>
              <div class="blog-card__content d-flex flex-column">
                <div class="blog-card__content__header d-flex flex-column">
                  <h5 class="blog-card__title text-uppercase">{{item.title}}</h5>
                </div>
                <div class="mt-auto">
                  <router-link :to="{ name: 'blog-detail', params: { id: item.id } }" class="btn btn--orange-1 btn--radius-100 d-inline-block align-items-center float-end">
                    <span class="text">ĐỌC TIẾP</span>
                  </router-link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>

</template>

<script lang="ts" setup>
import ProductDialog from '@/components/order/dialog/ProductDialog.vue';
import { ref, computed, onMounted } from 'vue'
import axiosClient from '@/utils/axiosConfig';
import {useStore} from 'vuex';
import Carousel from '@/components/Carousel.vue'

const ui = ref({
  dialogVisible: computed(() => store.state.dialogProduct) || false
})

const user = computed(() => store.getters.user);

const store = useStore();

const types = ref([]);
const products = ref([]);
const news = ref([]);
const activeId = ref(1);
const selectedProduct = computed(() => store.state.selectedProduct) || ref({});

const getTypes = async () => {
  const response = await axiosClient.get('/product-type');
  types.value = response.data;
  const response2 = await axiosClient.get(`/products/type/1?userID=`);
  products.value = response2.data;
};

const handleChangeType = async (id: number, name: string, event: Event) => {
  event.preventDefault();
  activeId.value = id;
  if(name == 'Topping') {
    const response = await axiosClient.get(`/topping`);
    products.value = response.data;
    console.log("topping: ", products.value)
  }
  else {
    const response = await axiosClient.get(`/products/type/${id}?userID=`);
    products.value = response.data;
  }
}

const formatPrice = (price: number): string => {
  return price.toLocaleString('vi-VN') + 'đ';
};

const showProductModal = async (product) => {
  // selectedProduct.value = product;
  store.dispatch('setProductDialog', product);
};

const closeDialog = () => {
  store.dispatch('closeProductDialog');
};

onMounted(async () => {
  getTypes();
  const response = await axiosClient.get(`/news/newest`);
  news.value = response.data;
})

</script>

<style>
.dropdown:hover .dropdown-menu {
      display: block;
    }
.box {
  padding: 3.25rem 0;
}

.box-title {
  margin-bottom: 1.875rem;
}

.box .box-title .text {
  color: #000;
  font-size: 1.75rem;
  font-weight: 600;
  line-height: 1.5rem;
}

.box .box-title .icon {
  color: #faa515;
  font-size: 1.5rem;
  line-height: 1;
  margin-right: 0.75rem;
}

.category-list {
  list-style-type: none;
}

.category-item {
  cursor: pointer;
}

.nav-link.active .category-image {
  background-color: var(--nude-2);
  background-size: cover;
  border: none;
}

.category-image {
  background-color: var(--nude-1);
  border-radius: 50%;
  height: 120px;
  width: 120px;
}

.category-image img {
  width: 100px;
  height: 100px;
}

.category-text {
  font-size: 12px;
  color: var(--smoky-gray-2);
  font-weight: 600;
}

.product-card {
  background-color: var(--white);
  border-radius: 0.5rem;
  cursor: pointer;
}

.product-title_name {
  color: var(--black);
  display: -webkit-box;
  font-size: 0.875rem;
  height: 3rem;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  line-height: 1.5rem;
  overflow: hidden;
}

.add-to-cart {
  height: 32px;
  width: 32px;
}

.el-dialog__body {
  padding: 1rem;
}

.card-product_note-item {
  background: var(--white-1);
  border: 1px solid var(--smoky-gray-4);
  border-radius: var(--space-4);
  display: flex;
  height: var(--space-44);
  margin-top: var(--space-20);
}

.card-product-note-icon {
  height: 20px;
  margin: 12px 11px;
  width: 18px;
}

.card-product-text {
  border: none;
  flex: 1;
  font-size: var(--space-16);
  height: 100%;
  outline: none;
  padding: 0 var(--space-16);
  width: 80%;
  width: 12.5rem;
}

.card-product-option {
  background-color: var(--smoky-gray-5);
  margin-left: -16px;
  margin-top: var(--space-16);
  padding: var(--space-8) var(--space-6) var(--space-8);
  width: 106.5%;
}

.card-product-option-text {
  color: #666;
  font-size: var(--space-12);
  font-weight: 600;
  line-height: var(--space-24);
  margin: var(--space-6) var(--space-16);
  text-transform: uppercase;
}

.card-product-option-topping:first-child {
  border-top: none;
}

.card-product-option-topping {
  display: flex;
  align-items: center;
  border-bottom: none;
  border-top: 1px solid #c4c4c4;
  height: var(--space-70);
  justify-content: space-between;
  margin: 0 var(--space-15);
  padding: var(--space-15) 0;
}

.card-product-option-topping-name {
  font-size: var(--space-16);
}

.card-product-option-topping-price {
  color: #262626;
  font-size: var(--space-14);
  font-weight: 600;
}

.quantity-extra {
  background: #fff;
  border: 2px solid #e4e4e4;
  border-radius: 50%;
  box-sizing: border-box;
  height: var(--space-24);
  width: var(--space-24);
  color: #e4e4e4;
}

.btn-add-item {
  background: radial-gradient(75.9% 488.28% at 24.1% 100%, #e87800 0, #fa8c16 100%);
  border: none;
  border-radius: var(--space-100);
  box-shadow: 0 var(--space-2) var(--space-8) rgba(0, 0, 0, .12);
  color: var(--white);
  flex: 1;
  font-size: var(--space-16);
  height: var(--space-48);
  line-height: var(--space-20);
  margin-left: 0;
  outline: none;
  text-align: center;
  width: 100%;
}

.btn.disabled {
  background-color: #e4e4e4!important;
}

.el-dialog__footer {
  padding: var(--space-12);
}

.el-notification {
  align-items: center!important;
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

@media(min-width:768px)
{
   .carousel-caption {
        left: 50%;
      top: 55%;
      transform: translate(-50%, -50%);
  }
}

@media(max-width:768px)
  .carousel {
    height: 240px;
  }
{
}

@media (prefers-reduced-motion: reduce) {
  .carousel-item {
      transition: transform .6s ease-in-out!important;
  }
}

</style>
