import { createRouter, createWebHashHistory } from 'vue-router';
import HomeView from '@/views/manager/HomeView.vue';
import ProductManagerView from '@/views/manager/product/index.vue';
import ProductView from '@/views/manager/product/ProductView.vue';
import ProductTypeView from '@/views/manager/product/ProductTypeView.vue';
import ToppingView from '@/views/manager/product/ToppingView.vue';
import VoucherView from '@/views/manager/voucher/VoucherView.vue';
import CustomerView from '@/views/manager/customer/CustomerView.vue';
import UserView from '@/views/manager/user/UserView.vue';
import BillView from '@/views/manager/bill/BillView.vue';
import HomeUserView from '@/views/user/HomeView.vue';
import OrderView from '@/views/user/OrderView.vue';
import CartView from '@/views/user/CartView.vue';
import BillOrderedView from '@/views/user/BillView.vue';
import LoginView from '@/views/user/LoginView.vue';
import RegisterView from '@/views/user/RegisterView.vue';

export const routes = [
  {
    path: '/admin',
    component: HomeView,
    children: [
      {
        path: '/product',
        name: 'Product',
        component: ProductManagerView,
        children: [
          {
            path: '/product',
            name: 'Product',
            component: ProductView,
          },
          {
            path: '/product-type',
            name: 'Product Type',
            component: ProductTypeView
          },
          {
            path: '/topping',
            name: 'Topping',
            component: ToppingView
          },
        ]
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
      },
      {
        path: '/bill',
        name: 'Bill',
        component: BillView
      }
    ]
  },
  {
    path: '/',
    component: HomeUserView,
    children: [
      {
        path: '/',
        name: 'order',
        component: OrderView
      },
      {
        path: '/checkout',
        name: 'checkout',
        component: CartView
      },
      {
        path: '/checkbill',
        name: 'checkbill',
        component: BillOrderedView
      },
      {
        path: '/login',
        name: 'login',
        component: LoginView
      },
      {
        path: '/register',
        name: 'register',
        component: RegisterView
      },
    ]
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

export default router;
