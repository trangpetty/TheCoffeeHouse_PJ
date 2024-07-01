import { createRouter, createWebHashHistory } from 'vue-router';
import HomeView from '@/views/manager/HomeView.vue';
import ProductManagerView from '@/components/manager/product/index.vue';
import ProductView from '@/components/manager/product/ProductView.vue';
import ProductTypeView from '@/components/manager/product/ProductTypeView.vue';
import ToppingView from '@/components/manager/product/ToppingView.vue';
import VoucherView from '@/components/manager/voucher/VoucherView.vue';
import CustomerView from '@/components/manager/customer/CustomerView.vue';
import UserView from '@/components/manager/user/UserView.vue';
import BillView from '@/components/manager/bill/BillView.vue';
import NewsView from '@/components/manager/news/index.vue';
import RevenueView from '@/components/manager/revenue/RevenueView.vue';

import HomeUserView from '@/views/OrderView.vue';
import OrderView from '@/components/order/order/OrderView.vue';
import CartView from '@/components/order/order/CartView.vue';
import BillOrderedView from '@/components/order/order/BillView.vue';
import LoginView from '@/components/order/account/LoginView.vue';
import RegisterView from '@/components/order/account/RegisterView.vue';
import UserInfoView from '@/components/order/layout/UserInfoView.vue';
import BlogsView from '@/components/order/news/NewsView.vue';
import BlogDetail from '@/components/order/news/NewsDetail.vue';

import ProductDetail from '@/components/user/ProductDetailView.vue';

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
        path: '/check-bill/:code',
        name: 'check-bill',
        component: BillOrderedView,
        props: true
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
      {
        path: '/user-info',
        name: 'user-info',
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
      {
        path: '/product/:id',
        name: 'product',
        component: ProductDetail,
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
