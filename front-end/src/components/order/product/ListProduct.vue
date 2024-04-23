<template>
    <div class="d-flex flex-wrap">
        <button class="card text-center" v-for="(item, index) in props.list" :key="index" @click="activeDialogIndex = index">
            <div class="card-body w-100">
                <h5 class="">{{ item }}</h5>
            </div>
        </button>
    </div>
    <div class="text-center mt-3">
        <button @click="prevPage" :disabled="currentPage === 1" class="btn btn-dark bg-brown"><font-awesome-icon icon="fa-solid fa-chevron-left" /></button>
        <span class="mx-3">Page {{ currentPage }} of {{ totalPages }}</span>
        <button @click="nextPage" :disabled="currentPage === totalPages" class="btn btn-dark bg-brown"><font-awesome-icon icon="fa-solid fa-chevron-right" /></button>
    </div>
    <template v-if="activeDialogIndex !== null">
        <GetProductDialog
        :visible="true"
        :name="props.list[activeDialogIndex]"
        @close="closeDialog"
        @onEdit="selectProduct"
        />
  </template>
</template>

<script lang="ts" setup>
import { ref, computed, defineProps, inject } from 'vue'
import ProductService from '@/services/ProductService'
import GetProductDialog from '@/components/order/dialog/GetProductDialog'

defineOptions({
  name: 'ListProduct'
})

const props = defineProps(['list'])

const activeDialogIndex = ref<number | null>(null)
const productTypes = ref([])
const products = ref([])
const listProductSelected = inject('listProductSelected')

const itemsPerPage = 12
const currentPage = ref(1)
const openProductDialog = ref([])

const selectProduct = (event) => {
  listProductSelected.value.push(event.productSelected.value)
  closeDialog(event.index)
}

// const openDialog = (index: number | String) => {
//     openProductDialog.value.forEach((isOpen, i) => {
//         if (i !== index && isOpen) {
//             closeDialog(i);
//         }
//     });

//     // Mở cửa sổ mới
//     openProductDialog.value[index] = true;
//     alert("open");
// }

const closeDialog = (index: number | String) => {
    activeDialogIndex.value = null;
}

const totalPages = computed(() => Math.ceil(products.value.length / itemsPerPage))
const currentPageData = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage
  const end = start + itemsPerPage
  return products.value.slice(start, end)
})

const prevPage = () => {
  if (currentPage.value > 1) currentPage.value--
}

const nextPage = () => {
  if (currentPage.value < totalPages.value) currentPage.value++
}
</script>

<style>
.card {
  margin-right: 0.5rem;
  margin-bottom: 1rem;
  flex: 0 0 calc((100% - 4*0.5rem) / 4);
}

</style>