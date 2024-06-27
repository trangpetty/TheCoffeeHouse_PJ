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
import NewsView from '@/views/manager/news/index.vue';
import RevenueView from '@/views/manager/revenue/RevenueView.vue';

import HomeUserView from '@/views/order/HomeView.vue';
import OrderView from '@/views/order/order/OrderView.vue';
import CartView from '@/views/order/order/CartView.vue';
import BillOrderedView from '@/views/order/order/BillView.vue';
import LoginView from '@/views/order/account/LoginView.vue';
import RegisterView from '@/views/order/account/RegisterView.vue';
import UserInfoView from '@/views/order/layout/UserInfoView.vue';
import BlogsView from '@/views/order/news/NewsView.vue';
import BlogDetail from '@/views/order/news/NewsDetail.vue';

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
        path: '/order',
        name: 'User',
        component: UserView
      },
      {
        path: '/bill',
        name: 'Bill',
        component: BillView
      },
      {
        path: '/news',
        name: 'News',
        component: NewsView
      },
      {
        path: '/revenue',
        name: 'Revenue',
        component: RevenueView
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
        path: '/account',
        name: 'login',
        component: LoginView
      },
      {
        path: '/register',
        name: 'register',
        component: RegisterView
      },
      {
        path: '/order-info',
        name: 'order-info',
        component: UserInfoView
      },
      {
        path: '/blogs',
        name: 'blogs',
        component: BlogsView
      },
      {
        path: '/blog-detail/:id',
        name: 'blog-detail',
        component: BlogDetail,
        props: true
      },
    ]
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

export default router;
