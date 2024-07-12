import { createRouter, createWebHashHistory } from 'vue-router';
import store from '@/store/index';
import HomeAdminView from '@/views/manager/HomeView.vue';
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
import MainPage from '@/components/user/main/MainPage.vue';
import HomeView from '@/views/HomeView.vue';
import {ElMessageBox} from "element-plus";

export const routes = [
  {
    path: '/admin',
    component: HomeAdminView,
    meta: { requiresAuth: true },
    children: [
      {
        path: '/admin/product',
        name: 'Product',
        component: ProductManagerView,
        children: [
          {
            path: '/admin/product',
            name: 'Product',
            component: ProductView,
          },
          {
            path: '/admin/product-type',
            name: 'Product Type',
            component: ProductTypeView
          },
          {
            path: '/admin/topping',
            name: 'Topping',
            component: ToppingView
          },
        ]
      },
      {
        path: '/admin/voucher',
        name: 'Voucher',
        component: VoucherView
      },
      {
        path: '/admin/customer',
        name: 'Customer',
        component: CustomerView
      },
      {
        path: '/admin/order',
        name: 'User',
        component: UserView
      },
      {
        path: '/admin/bill',
        name: 'Bill',
        component: BillView
      },
      {
        path: '/admin/news',
        name: 'News',
        component: NewsView
      },
      {
        path: '/admin/revenue',
        name: 'Revenue',
        component: RevenueView
      }
    ]
  },
  {
    path: '/order',
    component: HomeUserView,
    children: [
      {
        path: '/order',
        name: 'order',
        component: OrderView
      },
      {
        path: '/order/checkout',
        name: 'checkout',
        component: CartView
      },
      {
        path: '/order/check-bill/:code',
        name: 'check-bill',
        component: BillOrderedView,
        props: true
      },
      {
        path: '/order/login',
        name: 'login',
        component: LoginView
      },
      {
        path: '/order/register',
        name: 'register',
        component: RegisterView
      },
      {
        path: '/order/user-info',
        name: 'user-info',
        component: UserInfoView
      },
      {
        path: '/blogs',
        name: 'blogs',
        component: BlogsView
      },
    ]
  },
  {
    path: '/blog-detail/:id',
    name: 'blog-detail',
    component: BlogDetail,
    props: true
  },
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: { breadcrumb: 'Home' },
    children: [
      {
        path: '',
        name: 'main',
        component: MainPage,
        meta: { breadcrumb: 'Main Page' }
      },
      {
        path: 'product/:id',
        name: 'product',
        component: ProductDetail,
        props: true,
        meta: { breadcrumb: 'Product Detail' }
      },
    ],
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

router.beforeEach(async (to, from, next) => {
  const isAuthenticated = await store.dispatch('checkTokenExpiration');
  const requiresAuth = to.matched.some((record) => record.meta.requiresAuth);

  if (requiresAuth && !isAuthenticated) {
    if (to.path.startsWith('/admin')) {
      ElMessageBox.confirm('Phiên đăng nhập đã hết hạn. Bạn có muốn đăng nhập lại?', 'Phiên Đăng Nhập Hết Hạn', {
        confirmButtonText: 'Đăng nhập lại',
        cancelButtonText: 'Hủy bỏ',
        type: 'warning'
      }).then(() => {
        router.push('/order/login');
      }).catch(() => {
        next(false);
      });
    }
    // else {
    //   next('/order/login'); // Redirect to order login page if order authentication is required and user is not authenticated
    // }
  } else {
    next();
  }
});

export default router;
