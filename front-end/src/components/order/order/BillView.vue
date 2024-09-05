<template>
  <div v-loading="ui.loading">
    <div class="checkout-box">
      <div class="container-lg container-fluid">
        <div class="row justify-content-center">
          <div class="col-12 col-lg-10">
            <!-- Header -->
            <div class="mb-5 checkout-header">
              <h1 class="d-flex justify-content-center align-items-center fw-bold h3 mb-0">
                <span class="icon me-2">
                  <font-awesome-icon icon="fa-solid fa-star" />
                </span>
                <span class="text">Đánh giá đơn hàng</span>
              </h1>
            </div>
            <!-- Main -->
            <div class="row">
              <div class="col-lg-6 col-md-6 col-sm-12">
                <div class="px-4 py-3 box-shadow rounded mb-3">
                  <h4 class="mb-3 fw-bold">Thông tin đơn hàng</h4>
                  <div>
                    <div class="d-flex border-bottom align-items-center mt-4 pb-3">
                      <div class="w-50 border-end">
                        <p class="mb-1">Người nhận</p>
                        <h5 class="m-0">{{bill.contactDetail.name}}</h5>
                      </div>
                      <div class="w-50 px-3">
                        <p class="mb-1">Số điện thoại</p>
                        <h5 class="m-0">{{bill.contactDetail.phoneNumber}}</h5>
                      </div>
                    </div>
                    <div class="border-bottom mt-4 pb-3">
                      <p class="mb-1">Giao đến</p>
                      <h6 class="m-0">{{bill.contactDetail.address}}</h6>
                    </div>
                    <div class="border-bottom mt-4 pb-3">
                      <p class="mb-1">Trạng thái thanh toán</p>
                      <h6 class="m-0">
                        <el-tag v-if="bill.paymentStatus" type="danger" effect="dark">UNPAID</el-tag>
                        <el-tag v-else type="success" effect="dark">PAID</el-tag>
                        <span class="ms-2">{{bill.paymentStatus ? 'Chưa thanh toán' : 'Đã thanh toán'}}</span>
                      </h6>
                    </div>
                    <div class="mt-4 pb-3">
                      <p class="mb-1">Mã đơn hàng</p>
                      <h6 class="m-0">{{bill.code}}</h6>
                    </div>
                  </div>
                </div>

                <div class="px-4 py-3 box-shadow rounded mb-3">
                  <h5>Đánh giá đơn hàng của bạn</h5>
                  <el-form :model="formData">
                    <el-form-item class="star-rating">
                    <span
                        v-for="star in 5"
                        :key="star"
                        @click="() => !formData.rated && setRating(star)"
                        :class="{ 'filled': star <= formData.rating }"
                    >
                      ★
                    </span>
                    </el-form-item>
                    <el-form-item>
                      <el-input v-model="formData.comment" type="textarea" placeholder="Hãy chia sẻ nhận xét của bạn về đơn hàng nhé" :disabled="formData.rated"/>
                    </el-form-item>
                    <button class="btn btn--orange-5 w-100 fw-medium py-2 btn--radius-100" @click="submitRating" :disabled="formData.rated">
                      Gửi
                    </button>
                    <hr>
                    <div class="d-flex flex-wrap">
                      <RatingComponent title="Đánh giá sản phẩm" :items="products" :userId="user.id"/>
                    </div>
                  </el-form>
                </div>
              </div>
              <div class="col-lg-6 col-md-6 col-sm-12">
                <div class="px-4 py-3 box-shadow rounded mb-3">
                  <h4 class="mb-3 fw-bold">Sản phẩm đã chọn</h4>
                  <div>
                    <div v-for="(item, index) in bill.products" :key="index" class="d-flex mt-4 product-item">
                      <h6 class="me-3">{{item.quantityProduct}}</h6>
                      <div class="product-details d-flex align-items-center justify-content-between w-100 pb-2">
                        <div>
                          <h6>{{item.productName}}</h6>
                          <p class="m-0 mb-1">{{(item.productSize === 'S')? 'Nhỏ' : (item.productSize === 'M')? 'Vừa' : (item.productSize === 'L')? 'Lớn' : ''}}</p>
                          <p class="m-0" v-if="item.toppingName"> {{ item.toppingName }} x {{ item.quantityTopping }}</p>
                        </div>
                        <h4 class="mb-0">{{ Utils.formatPrice(item.cost) }}</h4>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="px-4 py-3 box-shadow rounded">
                  <h4 class="mb-3 fw-bold">Tổng cộng</h4>
                  <div>
                      <div class="border-bottom d-flex align-items-center justify-content-between mt-4 pb-3">
                        <h6 class="mb-0">Thành tiền</h6>
                        <h6 class="mb-0">{{ Utils.formatPrice(bill.totalValue) }}</h6>
                      </div>
                      <div class="border-bottom d-flex align-items-center justify-content-between mt-4 pb-3">
                        <h6 class="mb-0">Phí giao hàng</h6>
                        <h6 class="mb-0">{{ Utils.formatPrice(18000) }}</h6>
                      </div>
                      <div class="border-bottom d-flex flex-column mt-4 pb-3">
                        <div class="d-flex align-items-center justify-content-between">
                          <div>
                            <el-text class="h5 text-primary">Khuyến mãi</el-text>
                            <p class="m-0">{{voucher.name}}</p>
                          </div>
                          <h6 class="mb-0">-{{ bill.valueOfVoucher ? Utils.formatPrice(bill.valueOfVoucher) : (voucher.discountValue + '%') }}</h6>
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
                      <div class="border-bottom d-flex align-items-center justify-content-between w-100 mt-4 pb-3">
                        <h6 class="mb-0 fw-bold">Số tiền thanh toán</h6>
                        <h6 class="mb-0 fw-bold">{{ Utils.formatPrice(bill.value) }}</h6>
                      </div>
                      <div class="d-flex align-items-center w-100 mt-4 pb-3">
                        <img :src="paymentMethodInfo.url" alt="" style="width: 2.5rem; height: 2.5rem; object-fit: cover">
                        <span class="ms-3">{{paymentMethodInfo.label}}</span>
                      </div>
                  </div>
                </div>
                <!-- Right side content -->
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import cash from '@/assets/images/money.jpg';
import momo from '@/assets/images/momo.png';
import vnpay from '@/assets/images/vnpay.png'
import {ref, onMounted, computed, defineProps} from "vue";
import axiosClient from '@/utils/axiosConfig';
import * as Utils from '@/utils';
import RatingComponent from "@/components/order/order/RatingComponent.vue";

