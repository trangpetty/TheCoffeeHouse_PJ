import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { library } from '@fortawesome/fontawesome-svg-core'
import { fas } from '@fortawesome/free-solid-svg-icons'
import { far } from '@fortawesome/free-regular-svg-icons'
import store from './store'
import axios from 'axios';
import refreshToken from './utils/refreshToken';

axios.interceptors.response.use(
    response => response,
    async error => {
        const originalRequest = error.config;

        if (error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;
            try {
                const newToken = await refreshToken();
                axios.defaults.headers.common['Authorization'] = 'Bearer ' + newToken;
                originalRequest.headers['Authorization'] = 'Bearer ' + newToken;
                return axios(originalRequest);
            } catch (e) {
                return Promise.reject(e);
            }
        }
        return Promise.reject(error);
    }
);

library.add(fas)
library.add(far)
createApp(App)
.use(router)
.use(ElementPlus)
.use(store)
.component('font-awesome-icon', FontAwesomeIcon)
.mount('#app')
