<template>
  <div>
    <ul>
      <h2>Tổng số tiền đã mua:
        <span class="text-orange">{{ Utils.formatPrice(totalBill) }}</span>
      </h2>
      <el-tabs v-model="status" @tab-click="handleChooseStatus">
        <el-tab-pane label="Đang thực hiện" name="pending"></el-tab-pane>
        <el-tab-pane label="Đã hoàn tất" name="success"></el-tab-pane>
        <el-tab-pane label="Đã hủy" name="fail"></el-tab-pane>
      </el-tabs>
      <div class="card-history-item" v-for="(item, index) in paginatedBills" :key="index" @click="showDetailBill(item)">
        <div class="card-history-head d-flex justify-content-between">
          <p class="card-history-ref">
            {{ item.code }}
          </p>
          <div>
            <el-tag v-if="item.paymentStatus" type="danger" effect="dark">UNPAID</el-tag>
            <el-tag v-else type="success" effect="dark">PAID</el-tag>
          </div>
        </div>
        <div class="card-history-body">
          <div class="product-info my-1">
            <span>{{ item.products[0].productName }}</span>
          </div>
          <span class="total-price">{{ Utils.formatPrice(item.value) }}</span>
        </div>
        <div class="card-history-footer">
          <div class="date-order-wrap">
            <span>{{ new Date(item.modifyTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: false }) }}</span> -
            <span>{{ new Date(item.modifyTime).toLocaleDateString('vi-VN') }}</span>
          </div>
          <div class="user-bean-footer" v-if="item.valueOfCustomerPoint > 0 && !item.paymentStatus">
            + {{ item.valueOfCustomerPoint }} points
          </div>
        </div>
      </div>
    </ul>

    <!-- Dialog -->
    <el-dialog v-model="ui.isDialogVisible" class="rounded" width="420px">
      <el-dialog v-model="ui.isDialogVisible" class="rounded" width="420px">
        <template #header>
          <div class="d-flex align-items-center w-100 pt-3">
            <h4 class="mx-auto my-0">Trạng thái đơn hàng</h4>
          </div>
        </template>
        <div>
          <div class="box-shadow px-4 py-3 mb-3">
            <div class="border-bottom d-flex flex-column">
              <div class="mb-2" v-if="bill.status == 'success'">
                <p class="mb-1">Thời gian giao hàng dự kiến</p>
                <h5 class="mb-0">
                  <span>{{ new Date(bill.modifyTime).toLocaleDateString('vi-VN') }} - </span>
                  <span>{{ new Date(bill.modifyTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: false }) }}</span>
                </h5>
              </div>
              <div class="d-flex align-items-center">
                <div class="me-3 d-flex flex-column">
                  <font-awesome-icon icon="fa-solid fa-circle" class="text-orange" />
                  {{ new Date(bill.modifyTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: false }) }}
                </div>
                <div>
                  <h5 class="text-orange fw-bold">{{ bill.status == 'success' ? 'Hoàn tất' : bill.status == 'fail' ? 'Đơn hàng đã bị hủy' : 'Đơn hàng đang thực hiện' }}</h5>
                  <p class="mb-1" v-if="bill.status == 'success'">Bạn đã nhận đơn hàng thành công. Cảm ơn bạn đã chọn Petty!</p>
                  <p class="mb-1" v-else-if="bill.satus == 'fail'">Đơn hàng của bạn đã được hủy</p>
                </div>
              </div>
            </div>
            <div v-if="bill.rate > 0" class="py-1">
              <p>Cảm ơn bạn đã đánh giá trải nghiệm dành cho đơn hàng này</p>
              <div class="star-rating">
              <span
                  v-for="star in 5"
                  :key="star"
                  :class="{ 'filled': star <= bill.rate }"
              >
              ★
            </span>
              </div>
            </div>
          </div>
          <div class="px-4 py-3 box-shadow mb-3">
            <h4 class="mb-3 fw-bold">Thông tin đơn hàng</h4>
            <div>
              <div class="d-flex border-bottom align-items-center mt-4 pb-3" v-if="bill.contactDetail">
                <div class="w-50 border-end">
                  <p class="mb-1">Người nhận</p>
                  <h5 class="m-0">{{bill.contactDetail.name}}</h5>
                </div>
                <div class="w-50 px-3">
                  <p class="mb-1">Số điện thoại</p>
                  <h5 class="m-0">{{bill.contactDetail.phoneNumber}}</h5>
                </div>
              </div>
              <div class="border-bottom mt-4 pb-3" v-if="bill.contactDetail">
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

          <div class="px-4 py-3 box-shadow mb-3">
            <h4 class="mb-3 fw-bold">Sản phẩm đã chọn</h4>
            <div>
              <div v-for="(item, index) in bill.products" :key="index" class="d-flex mt-4 product-item">
                <h6 class="me-3">{{item.quantityProduct}}</h6>
                <div class="product-details d-flex align-items-center justify-content-between w-100 pb-2">
                  <div>
                    <h6>{{item.productName}}</h6>
                    <p class="m-0 mb-1">{{(item.productSize === 'S')? 'Nho' : (item.productSize === 'M')? 'Vua' : (item.productSize === 'L')? 'Lon' : ''}}</p>
                    <p class="m-0" v-if="item.toppingName"> {{ item.toppingName }} x {{ item.quantityTopping }}</p>
                  </div>
                  <h4 class="mb-0">{{ Utils.formatPrice(item.cost) }}</h4>
                </div>
              </div>
            </div>
          </div>

          <div class="px-4 py-3 box-shadow">
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
              <div class="border-bottom d-flex flex-column mt-4 pb-3" v-if="bill.voucherID || bill.usedCustomerPoints">
                <div class="d-flex align-items-center justify-content-between" v-if="bill.voucherID">
                  <div>
                    <el-text class="h5 text-primary">Khuyến mãi</el-text>
                    <p class="m-0">{{voucher.name}}</p>
                  </div>
                  <h6 class="mb-0">-{{ bill.valueOfVoucher ? Utils.formatPrice(bill.valueOfVoucher) : (voucher.voucherType == 'freeship') ? Utils.formatPrice(voucher.discountValue) :  (voucher.discountValue + '%') }}</h6>
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

        </div>
      </el-dialog>

    </el-dialog>

    <!-- Pagination -->
    <el-pagination
        @current-change="handlePageChange"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="filteredBills.length"
        layout="total, prev, pager, next"
        class="justify-content-center"
    ></el-pagination>
  </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref, nextTick } from 'vue';
