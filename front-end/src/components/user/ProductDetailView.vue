<template>
  <div class="container container-fluid pt-5">
    <el-breadcrumb separator="/" class="mb-4">
      <el-breadcrumb-item :to="{ path: '/' }">Menu</el-breadcrumb-item>
      <el-breadcrumb-item>
        <a href="/">{{productType.name}}</a>
      </el-breadcrumb-item>
      <el-breadcrumb-item>{{product.name}}</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="row">
      <div class="col-lg-6 col-md-6 col-sm-12">
        <el-carousel ref="carouselRef" trigger="click" :autoplay="false" @change="handleChange">
          <el-carousel-item v-for="(image, index) in product.images" :key="index">
            <img :src="image.url" class="w-100"/>
          </el-carousel-item>
        </el-carousel>
        <div class="custom-indicators">
          <span
              v-for="(image, index) in product.images"
              :key="index"
              :class="['custom-indicator', { 'is-active': activeIndex === index }]"
              @click="setActiveItem(index)"
          >
            <img :src="image.url" class="indicator-img" />
          </span>
        </div>
      </div>
      <div class="col-lg-6 col-md-6 col-sm-12">
        <div>
          <p class="info_product_title">{{product.name}}</p>
          <div class="info_product_price">
            <div class="price">
              {{Utils.formatPrice(cost)}}
            </div>
          </div>
        </div>
        <div>
          <div class="option_title">Chọn size (bắt buộc)</div>
          <div class="product_options">
            <div class="opt_size">
              <div v-for="(item, index) in product.productSizes" :key="index"
                   :class="['product__info__item__list__item', { active: selectedSize === item }]"
                   @click="selectedSize = item"
              >
                <el-icon :style="getIconStyle(item.size)" class="me-1">
                  <Coffee class="w-100 h-100"/>
                </el-icon>
                <div>{{ getSizeLabel(item.size) }} + {{ Utils.formatPrice(item.surcharge) }}</div>
              </div>
            </div>
          </div>
        </div>
        <div v-if="product.toppings">
          <div class="option_title">Topping</div>
          <div class="product_options">
            <div class="opt_size">
              <label v-for="(item, index) in product.toppings" :key="index"
                     :class="['product__info__item__list__item my-2', { active: selectedTopping.toppingID === item.id }]"
                     @click="selectTopping(item)"
              >
                {{item.name}} + {{Utils.formatPrice(item.price)}}
              </label>
            </div>
          </div>
        </div>
        <div class="btn btn--orange-5 w-100 mt-4 fs-5 fw-medium py-2" @click="navigateToOrder">
          <font-awesome-icon icon="fa-solid fa-motorcycle" class="me-2" />
          <span>Đặt giao tận nơi</span>
        </div>
      </div>
    </div>
  </div>
  <div class="container py-4">
    <div class="row">
      <div class="col-lg-12 col-md-12 col-sm-12">
        <hr>
        <div>
          <h4 class="fs-4 fw-bold">Mô tả sản phẩm</h4>
          <p>{{product.description}}</p>
        </div>
        <hr>
        <div id="product-related">
          <h4 class="fs-4 fw-bold mb-3">Sản phẩm liên quan</h4>
          <div class="list_product_related buy_combo">
            <div class="menu_item" v-for="(item, index) in filteredProductRelated" :key="index">
              <div class="menu_item_image">
                <router-link :to="{ name: 'product', params: { id: item.id } }">
                  <img :src="item.images[0].url" alt="" class="w-100">
                </router-link>
              </div>
              <div class="menu_item_info">
                <h3>
                  <router-link :to="{ name: 'product', params: { id: item.id } }">{{item.name}}</router-link>
                  <div class="price_product_item mt-2">{{Utils.formatPrice(item.price)}}</div>
                </h3>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, defineProps, computed } from 'vue';
import axiosClient from '@/utils/axiosConfig';
import * as Utils from '@/utils';
import {Coffee} from "@element-plus/icons-vue";
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';

const store = useStore();
const router = useRouter();

const props = defineProps(['id']);
const product = ref({});
const productRelated = ref([]);
const productType = ref({});

const carouselRef = ref(null);
const activeIndex = ref(0);

const selectedSize = ref({
  id: null,
  size: '',
  surcharge: 0
});

const selectedTopping = ref({
  toppingID: null,
  name: '',
  quantity: 1,
  price: 0
});

onMounted(async () => {
  if (props.id) {
    try {
      const response = await axiosClient.get(`/products/${props.id}`);
      product.value = response.data;
    } catch (error) {
      console.error('Error fetching product detail:', error);
    }
  }
  if (product.value.typeID) {
    try {
      const response = await axiosClient.get(`/products/type/${product.value.typeID}?userID=`);
      productRelated.value = response.data;
      const response2 = await axiosClient.get(`/product-type/${product.value.typeID}?userID=`);
      productType.value = response2.data;
    } catch (error) {
      console.error('Error fetching product detail:', error);
    }
  }
});

const cost = computed(() => {
  let totalCost = product.value.price + selectedSize.value.surcharge;

  // Add the cost of the selected topping if any
  if (selectedTopping.value) {
    totalCost += selectedTopping.value.price;
  }

  return totalCost;
});

