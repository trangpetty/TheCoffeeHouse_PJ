<template>
  <div class="menu_item">
    <div class="menu_item_image">
      <router-link :to="`/product/${props.product.id}`">
        <img :src="props.product.images.length > 0 ? props.product.images[0].url : ''" alt="">
      </router-link>
    </div>
    <div class="menu_item_info">
      <h3>{{props.product.name}}</h3>
      <p class="mb-0 d-flex align-items-center price_product_item">
        <span class="d-block me-2" :class="{ 'price-crossed': props.product.hasDiscount }" style="font-size: 0.875rem">{{ Utils.formatPrice(props.product.price) }}</span>
        <span class="d-block" style="font-size: 0.875rem" v-if="props.product.hasDiscount">{{ Utils.formatPrice(props.product.discountPrice) }}</span>
      </p>
    </div>
  </div>
</template>

<script lang="ts" setup>
import * as Utils from "@/utils";
import { ref, defineProps } from 'vue';

interface Product {
  name: string;
  id: number;
  productSizes: any[];
  cost: number;
  quantity: number;
  toppings: any[];
  price: number;
  description: string;
  images: any[];
  hasDiscount: boolean;
  discountPrice: number;
}

const props = defineProps<{ product: Product }>();

</script>

<style scoped>

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

@media (max-width: 767px) {
  .menu_item {
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
  .menu_item {
    flex: 0 0 calc(25% - 30px);
  }

  .menu_item {
    -ms-flex: 0 0 calc(50% - 30px);
    -webkit-flex: 0 0 calc(50% - 30px);
    flex: 0 0 calc(50% - 30px);
  }
}
</style>