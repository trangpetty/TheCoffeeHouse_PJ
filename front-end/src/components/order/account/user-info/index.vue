<template>
  <div class="container-lg container-fluid">
    <div class="user-info-head">
      <font-awesome-icon icon="fa-solid fa-circle-user" style="font-size: 2rem; color: #FAD207"/>
      <span class="user-info-text">Tài khoản của bạn</span>
    </div>

    <section class="user-info">
      <div class="user-info-left">
        <ul class="user-list">
          <li class="user-item" v-for="(item, index) in tabList" :key="index" @click="selectTab(index, item.tabName)" :class="{ 'active': selectedTab === index }">
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
import { ref, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import accountUser from '@/components/order/account/user-info/UserInfoComponent.vue';
import userAddress from '@/components/order/account/user-info/AddressComponent.vue';
import orderHistory from '@/components/order/account/user-info/OrderHistoryComponent.vue';

const route = useRoute();
const router = useRouter();

const tabList = ref([
  { icon: "fa-solid fa-user", text: "Thông tin tài khoản", component: accountUser, tabName: 'accountUser' },
  { icon: "fa-solid fa-location-dot", text: "Sổ địa chỉ", component: userAddress, tabName: 'userAddress' },
  { icon: "fa-solid fa-clock-rotate-left", text: "Lịch sử mua hàng", component: orderHistory, tabName: 'orderHistory' }
]);

const selectedTab = ref(0);

const selectedComponent = computed(() => {
  return tabList.value[selectedTab.value].component;
});

const selectTab = (index: number, tabName: string) => {
  selectedTab.value = index;
  router.push(`/order/user-info/${tabName}`);
};

watch(
    () => route.params.tabName,
    (newTabName) => {
      const index = tabList.value.findIndex(tab => tab.tabName === newTabName);
      if (index !== -1) {
        selectedTab.value = index;
      }
    },
    { immediate: true }
);
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

.user-list .active,
.user-list .active .icon {
  color: var(--orange-5);
  font-weight: bold;
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
  width: var(--space-18);
}
</style>