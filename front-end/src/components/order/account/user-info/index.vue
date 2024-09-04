<template>
  <div class="container-lg container-fluid">
    <div class="user-info-head">
      <font-awesome-icon icon="fa-solid fa-circle-user" class="text-yellow" style="font-size: 2rem;"/>
      <span class="user-info-text">Tài khoản của bạn</span>
    </div>

    <section class="user-info">
      <div class="user-info-left">
        <div class="box-shadow rounded mb-3">
          <div :class="['user-info-card', userMembershipClass]">
            <div class="user-info-card-header">
              <p class="text-uppercase">{{ user.firstName }}</p>
              <p>{{ user.point }} điểm - {{ user.membershipLevel }}</p>
            </div>
            <div class="user-card-barcode d-flex flex-column align-items-center">
              <canvas id="barcode" class="barcode" />
              <p class="m-0">{{ user.code }}</p>
            </div>
            <img :src="leaves" class="leaves-image">
          </div>
          <p class="px-3 py-2 mb-0" v-if="nextMembershipLevel.pointsNeeded > 0">
            Bạn còn thiếu <span class="text-orange fw-bold"> {{ nextMembershipLevel.pointsNeeded }} </span> điểm để đạt hạng <span class="text-orange fw-bold">{{ nextMembershipLevel.level }}</span>
          </p>
        </div>
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
import { ref, computed, watch, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import accountUser from '@/components/order/account/user-info/UserInfoComponent.vue';
import userAddress from '@/components/order/account/user-info/AddressComponent.vue';
import orderHistory from '@/components/order/account/user-info/OrderHistoryComponent.vue';
import favorProduct from '@/components/order/account/user-info/FavorProductComponent.vue'
import { useStore } from "vuex";
import JsBarcode from 'jsbarcode';
import leaves from '@/assets/images/Leaves.5c9ad83.svg'

const route = useRoute();
const router = useRouter();

const store = useStore();

const user = computed(() => store.getters.user);

const tabList = ref([
  { icon: "fa-solid fa-user", text: "Thông tin tài khoản", component: accountUser, tabName: 'accountUser' },
  { icon: "fa-solid fa-location-dot", text: "Sổ địa chỉ", component: userAddress, tabName: 'userAddress' },
  { icon: "fa-solid fa-clock-rotate-left", text: "Lịch sử mua hàng", component: orderHistory, tabName: 'orderHistory' },
  { icon: "fa-solid fa-bookmark", text: "Sản phẩm yêu thích", component: favorProduct, tabName: 'favorProduct' },
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

const createBarcode = (code: string) => {
  const barcodeElement = document.getElementById('barcode') as HTMLCanvasElement;
  if (barcodeElement) {
    JsBarcode(barcodeElement, code, {
      format: 'CODE128',
      displayValue: false,
    });
  }
}

const userMembershipClass = computed(() => {
  const membershipClasses = {
    'Diamond': 'user-diamond',
    'Gold': 'user-gold',
    'Silver': 'user-silver',
    'Basic': 'user-basic'
  };
  return membershipClasses[user.value.membershipLevel] || 'user-basic';
});

const nextMembershipLevel = computed(() => {
  const points = user.value.point;
  if (points < 10) {
    return { level: 'Silver', pointsNeeded: 10 - points };
  } else if (points < 30) {
    return { level: 'Gold', pointsNeeded: 30 - points };
  } else if (points < 50) {
    return { level: 'Diamond', pointsNeeded: 50 - points };
  } else {
    return { level: 'Diamond', pointsNeeded: 0 }; // Nếu đã là Diamond
  }
});

onMounted(() => {
  createBarcode(user.value.code);
});
</script>

<style scoped>
.user-info-head {
  display: flex;
  justify-content: center;
  padding: 50px;
  text-align: center;
  align-items: center;
}

.user-info-card {
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  min-height: 200px;
  padding: 20px 16px 10px 10px;
  position: relative;
}

.user-basic {
  background: linear-gradient(rgb(240, 240, 240) 0%, rgb(200, 200, 200) 50%);
}

.user-silver {
  background: linear-gradient(rgb(203, 215, 228) 0%, rgb(101, 121, 142) 50%);
}

.user-gold {
  background: linear-gradient(rgb(255, 223, 142) 0%, rgb(255, 166, 0) 50%);
}

.user-diamond {
  background: linear-gradient(rgb(0, 0, 0) 0%, rgb(54, 57, 63) 50%);
}

.user-info-card-header {
  color: #fff;
  font-size: 18px;
  font-weight: 600;
  text-shadow: .254px .508px .508px rgba(0, 0, 0, .25);
}

.user-card-barcode {
  background: #fff;
  border-radius: 8px;
  height: 102px;
  padding: 12px 18px 8px;
  text-align: center;
}

.barcode {
  height: calc(100% - 24px);
  width: 100%;
}

.leaves-image {
  height: 100%;
  -o-object-fit: contain;
  object-fit: contain;
  position: absolute;
  right: 0;
  top: 0;
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
