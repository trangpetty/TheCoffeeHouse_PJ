<template>
  <el-dialog v-model="isDialogVisible" @close="closeDialog" :show-close="false" class="rounded custom-dialog" width="376px">
    <template #header="{close}">
      <div class="d-flex align-items-center w-100 pt-3" v-if="!ui.showDetail">
        <h4 class="mx-auto my-0 fs-6">Khuyến mãi</h4>
        <font-awesome-icon icon="fa-solid fa-xmark" class="cursor-pointer" @click="close"/>
      </div>
      <div class="d-flex align-items-center w-100 p-3" v-if="ui.showDetail">
        <font-awesome-icon icon="fa-solid fa-chevron-left" @click="ui.showDetail = false" class="cursor-pointer"/>
        <h4 class="mx-auto my-0 fs-6">Chi tiết khuyến mãi</h4>
      </div>
    </template>
    <div v-if="!ui.showDetail">
      <div class="align-items-center mb-3 d-flex position-relative delivery__input p-0">
        <font-awesome-icon icon="fa-solid fa-expand" class="px-3"/>
        <input placeholder="Nhập mã khuyến mãi" class="form-control form-control--custom border-0"/>
        <div class="input-group-prepend">
          <button class="btn btn--smoky-gray rounded-end">Áp dụng</button>
        </div>
      </div>
      <div>
        <div class="sale-header">
          <div class="sale-title">Sẵn sàng sử dụng</div>
        </div>
        <div class="pt-4 pb-5">
          <div class="sale-card mb-2 cursor-pointer" v-for="(item, index) in vouchers" :key="index">
            <div class="d-flex align-items-center voucher-content">
              <div class="sale-card-image d-flex justify-content-center">
                <img :src="item.image" alt="">
              </div>
              <div class="sale-card-content d-flex flex-column justify-content-between">
                <p class="text-description mb-0">{{item.name}}</p>
                <p class="text-expired-time-10days" v-if="(getDaysUntilExpiration(item.applyTo)) < 10">
                  Hết hạn trong {{ getDaysUntilExpiration(item.applyTo) }} ngày
                </p>
                <p class="text-expired-time" v-else>
                  Hết hạn {{ formatDate(item.applyTo) }}
                </p>
                <span class="use-now" @click="handleSelect(item)">Sử dụng ngay</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-if="ui.showDetail">
      <div class="detail-sale-box d-flex flex-column align-items-center">
        <h4 class="detail-sale-box-title mb-1">{{selectedVoucher.name}}</h4>
        <p class="text-expired-time-10days" v-if="(getDaysUntilExpiration(selectedVoucher.applyTo)) < 10">
          {{ getDaysUntilExpiration(selectedVoucher.applyTo) }}
        </p>
        <p class="text-expired-time" v-else>
          {{ formatDate(selectedVoucher.applyTo) }}
        </p>
        <img :src="selectedVoucher.image" alt="" class="detail-sale-box-image">
        <p class="text-user-info mb-0">
          <span class="me-1">{{selectedVoucher.code}}</span>
          <span class="icon cursor-pointer" @click="copyToClipboard(selectedVoucher.code)"><font-awesome-icon icon="fa-solid fa-clone" /></span>
        </p>
        <p class="text-user-info text-black">{{selectedVoucher.description}}</p>
      </div>
      <p class="back cursor-pointer" @click="ui.showDetail = false">Quay về</p>
      <div class="btn btn--orange-1 rounded-5 w-100"><span class="text" @click="handleSelect(selectedVoucher)">Sử dụng ngay</span></div>
    </div>

  </el-dialog>
</template>

<script lang="ts" setup>
import { computed, ref, onMounted } from "vue";
import { useStore } from 'vuex';
import axiosClient from "@/utils/axiosConfig";
import { ElMessage } from 'element-plus'

const store = useStore();

const isDialogVisible = computed(() => store.getters.voucherDialog);
const cartItems = computed(() => store.getters.cartItems);
const user = computed(() => store.getters.user);

const vouchers = ref([]);
const selectedVoucher = ref({});

const ui = ref({
  showDetail: false
});

const closeDialog = () => {
  store.dispatch('VoucherDialog', false);
}

const getVouchers = async () => {
  const response = await axiosClient.get(`/vouchers/all?userID=${user.value.id}`);
  if(response.status == 200) {
    vouchers.value = response.data;
  }
};

const getDaysUntilExpiration = (applyTo: string) => {
  const expiredDate = new Date(applyTo)
  const today = new Date();
  const oneDay = 24 * 60 * 60 * 1000; // milliseconds in one day
  return Math.round((expiredDate.getTime() - today.getTime()) / oneDay);
}

