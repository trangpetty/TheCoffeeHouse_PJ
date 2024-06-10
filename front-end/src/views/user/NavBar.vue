<template>
  <header class="bg-header d-flex align-items-center text-white">
    <div class="container-fluid container-lg d-flex align-items-center px-3 justify-content-center">
      <router-link to="/" class="navbar-brand fw-bolder me-5" href="#">PETTY COFFEE</router-link>
      <div class="header-delivery header-delivery--bg d-flex align-items-center" @click="showAddressDialog">
        <img :src="delivery" alt="" class="icon-delivery">
        <div class="delivery-header_text ps-2">
          <h5 style="font-size: 13px; margin-bottom: 2px; font-weight: 600;">Giao hàng</h5>
          <p class="text-limit-1-line">Tại: {{address}}</p>
        </div>
        <font-awesome-icon icon="fa-solid fa-chevron-down" class="icon-vertor"/>
      </div>
      <div class="me-4">
        <nav class="navbar navbar-expand-lg d-lg-inline">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <a class="nav-link" aria-current="page" href="#">Blog</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" @click="showVoucherDialog">Voucher</a>
            </li>
          </ul>
        </nav>
      </div>
      <div class="d-flex align-items-center ms-auto header-right">
        <a href="#">
          <img :src="noAvatar" style="width: 40px;" alt="">
        </a>
        <router-link :to="(totalQuantity > 0) ? '/checkout' : ''" class="ms-3">
          <div class="icon d-flex align-items-center justify-content-center" :class="(totalQuantity > 0) ? 'icon-cart-has-item' : 'icon-cart'">
            <el-icon class="fs-4 fw-bold"><Handbag /></el-icon>
            <div class="icon-quatity d-flex align-items-center justify-content-center" v-if="totalQuantity > 0">
              <span>{{ totalQuantity }}</span>
            </div>
          </div>
        </router-link>
      </div>
    </div>
  </header>
</template>
<script setup lang="ts">
import delivery from "@/assets/images/Delivery2.png";
import noAvatar from "@/assets/images/no-avatar.png";
import {Handbag} from "@element-plus/icons-vue";
import {computed} from 'vue';
import { useStore } from 'vuex';

const showAddressDialog = () => {
  store.dispatch('openAddressDialog', true);
};

const showVoucherDialog = () => {
  store.dispatch('VoucherDialog', true);
};

const store = useStore();

const totalQuantity = computed(() => store.getters.cartTotalQuantity);
const address = computed(() => store.getters.address);

</script>

<style scoped>
.navbar-expand-lg {
  justify-content: center!important;;
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

.icon-vertor {
  flex-shrink: 0;
  height: var(--space-12);
  width: var(--space-8);
}

@media (min-width: 768px)
{
  header {
    height: var(--space-80);
  }

}


</style>