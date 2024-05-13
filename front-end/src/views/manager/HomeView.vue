<template>
  <div>
    <div class="d-flex">
      <div class="box-shadow">
          <img src="../../assets/images/logo.jpg" alt="" class="img-logo">
          <Sidebar @changeTab="changeTab" class="h-100"/>
      </div>
      <div class="container-fluid">
          <div class="p-3">
            <Component :is="selectedComponent" />
          </div>
      </div>
    </div>
  </div>
</template>

<script>
import Sidebar from '@/views/manager/Sidebar.vue'; // Assuming the path is correct
import Product from '@/views/manager/product/index.vue';
import Voucher from '@/views/manager/voucher/index.vue';

export default {
  components: {
    Sidebar,
    Product,
    Voucher
  },
  data() {
    return {
      selectedComponent: 'Product' // Default selected component
    };
  },
  methods: {
    changeTab(component) {
      this.selectedComponent = this.$options.components[component];
    }
  }
};
</script>

<style>
.img-logo {
  width: 200px;
  padding: 0 30px;
}

.text-logo {
  font-size: 1.5rem;
  margin-top: 5px;
}
</style>
