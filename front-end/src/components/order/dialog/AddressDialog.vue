<template>
  <el-dialog v-model="isDialogVisible" @close="closeDialog" :show-close="false" width="40%" class="rounded-4">
    <ul class="list-delivery-method d-flex justify-content-center">
      <li>
        <div class="deliery-method-card d-flex active">
          <div class="deliery-method-card__image">
            <img :src="delivery" alt="" style="width: 40px">
          </div>
          <div class="deliery-method-card__content d-flex justify-content-center align-items-center">
            <p class="deliery-method-card__title mb-0">
              Giao hàng
            </p>
          </div>
        </div>
      </li>
    </ul>
    <div class="d-flex align-items-stretch flex-wrap w-100 box-shadow-1">
      <el-autocomplete
          v-model="newInputAddress"
          :fetch-suggestions="querySearch"
          placeholder="Vui lòng nhập địa chỉ"
          @select="handleSelect"
          @change="handleChange"
          class="input-address"
      >
      </el-autocomplete>
      <div class="input-group-prepend d-flex">
        <button class="btn border-0">
          <font-awesome-icon icon="fa-solid fa-xmark" />
        </button>
      </div>
    </div>
    <div class="d-flex justify-content-center pt-2">
      <button class="current_address btn-address" @click="closeDialog" v-if="!ui.changeAddress">Chọn địa chỉ này</button>
      <button class="new_address btn-address" @click="newAddress" v-if="!ui.changeAddress">Nhập địa chỉ mới</button>
      <span class="txt_back" v-if="ui.backAddress" @click="backAddress"><font-awesome-icon icon="fa-solid fa-arrow-left" class="me-1" />Quay về địa chỉ cũ</span>
    </div>
  </el-dialog>
</template>

<script setup lang="ts">
import delivery from "@/assets/images/Delivery2.png";
import { computed, ref, watch } from "vue";
import { useStore } from 'vuex';

const ui = ref({
  changeAddress: false,
  backAddress: false
});

const store = useStore();
const newInputAddress = ref('');

const isDialogVisible = computed(() => store.getters.addressDialog);
const address = computed(() => store.getters.address);

watch(address, (newAddress) => {
  newInputAddress.value = newAddress;
});

const closeDialog = () => {
  store.dispatch('openAddressDialog', false);
}

const newAddress = () => {
  store.commit('setAddress', '');
  ui.value.changeAddress = true;
  ui.value.backAddress = true;
}

const backAddress = () => {
  store.commit('setAddress', newInputAddress.value);
  localStorage.setItem('address', newInputAddress.value); // Lưu địa chỉ cũ vào localStorage
  ui.value.changeAddress = false;
  ui.value.backAddress = false;
}

const handleChange = () => {
  ui.value.changeAddress = true;
}

const querySearch = async (queryString, cb) => {
  if (!queryString) {
    cb([]);
    return;
  }

  try {
    const response = await axiosClient.get('https://nominatim.openstreetmap.org/search', {
      params: {
        q: queryString,
        format: 'json',
        addressdetails: 1,
        countrycodes: 'VN',
        viewbox: '105.764641,21.090274,105.969697,20.885329',
        bounded: 1
      }
    });

    cb(response.data.map(place => ({
      value: place.display_name,
      ...place
    })));
  } catch (error) {
    console.error('Error fetching suggestions:', error);
    cb([]);
  }
};

const handleSelect = (item) => {
  console.log("Selected place:", item.value);
  store.dispatch('updateAddress', item.value);
  newInputAddress.value = item.value;
  ui.value.backAddress = false;
  localStorage.setItem('address', item.value); // Lưu địa chỉ mới vào localStorage
};

</script>

<style scoped>
.list-delivery-method {
  background: #fff;
  border: 1px dashed #fb8d17;
  border-radius: 100px;
  left: 50%;
  list-style: none;
  padding: var(--space-8);
  position: absolute;
  top: -50%;
  transform: translateY(20px) translateX(-50%);
  width: 165px;
  z-index: 100;
}

.deliery-method-card {
  border-radius: var(--space-100);
  padding: var(--space-6);
  padding-right: var(--space-12);
}

.deliery-method-card.active {
  background: var(--radial-gradient-orange-10);
  box-shadow: 0 4px 8px rgba(0, 0, 0, .2);
}

.deliery-method-card .deliery-method-card__image {
  border-radius: 100%;
  flex-shrink: 0;
  height: var(--space-44);
  width: var(--space-44);
}

.deliery-method-card .deliery-method-card__content {
  flex: 1;
  padding-left: var(--space-8);
}

.deliery-method-card.active .deliery-method-card__content .deliery-method-card__title {
  color: #fff;
}

.deliery-method-card .deliery-method-card__content .deliery-method-card__title {
  color: var(--orange-2);
  font-size: var(--space-16);
  font-weight: 600;
  line-height: var(--space-22);
}

.el-dialog__body {
  padding: var(--space-56) var(--space-20) var(--space-20);
}

::v-deep .input-address {
  flex: 1 1 auto;
  margin-bottom: 0;
  min-width: 0;
  position: relative;
  width: 1%;
  color: var(--black-3);
  font-size: var(--space-16);
  line-height: var(--space-24);
  padding: .375rem .75rem;
}

::v-deep .input-address .el-input__wrapper {
  box-shadow: none!important;
}

.input-address:focus {
  box-shadow: none;
  outline: none;
}

.current_address {
  background: #e57905;
  border: none;
  border-radius: 8px;
  color: #fff;
  margin-right: 10px;
}

.current_address,
.new_address {
  font-size: 16px;
  font-weight: 600;
}

.new_address {
  background: #fff;
  border: 1px solid rgba(0, 0, 0, .15);
  border-radius: 8px;
  color: rgba(0, 0, 0, .85);
}

.btn-address {
  height: 40px;
  padding: 8px 12px;
  width: 200px;
}

.txt_back {
  color: rgba(0, 0, 0, .85);
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  margin-left: 5px;
  margin-top: 2px;
}
</style>
