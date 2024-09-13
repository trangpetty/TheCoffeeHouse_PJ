<template>
  <div class="product-card p-2 m-2 box-shadow d-flex flex-lg-column">
    <div class="product-card_image">
      <img :src="product.image ? product.image : product.images[0].url" alt="" class="w-100 rounded">
    </div>
    <div class="d-flex flex-column pt-3 product-content product-content--mobile-padding">
      <div class="product-content__top">
        <div class="mb-1 mb-lg-3 product-content__title">
          <div class="mb-0">{{ product.name }}</div>
        </div>
      </div>
      <div class="d-flex align-items-center justify-content-between">
        <p class="mb-0 d-flex align-items-center">
          <span class="d-block me-2" :class="{ 'price-crossed': product.hasDiscount }" style="font-size: 0.875rem">{{ formatPrice(product.price) }}</span>
          <span class="d-block" style="font-size: 0.875rem" v-if="product.hasDiscount">{{ formatPrice(product.discountPrice) }}</span>
        </p>
        <div class="btn btn--orange-1 add-to-cart d-flex align-items-center justify-content-center rounded-circle text-white" @click="() => onAddToCart(product)">
          <font-awesome-icon icon="fa-solid fa-plus" />
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps<{
  product: {
    image?: string;
    images: { url: string }[];
    name: string;
    price: number;
    discountPrice?: number;
    hasDiscount?: boolean;
  };
}>();

const emit = defineEmits<{
  (e: 'add-to-cart', product: any): void;
}>();

const formatPrice = (price: number): string => {
  return price.toLocaleString('vi-VN') + 'đ';
};

const onAddToCart = (product: any) => {
  emit('add-to-cart', product);
};
</script>

<style scoped>
.product-card {
  background-color: var(--white);
  border-radius: var(--space-8);
  box-shadow: var(--product-box-shadow);
  cursor: pointer;
  margin-left: var(--space-8);
  padding: var(--space-8);
}

.product-card .product-content__top .product-content__title {
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

.product-card .product-content {
  flex: 1;
  padding-top: var(--space-8);
}

.add-to-cart {
  height: 32px;
  width: 32px;
}

.product-card_image {
  border-radius: var(--space-8);
  flex-shrink: 0;
}

.product-card .product-content__top {
  flex: 1;
  margin-top: calc(var(--space-2)* -1);
}

@media (max-width: 991px) {
  .product-card .product-content.product-content--mobile-padding {
    padding-left: var(--space-16);
    padding-top: 0;
  }

  .product-card_image {
    height: var(--space-100);
    width: var(--space-100);
  }
}
</style>
