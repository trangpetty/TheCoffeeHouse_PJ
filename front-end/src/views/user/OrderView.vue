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
          <li v-for="(item, index) in types" :key="index" class="category-item mx-2 mb-2 nav-item" @click="event => handleChangeType(item.id, event)">
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
                  <img :src="item.images[0].url" alt="" class="w-100 rounded">
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
          <el-dialog v-model="ui.dialogVisible" :show-close="false" width="30%" class="rounded custom-dialog">
            <template #header="{close}">
              <div class="d-flex align-items-center w-100 p-2">
                <h4 class="mx-auto my-0 fs-6">Thêm món mới</h4>
                <el-button class="border-0" @click="close">
                  <el-icon class="fs-3"><Close /></el-icon>
                </el-button>
              </div>
            </template>
            <div>
              <div class="card-product_detail d-flex flex-column">
                <img :src="selectedProduct?.images[0].url" alt="" class="w-100 rounded mb-3">
                <div class="d-flex flex-column px-1">
                  <h5 class="h5 text-dark" style="font-weight: 600;">{{selectedProduct.name}}</h5>
                  <p style="text-align: justify">{{selectedProduct.description}}</p>
                  <div class="d-flex justify-content-between mt-4">
                    <p>{{ formatPrice(selectedProduct?.price) }}</p>
                    <div class="card-product-quantity d-flex align-items-center">
                      <div class="btn add-to-cart d-flex align-items-center justify-content-center rounded-circle text-white" @click="quantity--" :class="quantity <= 1 ? 'disabled' :' btn--orange-1' ">
                        <font-awesome-icon icon="fa-solid fa-minus" />
                      </div>
                      <span class="my-1 text-center fs-6" style="width: 40px">{{quantity}}</span>
                      <div class="btn btn--orange-1 add-to-cart d-flex align-items-center justify-content-center rounded-circle text-white" @click="quantity++">
                        <font-awesome-icon icon="fa-solid fa-plus" />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <section class="card-product_note-item">
                <el-icon class="card-product-note-icon fs-5"><Tickets /></el-icon>
                <input class="card-product-text" placeholder="Ghi chú thêm cho món này" />
              </section>
              <section class="card-product_size">
                <div class="card-product-option">
                  <span class="card-product-option-text">CHỌN SIZE (BẮT BUỘC)</span>
                </div>
                <el-radio-group class="d-flex justify-content-between mt-3" v-model="selectedSize" @change="handleSelectSize">
                  <el-radio v-for="(item, index) in selectedProduct.productSizes" :key="index" :value="item">
                    <div class="d-flex flex-column fs-6">
                      <span>{{(item.size === 'S')? 'Nho' : (item.size === 'M')? 'Vua' : (item.size === 'L')? 'Lon' : ''}}</span>
                      <span>+ {{formatPrice(item.surcharge)}}</span>
                    </div>
                  </el-radio>
                </el-radio-group>
              </section>
              <section class="card-product_size">
                <div class="card-product-option">
                  <span class="card-product-option-text">CHỌN TOPPING (TÙY CHỌN)</span>
                </div>

              </section>
<!--              <div v-if="toppings.length">-->
<!--                <h5>Toppings</h5>-->
<!--                <div v-for="(topping, index) in toppings" :key="index">-->
<!--                  <input type="checkbox" :value="topping" v-model="selectedToppings">-->
<!--                  {{ topping.name }} (+{{ formatPrice(topping.price) }})-->
<!--                </div>-->
<!--              </div>-->
            </div>
            <template #footer>
              <button class="btn-add-item" @click="addToCart">{{formatPrice(cost = selectedProduct?.price * quantity + selectedSize.surcharge)}} - Thêm vào giỏ hàng</button>
            </template>
          </el-dialog>
        </div>
      </div>
    </div>
  </main>

</template>

<script lang="ts" setup>
import banner1 from '@/assets/images/banner1.webp'
import banner2 from '@/assets/images/banner2.webp'
import banner3 from '@/assets/images/banner3.webp'
import { ref } from 'vue'
import axios from "axios";
import {Close, Tickets} from "@element-plus/icons-vue";
import {useStore} from 'vuex'

const ui = ref({
  dialogVisible: false
})

const store = useStore();

const images = ref([banner1, banner2, banner3])
const types = ref([]);
const products = ref([]);
const activeId = ref(1);
const selectedProduct = ref(null);
const quantity = ref(1);
const selectedSize = ref({
  sizeId: null,
  size: '',
  surcharge: 0
});
const cost = ref(0)
const cart = ref([]);

const getTypes = async () => {
  const response = await axios.get('http://localhost:8082/api/product-type');
  types.value = response.data;
  const response2 = await axios.get(`http://localhost:8082/api/products/type/1`);
  products.value = response2.data;
  products.value.forEach(item => {
    console.log(item.images[0].url)
  })
};

const handleChangeType = async (id: number, event: Event) => {
  event.preventDefault();
  activeId.value = id;
  const response = await axios.get(`http://localhost:8082/api/products/type/${id}`);
  products.value = response.data;
}

const formatPrice = (price: number): string => {
  return price.toLocaleString('vi-VN') + 'đ';
};

const showProductModal = async (product) => {
  selectedProduct.value = product;
  quantity.value = 1; // Reset quantity
  selectedSize.value = selectedProduct.value.productSizes.reduce((max, item) => (item.surcharge > max.surcharge) ? item : max);
  // // Fetch toppings if needed (you can replace with actual API call)
  // // const response = await axios.get(`http://localhost:8082/api/toppings`);
  // // toppings.value = response.data;
  // toppings.value = [
  //   { id: 1, name: 'Topping 1', price: 5000 },
  //   { id: 2, name: 'Topping 2', price: 7000 }
  // ];
  // selectedToppings.value = [];
  ui.value.dialogVisible = true;
};

const addToCart = () => {
  const productWithDetails = {
    name: selectedProduct.value.name,
    productId: selectedProduct.value.id,
    productSize: selectedSize.value,
    cost: cost.value,
    quantity: quantity.value,
    // toppings: selectedToppings.value
  };
  cart.value.push(productWithDetails);
  localStorage.setItem('cart', JSON.stringify(cart.value));
  store.dispatch('addProductToCart', productWithDetails)
  ui.value.dialogVisible = false;
};

const handleSelectSize = () => {
  console.log(selectedSize.value)
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

.custom-dialog.el-overlay-dialog {
  overflow: hidden!important;
}

.custom-dialog.el-dialog {
  overflow-y: scroll;
  height: 90vh;
}

.custom-dialog.el-overlay-dialog {
  top: auto!important;
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