const formatDate = (dateString: string) => {
  const date = new Date(dateString);
  const day = String(date.getDate()).padStart(2, '0');
  const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based
  const year = date.getFullYear();
  return `${day}/${month}/${year}`;
};

const handleSelect = (voucher: Record<string, any>) => {
  const totalCost = cartItems.value.reduce((total, item) => total + item.cost, 0);
  const totalQuantity = cartItems.value.reduce((total, item) => total + item.quantity, 0);
  localStorage.setItem('voucher', JSON.stringify(voucher));
  store.commit('setVoucher', voucher);

  if (totalCost > voucher.minimumOrderValue && totalQuantity >= voucher.minimumItems) {
    store.dispatch('clearErrorMessage');
  } else {
    store.dispatch('setErrorMessage', voucher.errorMessage);
  }

  closeDialog();
};


const copyToClipboard = (text) => {
  // Tạo một textarea tạm thời
  const textarea = document.createElement('textarea');
  textarea.value = text;

  // Thêm textarea vào body để copy được
  document.body.appendChild(textarea);

  // Chọn và copy nội dung
  textarea.select();
  document.execCommand('copy');

  // Xóa textarea sau khi copy
  document.body.removeChild(textarea);
  ElMessage('Copied')
}

onMounted(() => {
  console.log(user)
  if (user.value.id) {
    getVouchers();
  }
});

</script>

<style scoped>
.form-control--custom {
  color: var(--black-3)!important;
  font-size: var(--space-16)!important;
  line-height: var(--space-24)!important;
  background-color: #fafafa!important;
  flex: 1 1 auto;
  margin-bottom: 0;
  min-width: 0;
  position: relative;
  width: 1%!important;
}

.sale-header {
  background-color: var(--smoky-gray-5);
  margin: 0 -16px;
  padding: var(--space-6) var(--space-16);
}

.sale-title {
  color: var(--black-2);
  font-size: var(--space-12);
  font-weight: 600;
  line-height: var(--space-24);
  text-transform: uppercase;
}

.sale-card {
  --sale-card-img: url('@/assets/images/sale-card.png'); /* Fallback for browsers that don't support CSS variables */
  background: var(--sale-card-img) 50%;
  background-size: 100% 100%;
  border-radius: 8px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, .1);
  width: 100%;
}

.sale-card .voucher-content {
  padding: var(--space-20) var(--space-16);
}

.sale-card .sale-card-image {
  border-radius: var(--space-8);
  flex-basis: 36%;
  flex-shrink: 0;
  height: var(--space-80);
  overflow: hidden;
  padding-right: var(--space-16);
  position: relative;
  width: var(--space-80);
}

.sale-card .sale-card-content {
  flex: 1;
  padding-left: var(--space-38);
}

.sale-card .sale-card-content .text-description {
  color: var(--black-3);
  font-size: var(--space-16);
  line-height: var(--space-24);
}

.sale-card .sale-card-content .text-expired-time-10days {
  color: #D2691E;
  font-size: var(--space-14);
  line-height: var(--space-22);
}

.tch-sale-card .tch-sale-card-content .text-expired-time {
  color: var(--black-6);
  font-size: var(--space-14);
  line-height: var(--space-22);
}

.use-now {
  color: #fa8c16;
  cursor: pointer;
  font-size: 16px;
}

.detail-sale-box .detail-sale-box-title {
  font-size: var(--space-18);
  font-weight: 600;
  line-height: var(--space-26);
}

.detail-sale-box .text-expired-time {
  color: var(--black-6);
  font-size: var(--space-14);
  line-height: var(--space-22);
  margin-bottom: var(--space-32);
}

.detail-sale-box .detail-sale-box-image {
  height: 9.5rem;
  margin-bottom: var(--space-12);
  -o-object-fit: cover;
  object-fit: cover;
  width: 9.5rem;
}

.detail-sale-box .text-user-info {
  color: var(--blue);
  font-size: var(--space-14);
  font-weight: 600;
  line-height: var(--space-24);
}

.detail-sale-box .text-user-info .icon {
  color: var(--smoky-gray-6);
  font-size: var(--space-14);
  line-height: 1;
}

.back {
  color: orange;
  font-weight: 600;
  margin-bottom: 30px;
  text-align: center;
}

.btn .text {
  color: var(--white);
  font-size: var(--space-16);
  font-weight: 600;
}

@media (min-width: 576px) {


  .detail-sale-box {
    padding: var(--space-16) 0 var(--space-84);
  }
}
</style>