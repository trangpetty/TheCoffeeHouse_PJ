<template>
  <div>
    <div class="checkout-box">
      <div class="container-lg container-fluid pb-4">
        <div class="row justify-content-center">
          <div class="col-12 col-lg-10">
            <div class="mb-5 mt-3 checkout-header">
              <div v-if="bill.status === 'cancel'" class="text-center">
                <h1 class="h2" style="font-weight: 600;">
                  <span class="text text-danger"><font-awesome-icon icon="fa-solid fa-clock-rotate-left" /> Đơn hàng đã bị hủy</span>
                </h1>
                <h4>Đơn hàng sẽ được hoàn tiền</h4>
                <h1>{{ Utils.formatPrice(bill.value) }}</h1>
              </div>
              <h1 class="text-center h2 mb-0" style="font-weight: 600;" v-else>
                <span class="text text-danger"><font-awesome-icon icon="fa-solid fa-circle-exclamation" /> Đặt hàng không thành công</span>
              </h1>
            </div>
            <div class="d-block">
              <!-- Delivery -->
              <div class="d-block checkout-box-item float-lg-start">
                <div class="d-flex justify-content-between">
                  <h4 class="checkout-box_title">
                    Giao hàng
                  </h4>
                </div>
                <div class="d-flex align-items-md-start flex-row delivery-card border-bottom">
                  <div class="delivery-card__image">
                    <img :src="delivery" style="width: 40px;" alt="">
                  </div>
                  <div class="d-flex align-items-md-start justify-content-between ps-3 w-100">
                    <div>
                      <h5 class="delivery-card__title m-0 text-uppercase">
                        {{ bill.contactDetail.name }}
                      </h5>
                      <p class="delivery-card__description mb-0">
                        {{ bill.contactDetail.address }}
                      </p>
                    </div>
                  </div>
                </div>
                <div class="d-flex align-items-md-start flex-row delivery-card">
                  <div class="delivery-card__image"></div>
                  <div class="d-flex align-items-md-start ps-3 w-100">
                    <div>
                      <h5 class="delivery-card__title m-0">
                        Thời gian giao dự kiến
                      </h5>
                      <p class="delivery-card__description mb-0">
                        Càng sớm càng tốt
                      </p>
                    </div>
                  </div>
                </div>
                <div class="d-flex align-items-md-start flex-row delivery-card">
                  <div class="delivery-card__image"></div>
                  <div class="d-flex align-items-md-start ps-3 w-100">
                    <div>
                      <h5 class="delivery-card__title m-0">
                        Người nhận
                      </h5>
                      <p class="delivery-card__description mb-0 text-uppercase">
                        {{ bill.contactDetail.name }}
                      </p>
                    </div>
                  </div>
                </div>
                <div class="d-flex align-items-md-start flex-row delivery-card">
                  <div class="delivery-card__image"></div>
                  <div class="d-flex align-items-md-start ps-3 w-100">
                    <div>
                      <h5 class="delivery-card__title m-0">
                        Số điện thoại
                      </h5>
                      <p class="delivery-card__description mb-0">
                        {{ bill.contactDetail.phoneNumber }}
                      </p>
                    </div>
                  </div>
                </div>
                <div class="d-flex align-items-md-start flex-row delivery-card">
                  <div class="delivery-card__image"></div>
                  <div class="d-flex align-items-md-start ps-3 w-100">
                    <div>
                      <h5 class="delivery-card__title m-0">
                        Hướng dẫn giao hàng
                      </h5>
                      <p class="delivery-card__description mb-0">
                        Khong
                      </p>
                    </div>
                  </div>
                </div>
              </div>
              <!--  Product List -->
              <div class="box-shadow checkout-box-item float-lg-end rounded">
                <div class="py-3 w-100 px-4">
                  <h4 class="checkout-box_title mb-0">Các món đã chọn</h4>
                  <div class="order-card d-flex align-items-center justify-content-between" v-for="(item, index) in bill.products" :key="index">
                    <div class="d-flex">
                      <div class="ps-3">
                        <h5 class="delivery-card__title mb-0"> {{ item.quantityProduct }} x {{ item.productName }}</h5>
                        <p class="delivery-card__description mb-0">{{(item.productSize === 'S')? 'Nhỏ' : (item.productSize === 'M')? 'Vừa' : (item.productSize === 'L')? 'Lớn' : ''}}</p>
                        <h5 class="delivery-card__description mb-0" v-if="item.toppingName"> {{ item.toppingName }} x {{ item.quantityTopping }}</h5>
                      </div>
                    </div>
                    <div>
                      <p class="mb-0">{{ Utils.formatPrice(item.cost) }}</p>
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
                      <p>{{Utils.formatPrice(bill.totalValue)}}</p>
                    </div>
                  </div>
                  <div class="border-bottom d-flex align-items-center justify-content-between pt-3">
                    <div>
                      <p class="order-card__text">Phí giao hàng</p>
                    </div>
                    <div>
                      <p>{{Utils.formatPrice(18000)}}</p>
                    </div>
                  </div>
                  <div class="border-bottom d-flex flex-column mt-4 pb-3">
                    <div class="d-flex align-items-center justify-content-between">
                      <div>
                        <el-text class="text-orange order-card__text">Khuyến mãi</el-text>
                        <p class="m-0">{{voucher.name}}</p>
                      </div>
                      <h6 class="mb-0" v-if="bill.valueOfVoucher || voucher.discountValue">-{{ bill.valueOfVoucher ? Utils.formatPrice(bill.valueOfVoucher) : (voucher.discountValue + '%') }}</h6>
                    </div>
                    <div class="d-flex align-items-center justify-content-between pt-3" v-if="bill.usedCustomerPoints">
                      <div>
                        <p class="order-card__text">Sử dụng điểm: {{ bill.usedCustomerPoints }}</p>
                      </div>
                      <div>
                        <p>- {{Utils.formatPrice(bill.totalValue - bill.value)}}</p>
                      </div>
                    </div>
                  </div>
                  <div class="d-flex align-items-center justify-content-between border-bottom pt-3" v-if="bill.usedCustomerPoints">
                    <div>
                      <p class="order-card__text">Sử dụng điểm</p>
                    </div>
                    <div>
                      <p>- {{Utils.formatPrice(bill.totalValue - bill.value)}}</p>
                    </div>
                  </div>
                </div>
                <div class="checkout-box checkout-box--list-submited px-4 d-flex align-items-center justify-content-between position-static">
                  <div>
                    <p class="order-card__text text-white mb-0">Thành tiền</p>
                    <p class="order-card__text text-white fw-bold mb-0">{{ Utils.formatPrice(bill.value) }}</p>
                  </div>
                  <button class="btn btn--white px-4">
                    Liên hệ hỗ trợ
                  </button>
                </div>
              </div>
              <!--  Method List -->
              <div class="d-block checkout-box-item float-lg-start mt-3">
                <div>
                  <h4 class="checkout-box_title mb-3">
                    Phương thức thanh toán
                  </h4>
                  <div class="d-flex align-items-center w-100 mt-2 pb-3">
                    <img :src="paymentMethodInfo.url" alt="" style="width: 2.5rem; height: 2.5rem; object-fit: cover">
                    <span class="ms-3">{{paymentMethodInfo.label}}</span>
                  </div>
                </div>
                <!--  Payment Status  -->
                <div class="mt-3">
                  <h4 class="checkout-box_title mb-3">
                    Trạng thái thanh toán
                  </h4>
                  <div class="d-flex align-items-center w-100 mt-2 pb-3">
                    <h6 class="m-0">
                      <el-tag v-if="bill.paymentStatus" type="danger" effect="dark">UNPAID</el-tag>
                      <el-tag v-else type="success" effect="dark">PAID</el-tag>
                      <span class="ms-2">{{bill.paymentStatus ? 'Chưa thanh toán' : 'Đã thanh toán'}}</span>
                    </h6>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import delivery from "@/assets/images/Delivery2.png";
