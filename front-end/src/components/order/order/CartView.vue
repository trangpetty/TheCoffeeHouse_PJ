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
                  <div class="d-flex align-items-md-start justify-content-between ps-3 w-100">
                    <div>
                      <h5 class="delivery-card__title m-0">
                        {{ address.split(',')[0] }}
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
                  <input type="text" v-model="formData.contactDetail.name" class="delivery__input form-control" placeholder="Tên người nhận">
                </div>
                <div class="mb-3">
                  <input type="text" v-model="formData.contactDetail.phoneNumber" @blur="handleBlur" class="delivery__input form-control" placeholder="Số điện thoại">
                  <el-text type="danger" v-if="ui.showError">Số điện thoại không hợp lệ</el-text>
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
                      <p class="mb-0">
                        <span v-if="voucher.voucherType === 'fixedPrice' && voucher.productIDs?.includes(item.id)">
                          <span class="price-crossed me-2">{{ formatPrice(item.cost) }}</span>
                          <span> {{ formatPrice(voucher.fixedPrice) }}</span>
                        </span>
                                                <span v-else>
                          {{ formatPrice(item.cost) }}
                        </span>


                      </p>
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
                  <div class="d-flex align-items-center justify-content-between border-bottom pt-3" v-if="!voucher.id">
                    <div>
                      <p class="order-card__text">Bạn có mã Freeship trong mục Ưu đãi</p>
                    </div>
                    <div>
                      <p class="text-decoration-line-through text-black-50">{{formatPrice(0)}}</p>
                    </div>
                  </div>
                  <div class="d-flex pt-3 order-card flex-column">
                    <div>
                      <div>
                        <p class="text-orange order-card__text mb-0" @click="showVoucherDialog">Khuyến mãi</p>
                      </div>
                      <div class="d-flex flex-row justify-content-between" v-if="!errorMessage && voucher.name">
                        <div class="d-flex flex-column">
                          <span>{{voucher.name}}</span>
                          <p class="d-inline cursor-pointer" @click="handleClearVoucher">Xoa</p>
                        </div>
                        <p v-if="!discount && voucher.discountValue">
                          -{{formatPrice(parseInt(voucher.discountValue))}}
                        </p>
                        <p v-if="discount">
                          -{{formatPrice(discount)}}
                        </p>
                      </div>
                      <div v-if="errorMessage" class="error-message">
                        <p>{{ errorMessage }}</p>
                      </div>
                    </div>
                    <div v-if="user.point || customerPoint">
                      <div class="d-flex justify-content-between align-items-center">
                        <p class="text-orange order-card__text mb-0 w-50">Điểm tích lũy hiện tại: {{user.point ? user.point : customerPoint}}</p>
                        <el-form-item v-if="user.point >= 10 || customerPoint >= 10" label="Sử dụng điểm" class="text-orange order-card-icon mb-0 w-50">
                          <el-select v-model="formData.usedCustomerPoints">
                            <el-option label="0" :value="0"/>
                            <el-option label="10%" :value="10"/>
                            <el-option label="15%" :value="15" v-if="user.point >= 15 || customerPoint >= 15"/>
                            <el-option label="20%" :value="20" v-if="user.point >= 20 || customerPoint >= 20"/>
                          </el-select>
                        </el-form-item>
                      </div>
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
import axiosClient from '@/utils/axiosConfig';
import router from "@/router";
import ProductDialog from "@/components/order/dialog/ProductDialog.vue";
import {ElMessage} from "element-plus";

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

const customerPoint = ref(0);

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
  let total = 0;

  cartItems.value.forEach(item => {
    if (voucher.value && voucher.value.voucherType === 'fixedPrice' && voucher.value.productIDs.includes(item.id) && (voucher.value.size === item.selectedSize.size)) {
      // Áp dụng giá cố định cho sản phẩm đủ điều kiện
      const fixedPrice = voucher.value.fixedPrice; // Giá cố định từ voucher
      const itemPrice = item.selectedSize?.surcharge ? fixedPrice + item.selectedSize.surcharge : fixedPrice;
      total += itemPrice * item.quantity;
    }
    else {
      // Giá bình thường cho các sản phẩm không áp dụng voucher
      total += item.cost;
    }
  });

  if (voucher.value && voucher.value.voucherType === 'comboPrice') {
    const applicableItems = cartItems.value.filter(item => voucher.value.productIDs.includes(item.id));
    if (applicableItems.length > 0) {
      // Tính toán chi phí các sản phẩm không nằm trong combo
      const otherItemsCost = cartItems.value
          .filter(item => !voucher.value.productIDs.includes(item.id))
          .reduce((acc, item) => acc + item.cost, 0);

      // Cộng thêm chi phí combo vào tổng giá
      total = voucher.value.comboPrice + otherItemsCost;
    }
  }

  return total;
});

