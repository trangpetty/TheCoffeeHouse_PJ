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
            <div class="price" :class="{ 'price-crossed': product.hasDiscount }">
              {{ Utils.formatPrice(product.price) }}
            </div>
            <div v-if="product.hasDiscount" class="price">
              {{ Utils.formatPrice(product.discountPrice) }}
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
            <ProductCard v-for="(item, index) in filteredProductRelated" :key="index" :product="item"/>
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
import ProductCard from '@/components/user/main/ProductCard.vue';

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
  const labels: { [key: string]: string } = { S: 'Nhỏ', M: 'Vừa', L: 'Lớn' };
  return labels[size] || size;
};

const navigateToOrder = () => {
  const selectedProduct = {
    name: product.value.name,
    id: product.value.id,
    hasDiscount: product.value.hasDiscount,
    discountPrice: product.value.discountPrice,
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
  margin-right: var(--space-16);
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

@media (max-width: 767px) {
  ::v-deep .el-carousel__item img {
    height: 360px !important;
    object-fit: contain;
  }

  ::v-deep .el-carousel, .el-carousel__container {
    height: 360px;
    width: 100%;
  }
}

</style>