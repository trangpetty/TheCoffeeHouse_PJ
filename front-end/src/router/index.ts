import { createRouter, createWebHashHistory } from 'vue-router';
import HomeView from '@/views/manager/HomeView.vue';
import ProductView from '@/views/manager/product/ProductView.vue';
import VoucherView from '@/views/manager/voucher/VoucherView.vue';
import CustomerView from '@/views/manager/customer/CustomerView.vue';
import UserView from '@/views/manager/user/UserView.vue';
import OrderView from '@/views/user/OrderView.vue';

export const routes = [
  {
    path: '/admin',
    component: HomeView,
    children: [
      {
        path: '/product',
        name: 'Product',
        component: ProductView
      },
      {
        path: '/voucher',
        name: 'Voucher',
        component: VoucherView
      },
      {
        path: '/customer',
        name: 'Customer',
        component: CustomerView
      },
      {
        path: '/user',
        name: 'User',
        component: UserView
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
