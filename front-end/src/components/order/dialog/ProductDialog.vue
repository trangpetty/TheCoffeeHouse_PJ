<template>
  <el-dialog v-model="props.visible" :show-close="false" width="30%" class="rounded custom-dialog">
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
        <img :src="image" alt="" class="w-100 rounded mb-3">
        <div class="d-flex flex-column px-1">
          <div class="d-flex align-items-center justify-content-between mb-2">
            <h5 class="h5 text-dark mb-0" style="font-weight: 600;">{{props.selectedProduct.name}}</h5>
            <font-awesome-icon
                :icon="[props.selectedProduct.liked ? 'fas' : 'far', 'heart']"
                class="text-orange fs-4 cursor-pointer"
                @click="toggleLike"
            />
          </div>
          <p style="text-align: justify" class="m-0">{{props.selectedProduct.description}}</p>
          <div class="d-flex justify-content-between align-items-center mt-2">
            <p class="mb-0 fs-5 fw-bold">{{ Utils.formatPrice(props.selectedProduct.price) }}</p>
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
        <el-radio-group class="d-flex justify-content-between mt-3" v-model="selectedSize">
          <el-radio v-for="(item, index) in props.selectedProduct.productSizes" :key="index" :value="item">
            <div class="d-flex flex-column fs-6">
              <span>{{(item.size === 'S')? 'Nho' : (item.size === 'M')? 'Vua' : (item.size === 'L')? 'Lon' : ''}}</span>
              <span>+ {{Utils.formatPrice(item.surcharge)}}</span>
            </div>
          </el-radio>
        </el-radio-group>
      </section>
      <section class="card-product_size">
        <div class="card-product-option">
          <span class="card-product-option-text">CHỌN TOPPING (TÙY CHỌN)</span>
        </div>
      </section>
      <div v-if="props.selectedProduct.toppings.length">
        <div v-for="(topping, index) in props.selectedProduct.toppings" :key="index" class="card-product-option-topping">
          <div class="d-flex flex-column">
            <span class="card-product-option-topping-name">{{ topping.name }}</span>
            <span class="card-product-option-topping-price">+{{ Utils.formatPrice(topping.price) }}</span>
          </div>
          <div class="d-flex align-items-center">
            <div v-if="getToppingQuantity(topping) > 0" class="quantity-extra d-flex align-items-center justify-content-center cursor-pointer" @click="decreaseQuantity(topping)"><font-awesome-icon icon="fa-solid fa-minus" /></div>
            <span class="my-1 text-center fs-6" style="width: 40px">{{getToppingQuantity(topping)}}</span>
            <div class="quantity-extra d-flex align-items-center justify-content-center cursor-pointer" @click="increaseQuantity(topping)"><font-awesome-icon icon="fa-solid fa-plus" /></div>
          </div>
        </div>
      </div>
    </div>
    <template #footer>
      <button class="btn-add-item" @click="addToCart">{{Utils.formatPrice(cost)}} - {{ props.addCart ? "Thêm vào" : "Thay đổi"}} giỏ hàng</button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed, defineProps, defineEmits, watch } from 'vue';
import { ElNotification } from 'element-plus';
import { useStore } from 'vuex';
import * as Utils from '@/utils/index'
import axios from "axios";

const props = defineProps(['selectedProduct', 'visible', 'addCart', 'index', 'userId']);
const emit = defineEmits(['updateProduct']);

const store = useStore();

const quantity = ref(1);
const selectedSize = ref({
  id: null,
  size: '',
  surcharge: 0
});

const selectedTopping = ref({
  toppingID: null,
  name: '',
  quantity: 0,
  price: 0
});

const image = ref([]);

const cart = ref([]);

const voucher = computed(() => store.getters.voucher);
const cartItems = computed(() => store.getters.cartItems);

// Function to handle increasing topping quantity
const increaseQuantity = (topping) => {
  if (selectedTopping.value.toppingID === topping.id) {
    if (selectedTopping.value.quantity / quantity.value < 2) {
      selectedTopping.value.quantity++;
    } else {
      console.log("Maximum topping quantity reached.");
    }
  } else {
    selectedTopping.value = {
      toppingID: topping.id,
      name: topping.name,
      price: topping.price,
      quantity: 1
    };
  }
};

// Function to handle decreasing topping quantity
const decreaseQuantity = (topping) => {
  if (selectedTopping.value.toppingID === topping.id && selectedTopping.value.quantity > 0) {
    selectedTopping.value.quantity--;
  }
};

