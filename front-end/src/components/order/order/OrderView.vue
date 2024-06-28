<template>
  <main>
    <div id="mainBanner" class="carousel slide" data-bs-ride="carousel" data-bs-interval="7000">
      <ul class="carousel-indicators" style="list-style-type: none;">
        <li v-for="index in images" :key="index" :data-bs-target="'#mainBanner'" :data-bs-slide-to="index" :class="{ 'active': index === 0 }"></li>
      </ul>
      <div class="carousel-inner">
        <div v-for="(item, index) in images" :key="index" :class="{ 'carousel-item': true, 'active': index === 0 }" style="height: 550px;">
          <div class="bounceIn">
            <img :src="item" class="d-block w-100" alt="...">
          </div>
        </div>
      </div>
      <a class="carousel-control-prev" href="#mainBanner" role="button" data-bs-slide="prev">
        <font-awesome-icon icon="fa-solid fa-chevron-left" class="fs-3 text-brown"/>
      </a>
      <a class="carousel-control-next" href="#mainBanner" role="button" data-bs-slide="next">
        <font-awesome-icon icon="fa-solid fa-chevron-right" class="fs-3 text-brown"/>
      </a>
    </div>
<!--    Main -->
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
                    <p class="mb-0">
                      <span class="d-block" style="font-size: 0.875rem">{{ formatPrice(item.price) }}</span>
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
              @close="closeDialog"
          />
        </div>
      </div>
    </div>
  </main>

</template>

<script lang="ts" setup>
import banner1 from '@/assets/images/banner1.webp'
import banner2 from '@/assets/images/banner2.webp'
import banner3 from '@/assets/images/banner3.webp'
import ProductDialog from '@/components/order/dialog/ProductDialog.vue';
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router';
import axios from "axios";
import {useStore} from 'vuex'
import { ElNotification } from 'element-plus'

const ui = ref({
  dialogVisible: computed(() => store.state.dialogProduct) || false
})

const store = useStore();

const images = ref([banner1, banner2, banner3])
const types = ref([]);
const products = ref([]);
const activeId = ref(1);
const selectedProduct = computed(() => store.state.selectedProduct) || ref({});

const getTypes = async () => {
  const response = await axios.get('http://localhost:8082/api/product-type');
  types.value = response.data;
  const response2 = await axios.get(`http://localhost:8082/api/products/type/1`);
  products.value = response2.data;
};

const handleChangeType = async (id: number, name: string, event: Event) => {
  event.preventDefault();
  activeId.value = id;
  if(name == 'Topping') {
    const response = await axios.get(`http://localhost:8082/api/topping`);
    products.value = response.data;
  }
  else {
    const response = await axios.get(`http://localhost:8082/api/products/type/${id}`);
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

getTypes();

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

.el-overlay-dialog {
  overflow: unset!important;
}

.custom-dialog.el-overlay-dialog {
  overflow: hidden!important;
}

.custom-dialog.el-dialog {
  overflow-y: scroll;
  height: 90vh;
}

.custom-dialog.el-dialog header {
  height: auto!important;
}

.custom-dialog.el-dialog__header {
  border-bottom: 1px solid #dee2e6;
}

.nav-link.active .category-image {
  background-color: var(--nude-2);
  background-size: cover;
  border: none;
}

.category-image {
  background-color: var(--nude-1);
  border-radius: 50%;
  height: 86px;
  width: 86px;
}

.category-image img {
  width: 45px;
  height: 45px;
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