const props = defineProps(['code']);

const ui = ref({
  loading: false,
});

const formData = ref({
  rating: 0,
  comment: '',
  rated: false
});

const bill = ref<any>({
  contactDetail: {
    name: '',
    phoneNumber: '',
    address: ''
  },
  products: [],
  voucherID: null,
  userID: null,
  paymentMethod: ''
});
const products = ref([]);
const voucher = ref({});
const user = ref({});

const setRating = (star: number) => {
  formData.value.rating = star;
};

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

const submitRating = async () => {
  if (formData.value.rated) {
    // Nếu đã đánh giá rồi thì không cần gửi lại
    return;
  }

  ui.value.loading = true;
  bill.value.rate = formData.value.rating;
  bill.value.comment = formData.value.comment;

  try {
    const response = await axiosClient.put(`/bills/${props.code}`, bill.value);
    formData.value.rated = true;

    let confirmed = await Utils.confirm("Bạn có muốn đánh giá các sản phẩm không？", "Xác nhận");
    if (confirmed) {
      ui.value.loading = false;
      return;
    } else {
      setTimeout(() => {
        window.location.href = '/';
      }, 1500);
    }
  } catch (error) {
    console.error("Error submitting rating:", error);
  } finally {
    ui.value.loading = false;
  }
};

onMounted (async () => {
  const response = await axiosClient.get(`/bills/${props.code}`);
  bill.value = response.data;
  if(bill.value.voucherID) {
    const response1 = await axiosClient.get(`/vouchers/${bill.value.voucherID}`);
    voucher.value = response1.data;
  }
  // const response2 = await axiosClient.get(`/users/${bill.value.userID}`);
  // user.value = response2.data;
  processBillData()

})
</script>

<style scoped>
hr {
  margin: 2rem 0 1rem 0;
  border-top: 1px solid var(--smoky-gray-2);
}

.checkout-header .icon {
  color: #fad207;
  font-size: var(--space-16);
  line-height: 1;
}

.checkout-box {
  padding-bottom: var(--space-20);
  padding-top: var(--space-16);
}

.product-details {
  border-bottom: 1px solid #ccc;
}

.product-item:last-child .product-details {
  border-bottom: none;
}

.star-rating {
  cursor: pointer;
}

.star-rating span {
  font-size: 50px; /* Adjust size as needed */
  color: #ccc; /* Color of unfilled stars */
}

.star-rating span.filled {
  color: gold; /* Color of filled stars */
}

@media (min-width: 768px) {
  .checkout-header .icon {
    font-size: var(--space-24);
  }
}
</style>
