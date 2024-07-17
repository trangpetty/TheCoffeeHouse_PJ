<template>
  <div class="container-lg container-fluid">
    <div class="user-info-head">
      <font-awesome-icon icon="fa-solid fa-circle-user" style="font-size: 2rem; color: #FAD207"/>
      <span class="user-info-text">Tài khoản của bạn</span>
    </div>

    <section class="user-info">
      <div class="user-info-left">
        <ul class="user-list">
          <li class="user-item" v-for="(item, index) in tabList" :key="index" @click="selectTab(index)" :class="{ 'active': index === selectedTab }">
            <font-awesome-icon :icon="item.icon" class="icon"/>
            <span class="user-item-txt">{{item.text}}</span>
          </li>
        </ul>
      </div>
      <div class="user-info-right">
        <component :is="selectedComponent" />
      </div>
    </section>
  </div>
</template>

<script lang="ts" setup>
import {computed, onMounted, ref} from 'vue';
import {useStore} from "vuex";
import UserInfoComponent from '@/components/order/account/user-info/UserInfoComponent.vue'
import AddressComponent from '@/components/order/account/user-info/AddressComponent.vue'
import OrderHistoryComponent from '@/components/order/account/user-info/OrderHistoryComponent.vue'

const store = useStore();

const tabList = ref([
  { icon: "fa-solid fa-user", text: "Thông tin tài khoản", component: "UserInfoComponent" },
  { icon: "fa-solid fa-location-dot", text: "Sổ địa chỉ", component: "AddressComponent" },
  { icon: "fa-solid fa-user-shield", text: "Quyền lợi thành viên", component: "" },
  { icon: "fa-solid fa-clock-rotate-left", text: "Lịch sử mua hàng", component: "OrderHistoryComponent" }
]);

const selectedTab = ref(0); // Initialize with the index of the default selected tab

const selectTab = (index: number) => {
  selectedTab.value = index;
};

const selectedComponent = computed(() => {
  return tabList.value[selectedTab.value].component;
});

const formData = ref({
  firstName: '',
  lastName: ''
})

const user = computed(() => store.getters.user);

onMounted(() => {
  formData.value.firstName = user.value.firstName;
  formData.value.lastName = user.value.lastName;
})

</script>

<style scoped>
.user-info-head {
  display: flex;
  justify-content: center;
  padding: 50px;
  text-align: center;
  align-items: center;
}

.user-info-text {
  font-size: 28px;
  font-weight: 600;
  margin-left: 15px;
}

.user-info {
  display: flex;
  margin-top: -70px;
}

.user-info-left {
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, .12);
  padding: 1rem 0;
  height: fit-content;
  width: 375px;
}

.user-info-left, .user-info-right {
  background: #fff;
  margin: 50px auto;
  max-width: 50%;
  padding: 15px 16px;
  position: relative;
}

.user-info-right {
  flex: 1;
}

.user-list {
  list-style-type: none;
  padding: 0px;
}

.user-item {
  align-items: center;
  cursor: pointer;
  display: flex;
  margin-bottom: 20px;
  position: relative;
  padding: 0 1rem;
}

.user-item-txt {
  font-size: 1rem;
  margin-left: 1rem;
}

.user-item:after {
  background: #c4c4c4;
  border: .5px solid;
  content: "";
  opacity: .1;
  position: absolute;
  right: 0;
  top: 140%;
  width: 100%;
}

.user-item .icon {
  font-size: 1.25rem;
  color: #c4c4c4;
}

.user-information {
  position: relative;
  text-align: left;
}

.user-information:before {
  border-bottom: 2px solid #fa8c16;
  bottom: -4px;
  content: "";
  left: 0;
  position: absolute;
  width: 30%;
}

.form-group {
  margin-bottom: 1rem;
}

.radio-input {
  margin: var(--space-6) var(--space-22);
  padding: 0 var(--space-16);
  cursor: pointer;
  width: auto;
  -webkit-print-color-adjust: exact;
  z-index: 1;
  min-height: 1.5rem;
  display: flex;
  align-items: center;
}

.custom-control-input {
  position: static;
  box-sizing: border-box;
  padding: 0 var(--space-16);
  height: var(--space-24);
  width: var(--space-24);
  z-index: -1;
}

.custom-control-input:checked
{
  background: var(--orange-2)!important;
  border-color: var(--orange-2)!important;
}

::v-deep .el-radio__inner {
  width: 1rem!important;
  height: 1rem!important;
}

::v-deep .el-radio__label {
  font-size: 1rem!important;

}

.btn-update {
  border-radius: var(--space-100);
  color: var(--white);
  text-align: center;
}

</style>