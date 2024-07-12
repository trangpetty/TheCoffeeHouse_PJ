<template>
  <div v-loading="ui.loading">
    <div class="checkout-box">
      <div class="container-lg container-fluid">
        <div class="row justify-content-center">
          <div class="col-12 col-lg-10">
            <div class="mb-5 checkout-header">
              <h1 class="text-center h3 mb-0" style="font-weight: 600;">
                <span class="icon me-2">
                  <font-awesome-icon icon="fa-solid fa-file" />
                </span>
                <span class="text">Xác nhận đơn hàng</span>
              </h1>
            </div>
            <div class="d-block">
              <!-- Delivery -->
              <div class="d-block checkout-box-item float-lg-start">
                <div class="d-flex justify-content-between">
                  <h4 class="checkout-box_title">
                    Giao hàng
                  </h4>
                  <div class="checkout-box_btn-outline">
                    Đổi phương thức
                  </div>
                </div>
                <div class="d-flex align-items-md-start flex-row delivery-card border-bottom" @click="showAddressDialog">
                  <div class="delivery-card__image">
                    <img :src="delivery" style="width: 40px;" alt="">
                  </div>
                  <div class="d-flex align-items-md-start justify-content-between ps-3">
                    <div>
                      <h5 class="delivery-card__title m-0">
                        4 Ngõ 15 Phố Duy Tân
                      </h5>
                      <p class="delivery-card__description mb-0">
                        {{ address }}
                      </p>
                    </div>
                    <span class="icon mt-2">
                      <font-awesome-icon icon="fa-solid fa-chevron-right" />
                    </span>
                  </div>
                </div>
                <div class="d-flex align-items-md-start flex-row delivery-card">
                  <div class="delivery-card__image"></div>
                  <div class="d-flex align-items-md-start justify-content-between ps-3 w-100">
                    <div>
                      <h5 class="delivery-card__title m-0">
                        Nhận hàng trong ngày 15 - 30 phút
                      </h5>
                      <p class="delivery-card__description mb-0">
                        Vào lúc: Càng sớm càng tốt
                      </p>
                    </div>
                    <span class="icon mt-2">
                      <font-awesome-icon icon="fa-solid fa-chevron-right" />
                    </span>
                  </div>
                </div>
                <div class="mb-3">
                  <input type="text" :value="user.name" class="delivery__input form-control" placeholder="Tên người nhận">
                </div>
                <div class="mb-3">
                  <input type="text" :value="user.phoneNumber" class="delivery__input form-control" placeholder="Số điện thoại">
                </div>
                <div class="mb-3">
                  <input type="text" class="delivery__input form-control" placeholder="Thêm hướng dẫn giao hàng">
                </div>
              </div>
              <!--  Product List -->
              <div class="ms-5 box-shadow checkout-box-item float-lg-end rounded">
                <div class="py-3 w-100 px-4">
                  <div class="d-flex justify-content-between">
                    <h4 class="checkout-box_title mb-0">Các món đã chọn</h4>
                    <router-link to="/" class="text-decoration-none text-dark">
                      <p class="checkout-box_btn-outline">Thêm món</p>
                    </router-link>
                  </div>
                  <div class="order-card d-flex align-items-center justify-content-between" v-for="(item, index) in cartItems" :key="item.id">
                    <div class="d-flex">
                      <span class="d-flex align-items-center order-card-icon" @click="openProductDialog(item, index)">
                        <font-awesome-icon icon="fa-solid fa-pen" />
                      </span>
                      <div class="ps-3">
                        <h5 class="delivery-card__title mb-0"> {{ item.quantity }} x {{ item.name }}</h5>
                        <p class="delivery-card__description mb-0">{{(item.selectedSize.size === 'S')? 'Nho' : (item.selectedSize.size === 'M')? 'Vua' : (item.selectedSize.size === 'L')? 'Lon' : ''}}</p>
                        <h5 class="delivery-card__description mb-0" v-if="item.selectedTopping.name"> {{ item.selectedTopping.name }} x {{ item.selectedTopping.quantity }}</h5>
                        <p class="d-inline" style="cursor: pointer" @click="removeFromCart(index)">Xoa</p>
                      </div>
                    </div>
                    <div>
                      <p class="mb-0">{{ formatPrice(item.cost) }}</p>
                    </div>
                  </div>
                  <div class="checkout-box">
                    <h4 class="checkout-box_title mb-0">Tổng cộng</h4>
                  </div>
                  <div class="d-flex align-items-center justify-content-between border-bottom pt-3">
                    <div>
                      <p class="order-card__text">Thành tiền</p>
                    </div>
                    <div>
                      <p>{{formatPrice(totalCost)}}</p>
                    </div>
                  </div>
                  <div class="d-flex align-items-center justify-content-between pt-3">
                    <div>
                      <p class="order-card__text">Phí giao hàng</p>
                    </div>
                    <div>
                      <p>{{formatPrice(18000)}}</p>
                    </div>
                  </div>
                  <div class="d-flex align-items-center justify-content-between border-bottom pt-3">
                    <div>
                      <p class="order-card__text">Bạn có mã Freeship trong mục Ưu đãi</p>
                    </div>
                    <div>
                      <p class="text-decoration-line-through text-black-50">{{formatPrice(0)}}</p>
                    </div>
                  </div>
                  <div class="d-flex pt-3 order-card flex-column">
                    <div>
                      <p class="text-orange order-card__text mb-0" @click="showVoucherDialog">Khuyến mãi</p>
                    </div>
                    <div class="d-flex flex-row justify-content-between" v-if="!errorMessage && voucher.name">
                      <div class="d-flex flex-column">
                        <span>{{voucher.name}}</span>
                        <p class="d-inline cursor-pointer" @click="handleClearVoucher">Xoa</p>
                      </div>
                      <p v-if="!discount">
                        -{{formatPrice(parseInt(voucher.discountValue))}}
                      </p>
                      <p v-else>
                        -{{formatPrice(discount)}}
                      </p>
                    </div>
                    <div v-if="errorMessage" class="error-message">
                      <p>{{ errorMessage }}</p>
                    </div>
                  </div>
                </div>
                <div class="checkout-box checkout-box--list-submited px-4 d-flex align-items-center justify-content-between position-static">
                  <div>
                    <p class="order-card__text text-white mb-0">Thành tiền</p>
                    <p class="order-card__text text-white fw-bold mb-0">{{ formatPrice(totalValue) }}</p>
                  </div>
                  <button class="btn btn--white px-4" @click="confirmOrder">
                    Đặt hàng
                  </button>
                </div>
              </div>
              <!--  Method List -->
              <div class="d-block checkout-box-item float-lg-start">
                <h4 class="checkout-box_title mb-3">
                  Phương thức thanh toán
                </h4>
                <ul class="list-payment-method">
                  <li class="list-payment-method-item" v-for="(item, index) in paymentMethods" :key="index">
                    <el-radio :value="item" v-model="paymentMethod" class="w-100">
                      <span class="mx-2" style="width: 1.5rem; height: 0.8rem">
                        <img :src="item.url" alt="" style="width: 1.5rem; height: 0.8rem; object-fit: cover">
                      </span>
                      <span class="text">{{item.label}}</span>
                    </el-radio>
                  </li>
                </ul>
              </div>
            </div>
            <div class="ms-5 checkout-box-item float-lg-end py-4">
              <p class="mb-0 text-center text-orange cursor-pointer" @click="deleteOrder">
                <span class="icon me-2">
                  <font-awesome-icon icon="fa-solid fa-trash" />
                </span>
                <span>Xóa đơn hàng</span>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- Product Dialog -->
    <ProductDialog
        :selectedProduct="selectedProduct"
        :visible="ui.dialogVisible"
        :addCart="false"
        :index="selectedIndex"
        :userId="user.id"
        @close="ui.dialogVisible = false"
    />
  </div>
