import { createRouter, createWebHashHistory } from 'vue-router';
import HomeView from '@/views/manager/HomeView.vue';
import ProductView from '@/views/manager/product/ProductView.vue';
import Voucher from '@/views/manager/voucher/VoucherView.vue';
import OrderView from '@/views/user/OrderView.vue';

const routes = [
  {
    path: '/',
    component: HomeView,
    children: [
      {
        path: 'product',
        name: 'Product',
        component: ProductView
      },
      {
        path: 'voucher',
        name: 'Voucher',
        component: Voucher
      }
    ]
  },
  {
    path: '/order',
    name: 'order',
    component: OrderView
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

export default router;
