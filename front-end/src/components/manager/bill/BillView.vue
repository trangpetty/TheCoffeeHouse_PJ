<template>
  <div>
    <div class="box box-shadow mb-3">
      <el-form label-width="auto" :model="queryForm">
        <el-row>
          <el-col :span="8">
            <el-form-item label="Code">
              <el-input
                  v-model="queryForm.code"
                  class="input"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Status">
              <el-select v-model="queryForm.status" class="input">
                <el-option label="All" value=""/>
                <el-option label="Created" value="created"/>
                <el-option label="Pending" value="pending"/>
                <el-option label="Success" value="success"/>
                <el-option label="Fail" value="fail"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="From">
              <el-date-picker
                  v-model="queryForm.applyFrom"
                  type="date"
                  style="width: 100%"
                  value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="To">
              <el-date-picker
                  v-model="queryForm.applyTo"
                  type="date"
                  style="width: 100%"
                  value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" @click="fetchData">Search</el-button>
          <el-button type="primary" @click="resetForm">Reset</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="box box-shadow">
      <el-table :data="tableData" stripe v-loading="ui.loading" highlight-current-row>
        <el-table-column type="index" label="#"/>.
        <el-table-column prop="code" label="Code" />
        <el-table-column prop="totalValue" label="Total" />
        <el-table-column prop="value" label="Value" />
        <el-table-column label="status">
          <template #default={row}>
            <span :style="{ color: row.status === 'success' ? '#5daf34' : (row.status === 'pending' ? 'orange' : 'red') }">
              {{ row.status }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="payment">
          <template #default={row}>
            <span :style="(row.paymentStatus === 0) ? 'color:#5daf34' : (row.paymentStatus === 1) ? 'color:red' : 'color:orange'">
              {{ (row.paymentStatus === 0) ? 'Success' : (row.paymentStatus === 1) ? 'Fail' : 'Pending'}}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="paymentMethod" label="Payment Method" />
        <el-table-column prop="deliveryStatus" label="Delivery Status" />
        <el-table-column prop="createTime" label="Create Time" />
        <el-table-column prop="modifyTime" label="Modify Time" />
        <el-table-column>
          <template #default={row}>
            <el-button type="primary" circle size="small" @click.stop="handleDetail(row)">
              <font-awesome-icon icon="fa-solid fa-info" />
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Dialog -->
      <el-dialog v-model="ui.dialogVisible" width="65%" class="dialog" title="Detail">
        <el-form label-width="120px">
          <el-row>
            <el-col :span="12" class="d-flex">
              <el-form-item :label="bill.userID ? 'User ID:' : 'Customer ID:'" v-if="bill.userID || bill.customerID">
                <p>{{ bill.userID ? bill.userID : bill.customerID }}</p>
              </el-form-item>
              <el-form-item label="Name:" v-if="bill.contactDetail">
                <p>{{ bill.contactDetail.name }}</p>
              </el-form-item>
            </el-col>
            <el-col :span="12" class="d-flex" v-if="bill.contactDetail">
              <el-form-item label="Phone Number:">
                <p>{{ bill.contactDetail.phoneNumber }}</p>
              </el-form-item>
              <el-form-item label="Owner Type:">
                <p>{{ bill.contactDetail.ownerType }}</p>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
              <el-form-item label="Address:" v-if="bill.contactDetail">
                <p>{{ bill.contactDetail.address }}</p>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-table :data="bill.products" class="w-100 mb-3">
          <el-table-column prop="productName" label="Product" width="200" header-align="center" />
          <el-table-column prop="quantityProduct" label="Quantity" align="center"/>
          <el-table-column prop="productSize" label="Size" align="center" />
          <el-table-column prop="priceProduct" label="Price" align="center"/>
          <el-table-column prop="surcharge" label="Surcharge" align="center"/>
          <el-table-column prop="toppingName" label="Topping" width="200" header-align="center"/>
          <el-table-column prop="quantityTopping" label="Quantity" align="center"/>
          <el-table-column prop="priceTopping" label="Price" align="center"/>
          <el-table-column prop="cost" label="Cost" />
        </el-table>

        <el-row class="px-3 justify-content-between">
          <el-col :span="12" class="d-flex">
            <p class="me-3">Comment: {{ bill.comment }}</p>
            <p>Rate: {{ bill.rate }}</p>
          </el-col>
          <el-col :span="6">
            <div class="bill-details">
              <p class="text-end"><span>Total:</span> <span>{{ bill.totalValue }}</span></p>
              <p class="text-end" v-if="bill.voucherID"><span>Voucher ID:</span> <span>{{ bill.voucherID }}</span></p>
              <p class="text-end" v-if="bill.valueOfVoucher"><span>Discount:</span> <span>- {{ bill.valueOfVoucher }}</span></p>
              <p class="text-end" v-if="bill.usedCustomerPoints"><span>Discount:</span> <span>- {{ bill.usedCustomerPoints }}%</span></p>
              <p class="text-end"><span>Total:</span> <span>{{ bill.value }}</span></p>
            </div>
          </el-col>
        </el-row>
      </el-dialog>

      <!-- Pagination -->
      <el-pagination class="pagination" v-model:current-page="currentPage" layout="total, prev, pager, next" :total="total" @current-change="fetchData" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import {  ref } from 'vue';
import axiosClient from '@/utils/axiosConfig';
import { connectWebSocket } from '@/services/websocketService';
import {IMessage} from "@stomp/stompjs";

const ui = ref({
  dialogVisible: false,
  loading: false,
  addRecord: false,
  userRole: false,
  adminRole: false,
  nameDisable: false,
});

const queryForm = ref({
  code: '',
  status: '',
  applyFrom: null,
  applyTo: null
});

const bill = ref<any>({})

const currentPage = ref(1);
const total = ref(0);
const tableData = ref([]);

const fetchData = async () => {
  try {
    ui.value.loading = true;
    let response: object
    response = await axiosClient.get('/bills', {
      params: {
        code: queryForm.value.code,
        status: queryForm.value.status,
        applyFrom: queryForm.value.applyFrom,
        applyTo: queryForm.value.applyTo,
        pageNo: currentPage.value
      }
    });
    tableData.value = response.data.content;
    total.value = response.data.totalElements;
    ui.value.loading = false;
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};

const resetForm = () => {
  queryForm.value.name = '';
  queryForm.value.status = null;
  queryForm.value.applyFrom = null;
  queryForm.value.applyTo = null;
  currentPage.value = 1;
  fetchData();
}

const handleDetail = async (row: object) => {
  console.log('Detail button clicked');
  ui.value.dialogVisible = true;
  bill.value = row
  console.log(bill.value)
  // for(const item of bill.value.products) {
  //   let product = await axiosClient.get(`/products/${item.productID}`);
  //   item.productName = product.data.name;
  //   item.price = product.data.price;
  // }
}

fetchData()

const handleBillMessage = (message: IMessage) => {
  const updatedBill = JSON.parse(message.body);  // Parse thông tin hóa đơn từ WebSocket

  // Kiểm tra xem hóa đơn đã có trong tableData chưa
  const existingBillIndex = tableData.value.findIndex(bill => bill.id === updatedBill.id);

  if (existingBillIndex !== -1) {
    // Nếu hóa đơn đã có, cập nhật trạng thái mới
    tableData.value[existingBillIndex] = updatedBill;
  } else {
    // Nếu hóa đơn mới, thêm vào đầu tableData
    tableData.value.unshift(updatedBill)
  }
  ;
}

  connectWebSocket({
    '/topic/bills': handleBillMessage
  });

</script>

<style scoped>
.text-end {
  display: flex;
  justify-content: space-between;
}
</style>
