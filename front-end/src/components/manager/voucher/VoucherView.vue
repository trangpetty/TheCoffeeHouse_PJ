<template>
  <div>
    <div class="box box-shadow mb-3">
      <el-form label-width="auto" :model="queryForm">
        <el-row>
          <el-col :span="8">
            <el-form-item label="Name">
              <el-input
                  v-model="queryForm.name"
                  class="input"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Type">
              <el-select v-model="queryForm.status" class="input">
                <el-option label="Active" :value="0"/>
                <el-option label="Inactive" :value="1"/>
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
                  @change="checkDate"
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
                  @change="checkDate"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" @click="fetchData">Search</el-button>
          <el-button type="primary" @click="resetForm">Reset</el-button>
          <el-button type="primary" @click="handleAdd">
            <i class="el-icon-circle-plus-outline"/><span>Add</span>
          </el-button>
          <el-button type="primary" @click="handleAddType">
            <span>Type</span>
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="box box-shadow">
      <el-table :data="tableData" stripe v-loading="ui.loading" highlight-current-row>
        <el-table-column type="index" label="#"/>.
        <el-table-column prop="name" label="Name" />
        <el-table-column label="status">
          <template #default={row}>
            <span :style="row.status === 0 ? 'color:#5daf34':'color:red'">
              {{ row.status === 0 ? 'Active' : 'Inactive' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="applyFrom" label="From" />
        <el-table-column prop="applyTo" label="To" />
        <el-table-column prop="createTime" label="Create Time" />
        <el-table-column prop="modifyTime" label="Modify Time" />
        <el-table-column label="Image">
          <template #default={row}>
            <el-image :src="row.image" class="w-50"/>
          </template>
        </el-table-column>
        <el-table-column>
          <template #default={row}>
            <el-button type="success" size="small" @click="handleEditRow(row)">Update</el-button>
            <el-button type="danger" size="small" @click="handleDeleteRow(row.id)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Dialog Voucher -->
      <el-dialog v-model="ui.dialogVisible" width="60%" class="dialog" :title="ui.addRecord?'Add voucher':'Update voucher'">
        <el-form :model="formData" label-width="auto">
          <el-input v-model="voucher_id" class="d-none" />
          <el-row>
            <el-col :span="12">
              <el-form-item label="Name">
                <el-input v-model="formData.name" />
              </el-form-item>
              <el-form-item label="Code">
                <el-input v-model="formData.code" />
              </el-form-item>
              <el-form-item label="Type">
                <el-select v-model="formData.voucherTypeID" @change="handleVoucherTypeChange">
                  <el-option v-for="(item, index) in types" :key="index" :label="item.type" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="Discount Value">
                <el-input-number v-model="formData.discountValue" :min="0" />
              </el-form-item>
              <el-form-item label="Max Discount">
                <el-input v-model="formData.discountMax" :formatter="(value) => `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')" :parser="(value) => value.replace(/\$\s?|(,*)/g, '')" />
              </el-form-item>
              <el-form-item label="Minimum Order Value">
                <el-input v-model="formData.minimumOrderValue" :formatter="(value) => `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')" :parser="(value) => value.replace(/\$\s?|(,*)/g, '')" />
              </el-form-item>
              <el-form-item label="Minimum Items">
                <el-input-number v-model="formData.minimumItems" :min="0" />
              </el-form-item>
              <el-form-item label="Max Uses">
                <el-input-number v-model="formData.maxUses" :min="0" :step="1" />
              </el-form-item>
              <el-form-item label="Current Uses">
                <el-input-number v-model="formData.currentUses" :min="0" :step="1" />
              </el-form-item>
              <el-form-item label="Status" v-if="!ui.addRecord">
                <el-switch
                    v-model="formData.status"
                    class="ml-2"
                    style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                />
              </el-form-item>
              <el-form-item label="Error Message">
                <el-input v-model="formData.errorMessage" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item v-if="visibleItems.includes('comboPrice')" label="Combo Price">
                <el-input v-model="formData.comboPrice" :formatter="(value) => `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')" :parser="(value) => value.replace(/\$\s?|(,*)/g, '')" />
              </el-form-item>
              <el-form-item v-if="visibleItems.includes('comboPrice')" label="Combo Quantity">
                <el-input-number v-model="formData.comboQuantity" :min="0" :step="1" />
              </el-form-item>
              <el-form-item v-if="visibleItems.includes('fixedPrice')" label="Fixed Price">
                <el-input v-model="formData.fixedPrice" :formatter="(value) => `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')" :parser="(value) => value.replace(/\$\s?|(,*)/g, '')" />
              </el-form-item>
              <el-form-item label="Enable Freeship">
                <el-switch
                    v-model="formData.freeShip"
                    class="ml-2"
                    style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                />
              </el-form-item>
              <el-form-item v-if="visibleItems.includes('buy1get1')" label="Enable Buy 1 Get 1">
                <el-switch
                    v-model="formData.buy1Get1"
                    class="ml-2"
                    style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                />
              </el-form-item>
              <el-form-item v-if="visibleItems.includes('buy1get1') || visibleItems.includes('fixedPrice') || visibleItems.includes('comboPrice')" label="Type">
                <el-select v-model="selectedProductType" @change="getProducts">
                  <el-option v-for="item in productTypes" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item v-if="visibleItems.includes('buy1get1') || visibleItems.includes('fixedPrice') || visibleItems.includes('comboPrice')" label="Products">
                <el-select v-model="selectedProducts" multiple>
                  <el-option v-for="item in products" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item v-if="visibleItems.includes('buy1get1') || visibleItems.includes('fixedPrice') || visibleItems.includes('comboPrice')" label="Size">
                <el-radio-group v-model="selectedSize">
                  <el-radio label="S">S</el-radio>
                  <el-radio label="M">M</el-radio>
                  <el-radio label="L">L</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="Apply">
                <el-date-picker
                    v-model="dateRange"
                    type="daterange"
                    range-separator="To"
                    start-placeholder="Start date"
                    end-placeholder="End date"
                    @change="handleDateChange"
                />
              </el-form-item>
              <el-form-item label="Description">
                <el-input type="textarea" v-model="formData.description" :rows="6"/>
              </el-form-item>
              <el-form-item label="Image">
                <el-upload action="#" list-type="picture-card" :auto-upload="false" :file-list="fileList" :on-change="handleUploadChange" :limit="1">
                  <template v-if="fileList.length < 1" #trigger>
                    <el-icon><Plus /></el-icon>
                  </template>
                  <template #file="{ file }">
                    <div>
                      <img class="el-upload-list__item-thumbnail" :src="file.url" alt="" />
                      <span class="el-upload-list__item-actions">
                        <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
                          <el-icon><ZoomIn /></el-icon>
                        </span>
                        <span class="el-upload-list__item-delete" @click="openFileInput(file)">
                          <el-icon><Edit /></el-icon>
                        </span>
                      </span>
                    </div>
                  </template>
                </el-upload>

                <el-dialog v-model="dialogVisible">
                  <img :src="dialogImageUrl" alt="" width="100%" />
                </el-dialog>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div class="text-end">
          <el-button @click="ui.dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleConfirm">Confirm</el-button>
        </div>
      </el-dialog>

      <!-- Dialog Type -->
      <el-dialog v-model="ui.typeDialog"  width="40%" class="dialog" title="Add type">
        <el-form :model="formType" label-width="auto">
          <el-form-item label="Type">
            <el-input v-model="formType.type" />
          </el-form-item>
          <el-form-item label="Description">
            <el-input type="textarea" v-model="formType.description" :rows="6"/>
          </el-form-item>
        </el-form>
        <div class="text-end">
          <el-button @click="ui.typeDialog = false">Cancel</el-button>
          <el-button type="primary" @click="handleConfirmType">Confirm</el-button>
        </div>
      </el-dialog>

      <!-- Pagination -->
      <el-pagination class="pagination" v-model:current-page="currentPage" layout="total, prev, pager, next" :total="total" @current-change="fetchData" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import {computed, onMounted, ref, watch} from 'vue';
import axiosClient from '@/utils/axiosConfig';
import { Edit, Plus, ZoomIn } from "@element-plus/icons-vue";
import {uploadFileToFirebaseAndGetURL} from "@/utils";
import moment from 'moment';
import * as Utils from '@/utils';

const dialogImageUrl = ref('');
const dialogVisible = ref(false);
const fileList = ref([]);
const dateRange = ref([]);

const ui = ref({
  dialogVisible: false,
  loading: false,
  addRecord: false,
  typeDialog: false
});

const queryForm = ref({
  name: '',
  status: 0,
  applyFrom: null,
  applyTo: null
});

const formData = ref({
  voucherTypeID: null,
  name: '',
  code: '',
  description: '',
  image: '',
  discountValue: 0,
  minimumOrderValue: 0,
  minimumItems: 0,
  maxUses: 0,
  currentUses: 0,
  status: 0,
  errorMessage: '',
  comboPrice: 0,
  fixedPrice: 0,
  productType: '',
  freeShip: false,
  buy1Get1: false,
  discountMax: 0,
  comboQuantity: 0,
  applyFrom: null,
  applyTo: null
})

const formType = ref({
  type: '',
  description: ''
})

const selectedProducts = ref([]);
const selectedSize = ref('M');

const types = ref([]);

const currentPage = ref(1);
const total = ref(0);
const tableData = ref([]);
const voucher_id = ref(0);
const products = ref([]);
const productTypes = ref([]);
const selectedProductType = ref(1);
const visibleItems = ref<string[]>([]);

const fetchData = async () => {
  try {
    ui.value.loading = true;
    let response: object
    response = await axiosClient.get('/vouchers', {
      params: {
        name: queryForm.value.name,
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

const getTypes = async () => {
  const result =  await axiosClient.get('/vouchers/type')
  if(result.status == 200) {
    types.value = result.data;
  }
}

const updateVisibleItems = () => {
  const voucherType = types.value.find(type => type.id === formData.value.voucherTypeID);
  if (voucherType) {
    switch (voucherType.type) {
      case 'comboPrice':
        visibleItems.value = ['comboPrice'];
        break;
      case 'fixedPrice':
        visibleItems.value = ['fixedPrice'];
        break;
      case 'buy1get1':
        visibleItems.value = ['buy1get1'];
        break;
      default:
        visibleItems.value = [];
        break;
    }
  } else {
    visibleItems.value = [];
  }
  console.log('Visible Items:', visibleItems.value); // Debugging line
};

const handleVoucherTypeChange = (newValue) => {
  updateVisibleItems(); // Call this function to update visible items based on the new value
  if (types.value.find(type => type.id === newValue)?.type === 'buy1get1' || types.value.find(type => type.id === newValue)?.type === 'fixedPrice') {
    getProductTypes();
  }
};


const getProductTypes = async () => {
  const response = await axiosClient.get('/product-type');
  productTypes.value = response.data;
};

const getProducts = async () => {
  try {
    if (selectedProductType.value) {
      const result = await axiosClient.get(`/products/name/${selectedProductType.value}`);
      if (result.status === 200) {
        products.value = result.data;
        console.log('Products fetched:', products.value);  // Debugging line
      }
    }
  } catch (error) {
    console.error('Failed to fetch products:', error);
  }
};

const handleUploadChange = (file, newFileList) => {
  fileList.value = newFileList.map(f => ({
    ...f,
    uid: f.uid || Date.now() // Ensure each file has a unique identifier
  }));
}

const handlePictureCardPreview = (file) => {
  dialogImageUrl.value = file.url;
  dialogVisible.value = true;
};

const handleDateChange = (value) => {
  if (value && value.length === 2) {
    formData.value.applyFrom = moment(value[0]).format('YYYY-MM-DDTHH:mm:ss');
    formData.value.applyTo = moment(value[1]).format('YYYY-MM-DDTHH:mm:ss');
  } else {
    formData.value.applyFrom = '';
    formData.value.applyTo = '';
  }
}

const handleConfirm = async () => {
  ui.value.dialogVisible = false;
  ui.value.loading = true;

  formData.value.productType = selectedProductType;
  if (ui.value.addRecord) {
    for (const file of fileList.value) {
      try {
        formData.value.image = await uploadFileToFirebaseAndGetURL(file.raw, 'vouchers');
      } catch (error) {
        console.error('Error uploading file:', error);
      }
    }
    console.log(typeof(formData.value.applyFrom))
    await axiosClient.post('/vouchers', {
      voucherDto: formData.value,
      productIDs: selectedProducts.value,
      size: selectedSize.value,
    });

  } else {
    formData.value.status = formData.value.status ? 0 : 1;
    await axiosClient.put(`/vouchers/${voucher_id.value}`, {
      voucherDto: formData.value,
      productIDs: selectedProducts.value,
      size: selectedSize.value,
    });
  }
  ui.value.loading = false;
  await fetchData();
};

const handleConfirmType = async () => {
  ui.value.typeDialog = false;
  await axiosClient.post('/vouchers/type', formType.value);
  getTypes();
}

const resetForm = () => {
  queryForm.value.name = '';
  queryForm.value.status = null;
  queryForm.value.applyFrom = null;
  queryForm.value.applyTo = null;
  currentPage.value = 1;
  fetchData();
}

const handleAddType = () => {
  ui.value.typeDialog = true;
  formType.value.type = '';
  formType.value.description = '';
}

const handleAdd = () => {
  ui.value.addRecord = true;
  ui.value.dialogVisible = true;
  formData.value.name = '';
  formData.value.image = '';
  formData.value.code = '';
  formData.value.description = '';
  formData.value.status = 0;
  formData.value.applyFrom = null;
  formData.value.applyTo = null;
  dateRange.value = null;
  fileList.value = [];
}

const handleEditRow = (row: object) => {
  ui.value.dialogVisible = true;
  ui.value.addRecord = false;
  voucher_id.value = row.id
  formData.value.name = row.name;
  formData.value.description = row.description;
  formData.value.code = row.code;
  formData.value.discountValue = row.discountValue;
  formData.value.image = row.image;
  formData.value.status = !row.status;
  formData.value.applyFrom = row.applyFrom;
  formData.value.applyTo = row.applyTo;
  dateRange.value[0] = row.applyFrom;
  dateRange.value[1] = row.applyTo;
  formData.value.minimumOrderValue = row.minimumOrderValue;
  formData.value.minimumItems = row.minimumItems;
  formData.value.voucherTypeID = row.voucherTypeID;
  formData.value.maxUses = row.maxUses;
  formData.value.currentUses = row.currentUses;
  formData.value.errorMessage = row.errorMessage;
  formData.value.buy1Get1 = row.buy1Get1;
  formData.value.fixedPrice = row.fixedPrice;
  formData.value.freeShip = row.freeShip;
  formData.value.productType = row.productType;
  formData.value.comboQuantity = row.comboQuantity;
  formData.value.comboPrice = row.comboPrice;
  products.value = row.productIDs;
  selectedSize.value = row.size;
  selectedProducts.value = row.productIDs;
  fileList.value = [{
    url: formData.value.image
  }]
  console.log(typeof(row.applyFrom))
}

const handleDeleteRow = async (id: number) => {
  let confirmed = await Utils.confirm("This action will delete this record! Confirm to delete？", "Confirm");
  if(!confirmed) {
    return;
  }
  await axiosClient.delete(`/vouchers/${id}`);
  await fetchData();
}

const openFileInput = (file) => {
  const input = document.createElement('input');
  input.type = 'file';
  input.accept = 'image/*';
  input.style.display = 'none';
  input.addEventListener('change', (event) => {
    handleFileInputChange(event, file);
  });
  document.body.appendChild(input);
  input.click();
  document.body.removeChild(input);
};

const handleFileInputChange = async (event, fileToEdit) => {
  const input = event.target;
  if (input.files && input.files.length > 0) {
    const newFile = input.files[0];
    const downloadURL = await uploadFileToFirebaseAndGetURL(newFile, 'vouchers');
    formData.value.image = downloadURL;
    const fileIndex = fileList.value.findIndex(f => f.uid === fileToEdit.uid);
    if (fileIndex !== -1) {
      fileList.value[fileIndex] = { ...fileToEdit, raw: newFile, url: downloadURL };
    }
  }
};

onMounted(() => {
  fetchData();
  getTypes();
})

watch(() => formData.value.voucherTypeID, updateVisibleItems);
watch(() => selectedProductType.value, getProducts);

</script>