</template>

<script lang="ts" setup>
import delivery from "@/assets/images/Delivery2.png";
import money from '@/assets/images/money.jpg';
import momo from '@/assets/images/momo.png';
import vnpay from '@/assets/images/vnpay.png'
import { ref, computed, watch, onMounted } from 'vue';
import { useStore } from 'vuex';
import axios from "axios";
import router from "@/router";
import ProductDialog from "@/components/order/dialog/ProductDialog.vue";

const store = useStore();

const cartItems = computed(() => store.getters.cartItems);
const voucher = computed(() => store.getters.voucher);
const errorMessage = computed(() => store.getters.errorMessage);
const address = computed(() => store.getters.address);
const user = computed(() => store.getters.user);
const totalQuantity = computed(() => store.getters.cartTotalQuantity);

const feeship = ref(18000);
const discount = ref(0);

const selectedProduct = ref({});
const selectedIndex = ref(null);

const removeFromCart = (index: number) => {
  store.dispatch('removeProductFromCart', index);
};

onMounted(() => {
  store.dispatch('loadVoucher');
});

const formatPrice = (price: number): string => {
  return price.toLocaleString('vi-VN') + 'đ';
};

const totalCost = computed(() => {
  return cartItems.value.reduce((total, item) => total + item.cost, 0);
});