import store from '@/store';
import axiosClient from '@/utils/axiosConfig';
import * as Utils from '@/utils/index';
import momo from "@/assets/images/momo.png";
import vnpay from "@/assets/images/vnpay.png";
import cash from "@/assets/images/money.jpg";
import type { TabsPaneContext } from 'element-plus';
import router from "@/router";

const ui = ref({
  isDialogVisible: false
});

const user = computed(() => store.getters.user);
const bills = ref<any[]>([]);
const filteredBills = ref<any[]>([]);
const bill = ref({});
const currentPage = ref(1);
const pageSize = ref(5);
const totalBill = ref(0);
const status = ref('pending');
const voucher = ref({});

const handleChooseStatus = async (tab: TabsPaneContext, event: Event) => {
  await nextTick();
  filterBillsByStatus();
};

const paginatedBills = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredBills.value.slice(start, end);
});

const getBills = async () => {
  const response = await axiosClient.get(`/users/bills/${user.value.id}`);
  bills.value = response.data;
  filterBillsByStatus();
};

const getVoucher = async () => {
  const response1 = await axiosClient.get(`/vouchers/${bill.value.voucherID}`);
  voucher.value = response1.data;
};

const filterBillsByStatus = () => {
  filteredBills.value = bills.value.filter(bill => bill.status === status.value);
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
};

const getTotalBill = async () => {
  const response = await axiosClient.get(`/users/bill-total/${user.value.id}`);
  totalBill.value = response.data;
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

const showDetailBill = (item: object) => {
  if(item.status === 'pending') {
    router.push({ name: 'payment-success', params: { code: item.code } });
  }
  bill.value = item;
  ui.value.isDialogVisible = true;
  if (bill.value.voucherID) {
    getVoucher();
  }
};

onMounted(() => {
  getBills();
  getTotalBill();
});
</script>

<style scoped>
.card-history-item {
  background: #fff;
  border: 1px solid #ededed;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  margin-top: 16px;
  padding: 12px 17px;
}

.card-history-ref {
  background: rgba(255, 231, 186, .6);
  border-radius: 4px;
  color: #f5222d;
  font-size: 12px;
  font-weight: 600;
  padding: 3px;
  text-align: center;
  width: 100px;
}

.product-info,
.total-price {
  color: #262626;
  font-size: 16px;
}

.total-price {
  font-weight: 600;
}

.card-history-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 4px;
}

.date-order-wrap {
  color: rgba(38, 38, 38, .5);
  font-size: 12px;
}

.user-bean-footer {
  color: #52c41a;
  font-size: 12px;
}

.star-rating span {
  font-size: 1.5rem;
  margin-right: 5px;
}

.star-rating span.filled {
  color: var(--orange-2);
}
</style>