const selectTopping = (item) => {
  selectedTopping.value.toppingID = item.id;
  selectedTopping.value.name = item.name;
  selectedTopping.value.price = item.price;
  console.log(selectedTopping.value)
};

const filteredProductRelated = computed(() => productRelated.value.filter(item => item.id !== product.value.id));

const setActiveItem = (index: number) => {
  carouselRef.value.setActiveItem(index);
  activeIndex.value = index;
};

const handleChange = (index: number) => {
  activeIndex.value = index;
};

const getIconStyle = (size: string) => {
  switch (size) {
    case 'S':
      return { width: '12px', height: '16px' };
    case 'M':
      return { width: '15px', height: '20px' };
    case 'L':
      return { width: '19px', height: '24px' };
    default:
      return {};
  }
};

const getSizeLabel = (size: string) => {
  const labels: { [key: string]: string } = { S: 'Nho', M: 'Vua', L: 'Lon' };
  return labels[size] || size;
};

const navigateToOrder = () => {
  const selectedProduct = {
    name: product.value.name,
    id: product.value.id,
    cost: cost.value,
    quantity: 1,
    productSizes: product.value.productSizes,
    toppings: product.value.toppings,
    selectedSize: selectedSize.value,
    selectedTopping: selectedTopping.value,
    description: product.value.description,
    price: product.value.price,
    images: product.value.images
  };

  store.dispatch('setProductDialog', selectedProduct);
  router.push('/order');
};

</script>

<style scoped>
a {
  color: var(--black);
  text-decoration: none;
}

::v-deep .el-carousel__container {
  height: 570px!important;
}

::v-deep .el-carousel__indicators {
  display: none!important;
}

::v-deep .el-carousel{
  width: 570px;
}

.custom-indicators {
  display: flex;
  justify-content: start;
  padding: var(--space-10) 0;
}

.custom-indicator {
  cursor: pointer;
  margin: 0 5px;
  opacity: 0.5;
  border-radius: var(--space-8);
  transition: all 0.3s;
}

.custom-indicator.is-active {
  opacity: 1;
  border: 1px solid #E57905;
}

.indicator-img {
  width: 75px;
  height: 75px;
  border-radius: var(--space-8);
}

.info_product_title {
  font-size: 26px;
  margin-bottom: 18px;
  color: #000;
}

.info_product_price {
  font-size: 16px;
  color: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  line-height: 1;
  margin-top: 16px;
}

.info_product_price .price {
  line-height: 0.75;
  font-family: "SF Pro Display", sans-serif;
  font-size: 26px;
  color: #E57905;
  font-weight: 600;
  margin-right: 37px;
}

.option_title {
  font-weight: 400;
  font-size: 16px;
  line-height: 22px;
  margin-top: 40px;
  margin-bottom: 15px;
}

.product_options {
  display: flex;
  flex-wrap: wrap;
}

.product_options .opt_size {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.product_options .opt_size .product__info__item__list__item {
  transition: all 0.3s;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  padding: 0 20px;
  border-radius: 5px;
  border: 1px solid rgba(0, 0, 0, 0.15);
  font-size: 16px;
  height: 40px;
  line-height: 1;
  font-weight: 400;
  margin-right: 16px;
  font-size: 14px;
  color: #666666;
}

.product_options .option_item {
  display: block;
  position: relative;
  margin: 10px 20px 10px 0;
}

.product_options .opt_size .product__info__item__list__item.active {
  border-color: var(--orange-5);
  background: var(--orange-5);
  color: var(--white);
}

hr {
  margin: 2rem 0;
  border-top: 1px solid var(--smoky-gray-2);
}

#product-related .buy_combo {
  margin: 0 -15px -30px;
}

#product-related .buy_combo, #product-buy-combo .buy_combo {
  display: flex;
  flex-wrap: wrap;
}

#product-related .buy_combo .menu_item {
  flex: 0 0 calc(16.667% - 30px);
  margin: 0 15px 40px;
}

.menu_item {
  background: #fff;
  -ms-flex: 0 0 calc(33.333% - 30px);
  -webkit-flex: 0 0 calc(33.333% - 30px);
  flex: 0 0 calc(33.333% - 30px);
  margin: 0 15px 40px;
  min-height: 145px;
}

.menu_item_image {
  position: relative;
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
}

.menu_item .menu_item_info {
  padding: 12px 0 0;
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

@media (max-width: 767px) {
  #product-related .buy_combo .menu_item {
    flex: 0 0 calc(50% - 30px);
    margin-left: 15px;
    margin-right: 15px;
  }

  .menu_item {
    -ms-flex: 0 0 calc(50% - 16px);
    -webkit-flex: 0 0 calc(50% - 16px);
    flex: 0 0 calc(50% - 16px);
    margin: 0 8px 16px;
  }
}

@media (max-width: 991px) {
  #product-related .buy_combo .menu_item {
    flex: 0 0 calc(25% - 30px);
  }

  .menu_item {
    -ms-flex: 0 0 calc(50% - 30px);
    -webkit-flex: 0 0 calc(50% - 30px);
    flex: 0 0 calc(50% - 30px);
  }
}

</style>