const computedDiscount = computed(() => {
  let discount = 0;

  if (voucher.value && totalCost.value >= voucher.value.minimumOrderValue && totalQuantity.value >= voucher.value.minimumItems) {
    if (voucher.value.voucherType === 'percentage' || voucher.value.voucherType === 'Silver' || voucher.value.voucherType === 'Gold') {
      discount = (totalCost.value * voucher.value.discountValue) / 100;
    }
    else if (voucher.value.voucherType === 'Gold' && voucher.value.description.toLowerCase().includes('freeship')) {
      discount = feeship.value;
    }
    else if (voucher.value.voucherType === 'buy1get1') {
      let totalDiscount = 0;

      const applicableItems = cartItems.value.filter(item => (voucher.value.productIDs.includes(item.id) && (voucher.value.size === item.selectedSize.size)));

      let itemQuantity = 0;
      applicableItems.forEach(item => {
        itemQuantity += cartItems.value
            .filter(cartItem => cartItem.id === item.id)
            .reduce((acc, cartItem) => acc + cartItem.quantity, 0);

        const freeItems = Math.floor(itemQuantity / 2);
        const itemPrice = item.price || 0;
        const sizeSurcharge = item.selectedSize?.surcharge || 0;

        totalDiscount += freeItems * (itemPrice + sizeSurcharge);
      });

      discount = totalDiscount;
    }
    else if (voucher.value.voucherType === 'comboPrice') {
      let totalComboDiscount = 0;

      // Lọc các sản phẩm phù hợp với voucher combo
      const applicableItems = cartItems.value.filter(item => voucher.value.productIDs.includes(item.id));

      if (applicableItems.length > 0) {
        // Lấy số lượng sản phẩm của từng loại trong giỏ hàng
        let productQuantities = {};
        applicableItems.forEach(item => {
          if (!productQuantities[item.id]) {
            productQuantities[item.id] = 0;
          }
          productQuantities[item.id] += item.quantity;
        });

        // Kiểm tra nếu tất cả sản phẩm trong combo đều có mặt trong giỏ hàng
        const allProductsInCombo = voucher.value.productIDs.every(productId => productQuantities[productId] > 0);

        if (allProductsInCombo) {
          // Nếu có tất cả sản phẩm trong combo, tính toán giá trị combo discount
          totalComboDiscount = voucher.value.comboPrice;
          // Nếu có free shipping, cộng thêm phí vận chuyển vào tổng chiết khấu
          if (voucher.value.freeShip) {
            totalComboDiscount += feeship.value;
          }
        }
      }
      discount = totalComboDiscount;
    }
    else {
      discount = parseInt(voucher.value.discountValue);
    }

    // Nếu có miễn phí vận chuyển, cộng thêm phí vận chuyển vào tổng chiết khấu
    if (voucher.value.freeShip) {
      discount += feeship.value;
    }
  }

  return discount;
});

const totalValue = computed(() => {
  // Tính toán tổng chi phí giỏ hàng
  let cost = totalCost.value;
  let discount = computedDiscount.value;

  // Nếu có voucher combo và các sản phẩm khác, tổng giá là comboPrice + giá của sản phẩm khác
  if (voucher.value && voucher.value.voucherType === 'comboPrice') {
    const applicableItems = cartItems.value.filter(item => voucher.value.productIDs.includes(item.id));
    if (applicableItems.length > 0) {
      // Tính toán chi phí các sản phẩm không nằm trong combo
      const otherItemsCost = cartItems.value
          .filter(item => !voucher.value.productIDs.includes(item.id))
          .reduce((acc, item) => acc + item.cost, 0);

      // Cộng thêm chi phí combo vào tổng giá
      cost = voucher.value.comboPrice + otherItemsCost;
    }
  } else {
    // Nếu không có voucher combo, chỉ tính chiết khấu và phí vận chuyển
    cost -= discount;
    cost += feeship.value;
  }

  // Áp dụng chiết khấu từ điểm thưởng khách hàng
  const pointsDiscount = cost * (formData.value.usedCustomerPoints / 100);
  cost -= pointsDiscount;

  return cost;
});


