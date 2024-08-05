<template>
  <header class="bg-header d-flex align-items-center text-white">
    <div class="container-fluid container-lg d-flex align-items-center px-3 justify-content-center">
      <router-link to="/order" class="navbar-brand fw-bolder me-5 d-flex align-items-center" href="#">
        <img :src="cup" style="width: 60px;" class="me-2">
        PETTY COFFEE
      </router-link>
      <div class="header-delivery header-delivery--bg d-flex align-items-center justify-content-between" @click="showAddressDialog">
        <div class="d-flex">
          <img :src="delivery" alt="" class="icon-delivery">
          <div class="delivery-header_text ps-2">
            <h5 style="font-size: 13px; margin-bottom: 2px; font-weight: 600;">Giao hàng</h5>
            <p class="text-limit-1-line">Tại: {{address}}</p>
          </div>
        </div>
        <font-awesome-icon icon="fa-solid fa-chevron-down" class="icon-vertor"/>
      </div>
      <div class="me-4">
        <nav class="navbar navbar-expand-lg d-lg-inline">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <router-link to="/blogs" class="nav-link" aria-current="page" href="#">Tin tức</router-link>
            </li>
            <li class="nav-item">
              <a class="nav-link cursor-pointer" @click="showVoucherDialog">Khuyến mãi</a>
            </li>
          </ul>
        </nav>
      </div>
      <div class="d-flex align-items-center ms-auto header-right">
        <el-dropdown trigger="click" v-if="user.phoneNumber" ref="dropdown">
          <div>
            <img :src="user.avatar ? user.avatar : noAvatar" style="width: 40px; height: 40px" alt="" class="rounded-circle object-fit-cover">
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="handleItemClick('accountUser')">
                <div class="d-flex align-items-center" :class="{ 'active-link': isActive('/order/user-info/accountUser') }">
                  <font-awesome-icon style="width: var(--space-24)" class="fs-5 me-2" icon="fa-solid fa-user" />
                  <span>Thông tin tài khoản</span>
                </div>
              </el-dropdown-item>
              <el-dropdown-item @click="handleItemClick('userAddress')">
                <div class="d-flex align-items-center" :class="{ 'active-link': isActive('/order/user-info/userAddress') }">
                  <font-awesome-icon style="width: var(--space-24)" class="fs-5 me-2" icon="fa-solid fa-location-dot" />
                  <span>Sổ địa chỉ</span>
                </div>
              </el-dropdown-item>
              <el-dropdown-item @click="handleItemClick('orderHistory')">
                <div class="d-flex align-items-center" :class="{ 'active-link': isActive('/order/user-info/orderHistory') }">
                  <font-awesome-icon style="width: var(--space-24)" class="fs-5 me-2" icon="fa-solid fa-clock-rotate-left" />
                  <span>Lịch sử mua hàng</span>
                </div>
              </el-dropdown-item>
              <el-dropdown-item @click="logout">
                <div class="d-flex align-items-center">
                  <font-awesome-icon style="width: var(--space-24)" class="fs-5 me-2" icon="fa-solid fa-arrow-right-from-bracket" />
                  <span>Thoát</span>
                </div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <router-link v-else to="/order/login">
          <img :src="noAvatar" style="width: 40px;height: 40px" alt="" class="rounded-circle object-fit-cover">
        </router-link>
        <span class="name_user">{{user.firstName}} {{user.lastName}}</span>
        <div class="ms-3" @click="navigateToCheckout">
          <div class="icon d-flex align-items-center justify-content-center" :class="(totalQuantity > 0) ? 'icon-cart-has-item' : 'icon-cart'">
            <el-icon class="fs-4 fw-bold"><Handbag /></el-icon>
            <div class="icon-quatity d-flex align-items-center justify-content-center" v-if="totalQuantity > 0">
              <span>{{ totalQuantity }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import cup from "@/assets/images/cup.png";
import delivery from "@/assets/images/Delivery2.png";
import noAvatar from "@/assets/images/no-avatar.png";
import { Handbag } from "@element-plus/icons-vue";
import { computed, ref, onMounted } from 'vue';
import { useStore } from 'vuex';
import { useRoute, useRouter } from "vue-router";

const showAddressDialog = () => {
  store.dispatch('openAddressDialog', true);
};

const showVoucherDialog = () => {
  store.dispatch('VoucherDialog', true);
};

const store = useStore();
const totalQuantity = computed(() => store.getters.cartTotalQuantity);
const user = computed(() => store.getters.user);
const address = computed(() => store.getters.address);
const dropdown = ref(null);
const route = useRoute();
const router = useRouter();

const handleItemClick = (path: string) => {
  if (dropdown.value) {
    dropdown.value.handleClose();
  }
  router.push(`/order/user-info/${path}`);
};

const isActive = (path: string) => {
  return route.path === path;
};

const logout = () => {
  store.dispatch('logout');
  store.dispatch('clearCart');
  router.push('/order/login');
};

const navigateToCheckout = () => {
  if (totalQuantity.value > 0) {
    router.push('/order/checkout');
  }
};

onMounted(() => {
  store.dispatch('loadAddress');
});
</script>

<style scoped>
a {
  color: var(--black);
  text-decoration: none;
}

.navbar-expand-lg {
  justify-content: center!important;
}

header {
  position: sticky;
  background: rgba(255, 255, 255, 0.8);
  top: 0;
  left: 0;
  z-index: 999;
  height: 60px;
  width: 100%;
}

.navbar .navbar-nav .nav-item a {
  color: #fff;
  font-size: 13.5px;
  font-weight: 600;
  line-height: 150%;
}

.header-right .icon {
  border-radius: 50%;
  cursor: pointer;
  height: 40px;
  width: 40px;
  position: relative;
  border: 2px solid #fff;
}

.header-right .icon-cart {
  background: var(--orange-2);
  color: var(--white);
}

.header-right .icon-cart-has-item {
  background: var(--white);
  color: var(--orange-2);
}

.header-right .icon-quatity {
  background-color: red;
  border-radius: 50%;
  height: 16px;
  position: absolute;
  right: 0;
  top: 0;
  width: 16px;
}

.header-right .icon-quatity span {
  color: var(--white);
  font-size: 0.625rem;
  font-weight: 600;
  line-height: 1.125rem;
}

.text-limit-1-line, input::placeholder {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  -webkit-line-clamp: 1;
}

.name_user {
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  height: 14px;
  line-height: 14px;
  margin: 0 1.5rem;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
  display: -webkit-box;
  overflow: hidden;
}

.icon-vertor {
  flex-shrink: 0;
  height: var(--space-12);
  width: var(--space-8);
}

.active-link {
  font-weight: bold;
  color: var(--orange-1);
}

@media (min-width: 768px)
{
  header {
    height: var(--space-80);
  }

}


</style>