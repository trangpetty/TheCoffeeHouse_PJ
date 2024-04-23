<template>
    <el-dialog v-model="props.visible" width="500" align-center class="text-center">
      <h2>{{ props.name }}</h2>
      <nav class="nav my-3 justify-content-center">
          <button v-for="size in ['S', 'M', 'L']" :key="size" class="nav-link btn mx-2 border" @click="getPriceProduct(props.name, size)">
            <span class="h5">{{ size }}</span>
          </button>
      </nav>
      <span class="h3">Price: {{ productSelected.price }}</span>
      <div class="my-3 d-flex align-items-center justify-content-center">
          <button class="btn btn-dark bg-brown mx-2" @click="(productSelected.quantity > 0) ? productSelected.quantity-- : 0"><font-awesome-icon icon="fa-solid fa-minus" /></button>
          <span class="h3 w-25">{{ productSelected.quantity }}</span>
          <button class="btn btn-dark bg-brown mx-2" @click="productSelected.quantity++"><font-awesome-icon icon="fa-solid fa-plus" /></button>
      </div>
      <button class="btn btn-dark bg-brown btn-select" @click="confirmSelect">Select</button>
    </el-dialog>
</template>

<script lang="ts" setup>
import { ref, defineEmits, defineProps} from 'vue'
import ProductService from '@/services/ProductService'

defineOptions({
  name: 'GetProductDialog'
})

const props = defineProps(['name', 'visible', 'index'])
const emits = defineEmits()

const productSelected = ref( {
  name: props.name,
  size: '',
  quantity: 0,
  price: 0
})

const getPriceProduct = async (name, size) => {
  ProductService.getProduct(name, size)
  .then(response => {
    productSelected.value.size = size
    productSelected.value.price = response.data
  })
  .catch(error => {
    console.error('Error fetching data:', error)
  })
}

getPriceProduct(props.name, 'S')

const confirmSelect = () => {
  emits('onEdit', { index: props.index, productSelected })
}
</script>

<style>
button.nav-link.active {
    background-color: var(--brown);
    color: #fff;
}
button.nav-link {
    padding: 0.5rem 1rem;
    color: var(--brown);
}

button.nav-link:hover {
  background-color: var(--brown);
  color: var(--white);
  opacity: 0.5;
}
</style>