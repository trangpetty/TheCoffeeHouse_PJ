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
import store from './store'

library.add(fas)
store.dispatch('loadCart');
createApp(App)
.use(router)
.use(ElementPlus)
.use(store)
.component('font-awesome-icon', FontAwesomeIcon)
.mount('#app')