const validatePhoneNumber = (phoneNumber) => {
  return /^[0]{1}[0-9]{9}$/.test(phoneNumber);
};

const handleBlur = async () => {
  const phoneNumber = formData.value.contactDetail.phoneNumber;
  if (validatePhoneNumber(phoneNumber)) {
    ui.value.showError = false;
    if (user.value != {}) {
      const response = await axiosClient.get(`/customers/get-point?phoneNumber=${phoneNumber}`);
      customerPoint.value = response.data;
    }
    if (voucher.value && voucher.value.id) {
      const response = await axiosClient.get(`/vouchers/find-by-phone-number?voucherID=${voucher.value.id}&phoneNumber=${phoneNumber}`);
      if (response.data == 'fail') {
        handleClearVoucher();
      }
    }
  } else {
    ui.value.showError = true;
  }
};

watch(voucher, (newVoucher, oldVoucher) => {
  discount.value = 0; // Reset discount when voucher changes
});

const formData = ref({
  userID: (user.value.id) ? user.value.id : null,
  voucherID: null,
  valueOfVoucher: 0,
  valueOfCustomerPoint: 0,
  totalValue: 0,
  value: 0,
  code: generateRandomString(),
  usedCustomerPoints: 0,
  status: 0,
  products: [],
  paymentMethod: '',
  contactDetail: {
    id: null,
    ownerID: (user.value.id) ? user.value.id : null,
    ownerType: '',
    phoneNumber: (user.value.phoneNumber) ? user.value.phoneNumber : '',
    name: (user.value.lastName) ? (user.value.firstName + ' ' + user.value.lastName) : '',
    address: ''
  }
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

watch([user, voucher, totalCost, totalValue], ([newUser, newVoucher, newTotalCost, newTotalValue]) => {
  formData.value.userID = newUser?.id || null;
  formData.value.voucherID = newVoucher?.id || null;
  formData.value.valueOfVoucher = discount.value;
  formData.value.totalValue = newTotalCost;
  formData.value.value = newTotalValue;
});

watch(cartItems, (newCartItems) => {
  if (newCartItems.length === 0) {
    ui.value.loading = true;
    setTimeout(() => {
      router.push('/'); // Replace 'home' with your actual home route name
    }, 2000);
  }
});

watch(voucher, async (newVoucher) => {
  if (formData.value.contactDetail.phoneNumber) {
    if (newVoucher && newVoucher.id) {
      const response = await axiosClient.get(`/vouchers/find-by-phone-number?voucherID=${newVoucher.id}&phoneNumber=${formData.value.contactDetail.phoneNumber}`);
      if (response.data == 'fail') {
        handleClearVoucher();
      }
    }
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
  dialogVisible: false,
  showError: false
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
  if (!formData.value.contactDetail.name || !address.value || !formData.value.contactDetail.phoneNumber || ui.value.showError) {
    ElMessage({
      message: 'Tên, địa chỉ và số điện thoại không được để trống.',
      type: 'danger',
    })
    return;
  }

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

  formData.value.contactDetail.address = address.value;

  const isAuthenticated = await store.dispatch('checkTokenExpiration');

  if(isAuthenticated) {
    formData.value.contactDetail.ownerID = user.value.id;
    formData.value.contactDetail.ownerType = 'USER';
  }
  else  {
    formData.value.contactDetail.ownerType = 'CUSTOMER';
  }

  formData.value.valueOfCustomerPoint = Math.floor(totalValue.value / 100000);

  if (paymentMethod.value.methodName === 'cash') {
    // Handle cash payment method
  } else {
    try {
      const endpoint = paymentMethod.value.methodName === 'momo' ? 'momo' : 'vnpay';
      // Example axios request
      const pay = await axiosClient.post(`/payment/${endpoint}`, formData.value);
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
