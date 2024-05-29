<template>
  <div>
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
                <div class="d-flex align-items-md-start flex-row delivery-card border-bottom">
                  <div class="delivery-card__image">
                    <img :src="delivery" style="width: 40px;" alt="">
                  </div>
                  <div class="d-flex align-items-md-start justify-content-between ps-3">
                    <div>
                      <h5 class="delivery-card__title m-0">
                        4 Ngõ 15 Phố Duy Tân
                      </h5>
                      <p class="delivery-card__description mb-0">
                        4 Ngõ 15 Phố Duy Tân, Dịch Vọng Hậu, Cầu Giấy, Hà Nội 100000, Việt Nam
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
                  <input type="text" class="delivery__input form-control" placeholder="Tên người nhận">
                </div>
                <div class="mb-3">
                  <input type="text" class="delivery__input form-control" placeholder="Số điện thoại">
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
                    <router-link to="/order" class="text-decoration-none text-dark">
                      <p class="checkout-box_btn-outline">Thêm món</p>
                    </router-link>
                  </div>
                  <div class="order-card d-flex align-items-center justify-content-between" v-for="(item, index) in cartItems" :key="item.productId">
                    <div class="d-flex">
                      <span class="d-flex align-items-center order-card-icon">
                        <font-awesome-icon icon="fa-solid fa-pen" />
                      </span>
                      <div class="ps-3">
                        <h5 class="delivery-card__title mb-0"> {{ item.quantity }} x {{ item.name }}</h5>
                        <p class="delivery-card__description mb-0">{{(item.productSize.size === 'S')? 'Nho' : (item.productSize.size === 'M')? 'Vua' : (item.productSize.size === 'L')? 'Lon' : ''}}</p>
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
                  <div class="d-flex align-items-center justify-content-between pt-3 order-card">
                    <div>
                      <p class="text-orange order-card__text">Khuyến mãi</p>
                    </div>
                  </div>
                </div>
                <div class="checkout-box checkout-box--list-submited px-4 d-flex align-items-center justify-content-between position-static">
                  <div>
                    <p class="order-card__text text-white mb-0">Thành tiền</p>
                    <p class="order-card__text text-white fw-bold mb-0">{{formatPrice(totalCost + 18000)}}</p>
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
                    <el-radio :value="item" v-model="payment">
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
              <p class="mb-0 text-center text-orange">
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
<!-- Voucher Dialog -->
    <el-dialog v-model="ui.voucherDialog" :show-close="false" width="30%" class="rounded custom-dialog">
      <template #header="{close}">
        <div class="d-flex align-items-center w-100 p-2">
          <h4 class="mx-auto my-0 fs-6">Khuyến mãi</h4>
          <el-button class="border-0" @click="close">
            <el-icon class="fs-3"><Close /></el-icon>
          </el-button>
        </div>
      </template>
      <div>
        <div class="d-flex align-items-center">
          <font-awesome-icon icon="fa-solid fa-expand" />
          <input type="text" v-model="code">
          <button class="btn" :class=" (code) ? 'btn--orange-1' : 'btn--smoky-gray'"></button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import delivery from "@/assets/images/Delivery2.png";
import money from '@/assets/images/money.jpg';
import momo from '@/assets/images/momo.png';
import zalo from '@/assets/images/zalopay.png';
import shoppe from '@/assets/images/shoppepay.png';
import banking from '@/assets/images/banking.png'
import { ref, computed } from 'vue';
import { useStore } from 'vuex';
import {Close} from "@element-plus/icons-vue";
import axios from "axios";

const store = useStore();
const cartItems = computed(() => store.getters.cartItems);
const removeFromCart = (index: number) => {
  store.dispatch('removeProductFromCart', index);
};

const formatPrice = (price: number): string => {
  return price.toLocaleString('vi-VN') + 'đ';
};

const totalCost = computed(() => {
  return cartItems.value.reduce((total, item) => total + item.cost, 0);
});

const paymentMethods = [
  {url: money, label: 'Tiền mặt'},
  {url: momo, label: 'MoMo'},
  {url: zalo, label: 'ZaloPay'},
  {url: shoppe, label: 'ShoppePay'},
  {url: banking, label: 'Thẻ ngân hàng'}
]

const payment = {};

const formData = ref({
  customerID: null,
  voucherID: null,
  value: totalCost.value,
  ValueOfVoucher: 0,
  ValueOfCustomerPoint: 0,
  totalValue: totalCost.value + 18000,
  code: generateRandomString(),
  point: 0,
  status: 0,
  address: '4 Ngõ 15 Phố Duy Tân, Dịch Vọng Hậu, Cầu Giấy, Hà Nội 100000, Việt Nam',
  products: []
})

const ui = ref({
  voucherDialog: false
})

function generateRandomString(): string {
  const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
  return Array.from({ length: 10 }, (_, i) => i === 0 ? characters[Math.floor(Math.random() * 26)] : characters[Math.floor(Math.random() * characters.length)]).join('');
}

const confirmOrder = async () => {
  for (const item of cartItems.value) {
    formData.value.products.push({
      productID: item.productId,
      productSizeID: item.productSize.id,
      toppingID: null,
      quantity: item.quantity,
      cost: item.cost
    })
  }
  await axios.post('http://localhost:8082/api/bills', formData.value);
}
</script>
<style>
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

.delivery__input {
  background: #fafafa;
  border: 1px solid rgba(0, 0, 0, .15);
  border-radius: 0!important;
  font-size: 1rem;
  line-height: 1.5;
  outline: 0;
  padding: 11px 23px!important;
  height: var(--space-44);
}

.delivery__input::placeholder {
  font-size: var(--space-14)!important;
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
