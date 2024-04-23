<template>
  <nav class="p-4 bg-brown">
    <router-link to="/order" class="me-2"><button class="btn">Order</button></router-link> 
    <router-link to="/"><button class="btn ">Member</button></router-link> 
  </nav>
  <div class="d-flex flex-wrap">
    <div class="col p-3 border-end">
      <form class="form-inline w-50 d-flex">
        <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
        <button class="btn btn-outline-brown my-2 my-sm-0" type="submit"><font-awesome-icon icon="fa-solid fa-magnifying-glass" /></button>
      </form> 
      <div id="search-products_result"></div>
      <nav class="nav mt-3">
        <button v-for="item in productTypes" :key="item" class="nav-link rounded-top border" @click="getProducts(item)"><span class="h5">{{ item }}</span></button>
      </nav>
      <div class="">
        <ListProduct 
          :list="products"
        />
      </div>
    </div>
    <div class="col p-3" id="bill-content">
      <div class="border-bottom mb-3">
        <h4>Danh sách sản phẩm đã chọn</h4>
        <el-table :data="listProductSelected" stripe style="width: 100%;" :header-cell-style="{ background: '#724E2C', color: '#fff' }">
          <el-table-column type="index" label="STT" width="70"/>
          <el-table-column prop="name" label="Name" />
          <el-table-column prop="size" label="Size" />
          <el-table-column prop="price" label="Price" />
          <el-table-column prop="quantity" label="Quantity">
            <template #default="{ row, $index }">
              <el-input type="number" v-model="row.quantity" @change="editQuantity($index, row.quantity)" class="w-100"/>
            </template>
          </el-table-column>
          <el-table-column label="Cost">
            <template #default="scope">
              {{ scope.row.price * scope.row.quantity }}
            </template>
          </el-table-column>
          <el-table-column label="Actions">
            <template #default="scope">
              <button class="btn btn-outline-brown my-2 my-sm-0" type="submit" @click="removeProduct(scope.$index)"><font-awesome-icon icon="fa-solid fa-trash-can" /></button> 
            </template>
          </el-table-column>
        </el-table>
        <div id="thanhtoan" class="border-top pt-3">
          <div class="row ps-4">
              <div class="col">Tổng tiền</div>
              <div class="col">{{ totalCost }}</div>
              <div class="col">VND</div>
          </div>
          <div class="row ps-4">
              <div class="col">Giảm giá</div>
              <div class="col">{{ discountPercentage }}</div>
              <div class="col">%</div>
          </div>
          <div class="row ps-4">
              <div class="col">Tổng tiền  phải trả</div>
              <div class="col">{{ totalAmountToBePaid }}</div>
              <div class="col">VND</div>
          </div>
        </div>
      </div>
      <div class="d-flex">
        <h3 class="w-50">NV: 01</h3>
        <form class="form-inline w-50 d-flex">
          <h3 class="w-50">Hoi vien:</h3>
          <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
          <button class="btn btn-outline-brown my-2 my-sm-0" type="submit"><font-awesome-icon icon="fa-solid fa-magnifying-glass" /></button>
        </form> 
      </div>
      <button class="btn btn-dark bg-brown">Print Bill</button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, provide } from 'vue'
import ProductService from '@/services/ProductService'
import ListProduct from '@/components/order/product/ListProduct'
import $ from 'jquery'


const productTypes = ref([])
const products = ref([])
// const totalCost = ref(0)
const listProductSelected = ref([])
const discountPercentage = ref(0);


provide('listProductSelected', listProductSelected)

const fetchItems = async () => {
  ProductService.getProducts()
  .then(response => {
      productTypes.value = Array.from(new Set(response.data.map(item => item.type)))
      getProducts(productTypes.value[0])
  })
  .catch(error => {
      console.error('Error fetching data:', error)
  })
}

fetchItems()

const getProducts = (item: String) => {
  ProductService.getProductByType(item)
  .then(response => {
      products.value = response.data
  })
  .catch(error => {
      console.error('Error fetching data:', error)
  })
}

const removeProduct = (index: number) => {
  listProductSelected.value.splice(index, 1);
}

const editQuantity = (index:number, newQuantity:number) => {
  listProductSelected.value[index].quantity = newQuantity;
};

const totalCost = computed(() => {
  let cost = 0;
  for (const item of listProductSelected.value) {
    cost += item.price * item.quantity;
  }
  return cost;
});

const totalAmountToBePaid = computed(() => {
  const discount = totalCost.value * (discountPercentage.value / 100);
  return totalCost.value - discount;
});

const order = ref({
  listProductSelected: listProductSelected.value,
  discountPercentage: discountPercentage.value, // Example discount percentage
});

</script>

<style scoped>
.nav-link.active {
    background-color: var(--brown);
    color: #fff;
}
.nav-link {
    padding: 0.5rem 1rem;
    color: var(--brown);
}

.nav-link:hover {
  background-color: var(--brown);
  color: var(--white);
  opacity: 0.5;
}
@media screen and (max-width: 768px) {
  .col {
    flex: 1 1 100%!important;
  }
}

@media screen and (min-width: 576px) {
}
</style>