// Function to get the current quantity of a topping
const getToppingQuantity = (topping) => {
  return selectedTopping.value.toppingID === topping.id ? selectedTopping.value.quantity : 0;
};

const cost = computed(() => {
  let totalCost = props.selectedProduct.price * quantity.value;
  if(selectedSize.value.surcharge) {
    totalCost += selectedSize.value.surcharge;
  }

  // Add the cost of the selected topping if any
  if (selectedTopping.value.quantity > 0) {
    totalCost += selectedTopping.value.price * selectedTopping.value.quantity;
  }

  return totalCost;
});

const addToCart = async () => {
  const isAuthenticated = await Utils.checkTokenAndNotify('Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng.');

  if (!isAuthenticated) {
    return; // Exit function if not authenticated
  }
  if (props.selectedProduct) {
    const productWithDetails = {
      name: props.selectedProduct.name,
      id: props.selectedProduct.id,
      cost: cost.value,
      quantity: quantity.value,
      productSizes: props.selectedProduct.productSizes,
      toppings: props.selectedProduct.toppings,
      selectedSize: selectedSize.value,
      selectedTopping: selectedTopping.value.quantity > 0 ? selectedTopping.value : {
        toppingID: null,
        name: '',
        quantity: 0,
        price: 0
      },
      description: props.selectedProduct.description,
      price: props.selectedProduct.price,
      images: props.selectedProduct.images
    };

    if (props.addCart) {
      store.dispatch('addProductToCart', productWithDetails);
    } else {
      store.dispatch('updateProductInCart', { product: productWithDetails, index: props.index });
    }

    emit('close');

    ElNotification({
      title: 'Thành công',
      message: 'Thêm sản phẩm thành công',
      type: 'success',
      showClose: false,
      offset: 100
    });

    const totalCost = cartItems.value.reduce((total, item) => total + item.cost, 0);
    const totalQuantity = cartItems.value.reduce((total, item) => total + item.quantity, 0);

    if (totalCost > voucher.value.minimumOrderValue && totalQuantity >= voucher.value.minimumItems) {
      store.dispatch('clearErrorMessage');
    } else {
      store.dispatch('setErrorMessage', voucher.value.errorMessage);
    }

    // Reset selectedTopping after adding to cart
    selectedTopping.value = {
      toppingID: null,
      name: '',
      quantity: 0
    };
  }
};

const toggleLike = async () => {
  const isAuthenticated = await Utils.checkTokenAndNotify('Vui lòng đăng nhập để thêm sản phẩm vào mục yêu thích.');

  if (!isAuthenticated) {
    return; // Exit function if not authenticated
  }
  try {
    const response = await axios.post('http://localhost:8082/api/products/like', {
      userId: props.userId,
      productId: props.selectedProduct.id
    });

    props.selectedProduct.liked = !props.selectedProduct.liked;
    emit('updateProduct', props.selectedProduct);

    ElNotification({
      title: 'Thành công',
      message: 'Đã cập nhật mục yêu thích',
      type: 'success',
      showClose: false,
      offset: 100
    });
  } catch (error) {
    console.error('Failed to like/unlike product:', error.message);
    // Optional: Handle errors, display error messages, etc.
    if (error.response) {
      console.error('Response status:', error.response.status);
      console.error('Response data:', error.response.data);
    }
  }
}

watch(() => props.selectedProduct, () => {
  // Initialize selected size
  if (props.selectedProduct && props.selectedProduct.selectedSize) {
    selectedSize.value = props.selectedProduct.selectedSize;
  } else if (props.selectedProduct && props.selectedProduct.productSizes) {
    selectedSize.value = props.selectedProduct.productSizes.reduce((max, item) => {
      return item.surcharge > max.surcharge ? item : max;
    });
  }

  // Initialize selected topping
  if (props.selectedProduct && props.selectedProduct.selectedTopping) {
    selectedTopping.value = props.selectedProduct.selectedTopping;
    console.log(props.selectedProduct.selectedTopping)
  }

  // Set images
  if (props.selectedProduct) {
    if(props.selectedProduct.images) {
      image.value = props.selectedProduct.images[0].url;
    }
    else if (props.selectedProduct.image) {
      image.value = props.selectedProduct.image;
    }
  }

  // Set quantity
  if (props.selectedProduct && props.selectedProduct.quantity) {
    quantity.value = props.selectedProduct.quantity;
  }

  console.log(image.value)
}, { immediate: true });

</script>

<style scoped>
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

</style>