import cash from '@/assets/images/money.jpg';
import momo from '@/assets/images/momo.png';
import vnpay from '@/assets/images/vnpay.png'
import {ref, onMounted, computed, defineProps} from "vue";
import axiosClient from '@/utils/axiosConfig';
import * as Utils from '@/utils';
import RatingComponent from "@/components/order/order/RatingComponent.vue";

const props = defineProps(['code']);

const bill = ref<any>({
  contactDetail: {
    name: '',
    phoneNumber: '',
    address: ''
  },
  products: [],
  voucherID: null,
  userID: null,
  paymentMethod: '',
  paymentStatus: null,
  status: '',
  value: null
});
const products = ref([]);
const voucher = ref({});

const processBillData = () => {

  const uniqueProducts = new Map();
  bill.value.products.forEach((item) => {
    // Check for unique products
    if (!uniqueProducts.has(item.productID)) {
      uniqueProducts.set(item.productID, {
        id: item.productID,
        name: item.productName
      });
    }
  });

  // Convert the maps back to arrays
  products.value = Array.from(uniqueProducts.values());
};

const paymentMethods = ref([
  { url: momo, label: 'MoMo', methodName: 'momo' },
  { url: vnpay, label: 'VnPay', methodName: 'vnpay' },
  { url: cash, label: 'Tiền mặt', methodName: 'cash' },
]);

// Computed property to get the correct image based on payment method
const paymentMethodInfo = computed(() => {
  const method = paymentMethods.value.find(
      (method) => method.methodName === bill.value.paymentMethod
  );
  return method ? { url: method.url, label: method.label } : { url: '', label: '' };
});

onMounted (async () => {
  const response = await axiosClient.get(`/bills/${props.code}`);
  bill.value = response.data;
  if(bill.value.voucherID) {
    const response1 = await axiosClient.get(`/vouchers/${bill.value.voucherID}`);
    voucher.value = response1.data;
  }

  processBillData()
})
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

@media (max-width: 769px) {
  .checkout-box .checkout-box-item {
    width: 100%;
    margin-left: 0;
  }
}
</style>