const totalValue = computed(() => {
  let cost = totalCost.value;

  // Adjust total cost based on voucher type
  if (cost >= voucher.value.minimumOrderValue && totalQuantity.value >= voucher.value.minimumItems) {
    if(voucher.value.voucherType == 'percentage') {
      // eslint-disable-next-line vue/no-side-effects-in-computed-properties
      discount.value = cost * voucher.value.discountValue;
    }
    else {
      // eslint-disable-next-line vue/no-side-effects-in-computed-properties
      discount.value = parseInt(voucher.value.discountValue);
    }
    cost -= discount.value;
  }
  else {
    // eslint-disable-next-line vue/no-side-effects-in-computed-properties
    discount.value = 0;
  }

  cost += feeship.value; // Adding feeship
  return cost;
})

watch(voucher, (newVoucher, oldVoucher) => {
  discount.value = 0; // Reset discount when voucher changes
});

const formData = ref({
  userID: null,
  voucherID: null,
  ValueOfVoucher: 0,
  ValueOfCustomerPoint: 0,
  totalValue: 0,
  value: 0,
  code: generateRandomString(),
  point: 0,
  status: 0,
  address: '',
  products: [],
  paymentMethod: ''
});

watch(totalValue, (newValue) => {
  formData.value.value = newValue;
});

watch(totalCost, (newValue) => {
  formData.value.totalValue = newValue;

  if (newValue > voucher.value.minimumOrderValue && totalQuantity.value >= voucher.value.minimumItems) {
    store.dispatch('clearErrorMessage');
  } else {
    store.dispatch('setErrorMessage', voucher.value.errorMessage);
    discount.value = 0;
  }
});

watch(errorMessage, (newErrorMessage) => {
  if (newErrorMessage) {
    formData.value.voucherID = null;
  }
});

watch([user, voucher, totalCost, totalValue, address], ([newUser, newVoucher, newTotalCost, newTotalValue, newAddress]) => {
  formData.value.userID = newUser?.id || null;
  formData.value.voucherID = newVoucher?.id || null;
  formData.value.ValueOfVoucher = discount.value;
  formData.value.totalValue = newTotalCost;
  formData.value.value = newTotalValue;
  formData.value.address = newAddress;
});

watch(cartItems, (newCartItems) => {
  if (newCartItems.length === 0) {
    ui.value.loading = true;
    setTimeout(() => {
      router.push('/'); // Replace 'home' with your actual home route name
    }, 2000);
  }
});

const handleClearVoucher = () => {
  store.dispatch('clearVoucher');
  store.dispatch('clearErrorMessage');
};

const paymentMethods = ref([
  {url: momo, label: 'MoMo', methodName: 'momo'},
  {url: vnpay, label: 'VnPay', methodName: 'vnpay'},
  {url: money, label: 'Tiền mặt', methodName: 'cash'},
]);

const paymentMethod = ref(paymentMethods.value[0]);

const ui = ref({
  voucherDialog: false,
  loading: false,
  dialogVisible: false
});

function generateRandomString(): string {
  const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
  return Array.from({ length: 10 }, (_, i) => i === 0 ? characters[Math.floor(Math.random() * 26)] : characters[Math.floor(Math.random() * characters.length)]).join('');
}

const showAddressDialog = () => {
  store.dispatch('openAddressDialog', true);
};

const showVoucherDialog = () => {
  store.dispatch('VoucherDialog', true);
};

const confirmOrder = async () => {
  ui.value.loading = true;
  for (const item of cartItems.value) {
    formData.value.products.push({
      productID: item.id,
      productSizeID: item.selectedSize.id,
      toppingID: item.selectedTopping ? item.selectedTopping.toppingID : null,
      quantityProduct: item.quantity,
      quantityTopping: item.selectedTopping ? item.selectedTopping.quantity : null,
      cost: item.cost
    });
  }
  if (paymentMethod.value.methodName === 'cash') {
    // Handle cash payment method
  } else {
    try {
      const endpoint = paymentMethod.value.methodName === 'momo' ? 'momo' : 'vnpay';
      // Example axios request
      const pay = await axios.post(`http://10.30.100.178:8082/api/payment/${endpoint}`, formData.value);
      if (pay.status === 200) {
        setTimeout(() => {
          deleteOrder();
          handleClearVoucher();
          window.location.href = pay.data.paymentUrl;
        }, 2000);
      }
      console.log(formData.value)
    } catch (error) {
      console.error('Error processing payment:', error);
    }
  }
};

const deleteOrder = () => {
  store.dispatch('clearCart');
}

const openProductDialog = (item, index) => {
  console.log("user id: ", user.value.id);
  selectedProduct.value = item;
  selectedIndex.value = index;
  ui.value.dialogVisible = true;
  console.log("edit", item)
}
</script>

<style scoped>
.checkout-header .icon{
  color: #fad207;
  font-size: var(--space-16);
  line-height: 1;
}

.checkout-box {
  padding-bottom: var(--space-20);
  padding-top: var(--space-16);
}

.checkout-box .checkout-box-item
{
  margin-left: var(--space-50);
  width: calc(50% - var(--space-50));
}

.checkout-box_title {
  color: var(--black);
  font-size: var(--space-18);
  line-height: var(--space-26);
  padding-bottom: var(--space-6);
  position: relative;
}

.checkout-box_title:after {
  background: #fa8c16;
  border-radius: var(--space-6);
  bottom: 0;
  content: "";
  height: var(--space-2);
  left: 0;
  position: absolute;
  width: var(--space-50);
  z-index: 999;
}

.checkout-box_btn-outline {
  background: var(--white);
  border: 1px solid #262626;
  border-radius: var(--space-100);
  cursor: pointer;
  font-size: var(--space-12);
  line-height: var(--space-22);
  padding: 7px var(--space-12);
}

.delivery-card,
.order-card {
  cursor: pointer;
  padding: var(--space-12) 0;
}

.delivery-card .delivery-card__image {
  border-radius: 50%;
  height: var(--space-44);
  width: var(--space-44);
}

.delivery-card__title,
.order-card__text{
  display: -webkit-box;
  font-size: var(--space-16);
  height: 24px;
  line-height: var(--space-24);
  overflow: hidden;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}

.delivery-card__title {
  font-weight: 600;
}

.delivery-card__description {
  font-size: var(--space-14);
  line-height: var(--space-22);
}

.delivery-card .icon {
  font-size: var(--space-24);
  line-height: 1;
  padding-left: var(--space-30);
}

.order-card-icon {
  color: var(--orange-2);
  font-size: var(--space-16);
  line-height: 1;
}

.list-payment-method {
  list-style-type: none;
}

.list-payment-method .list-payment-method-item {
  border-bottom: 1px solid #c4c4c4;
  padding: var(--space-16) 0;
}

.payment-method-item img {
  height: var(--space-16);
  object-fit: cover;
  width: var(--space-24);
}

.btn.btn--orange-1, .btn.btn--orange-1:hover, .btn.btn--orange-1:active, .el-radio__input.is-checked .el-radio__inner {
  background: var(--orange-2) !important;
  border-color: var(--orange-2) !important;
}

.checkout-box.checkout-box--list-submited {
  background: var(--radial-gradient-orange-9);
  bottom: var(--space-70);
  box-shadow: 0 2px 8px rgba(0,0,0,.12);
  left: 0;
  padding: var(--space-16) 15px;
  position: fixed;
  z-index: 999;
  border-bottom-left-radius: var(--space-8);
  border-bottom-right-radius: var(--space-8);
}

.form-control:focus {
  box-shadow: none!important;
  outline: none!important;
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

@media (min-width: 992px)
{
  .box-checkout {
    padding-bottom: var(--space-98);
    padding-top: var(--space-50);
  }
}

@media (min-width: 768px)
{
  .checkout-header .icon {
    font-size: var(--space-24);
  }
}
</style>
