<template>
  <header class="bg-header d-flex align-items-center">
    <div class="container-fluid container-lg d-flex align-items-center">
      <a class="navbar-brand fw-bolder" href="#">THE COFFEE HOUSE</a> 
      <div class="header-delivery header-delivery--bg">
        <img src="/src/assets/images/Delivery2.png" alt="" class="icon-delivery">
      </div> 
    </div>
  </header>
  <main>
    <div id="mainBanner" class="carousel slide" data-bs-ride="carousel" data-bs-interval="7000">
      <ul class="carousel-indicators" style="list-style-type: none;">
        <li v-for="(slide, index) in images" :key="index" :data-bs-target="'#mainBanner'" :data-bs-slide-to="index" :class="{ 'active': index === 0 }"></li>
      </ul>
      <div class="carousel-inner">
        <div v-for="(item, index) in images" :key="index" :class="{ 'carousel-item': true, 'active': index === 0 }" style="height: 550px;">
          <div class="bounceIn">
            <img :src="item" class="d-block w-100" alt="...">
          </div>
        </div>
      </div>
      <a class="carousel-control-prev" href="#mainBanner" role="button" data-bs-slide="prev">
        <font-awesome-icon icon="fa-solid fa-chevron-left" class="fs-3 text-brown"/>
      </a>
      <a class="carousel-control-next" href="#mainBanner" role="button" data-bs-slide="next">
        <font-awesome-icon icon="fa-solid fa-chevron-right" class="fs-3 text-brown"/>
      </a>
    </div>
    
  </main>

</template>

<script lang="ts" setup>
import banner1 from '@/assets/images/banner1.webp'
import banner2 from '@/assets/images/banner2.webp'
import banner3 from '@/assets/images/banner3.webp'
import {ref} from 'vue'


const images = ref([banner1, banner2, banner3])

</script>

<style>
.dropdown:hover .dropdown-menu {
      display: block;
    }

    
.navbar-expand-lg {
  justify-content: center!important;;
}

header {
    position: sticky;
    background: rgba(255, 255, 255, 0.8);
    top: 0;
    left: 0;
    z-index: 999;
    height: 60px;
    width: 100%;
}

@media(min-width:768px)
{
   .carousel-caption {
        left: 50%;
      top: 55%;
      transform: translate(-50%, -50%);
  } 
}

@media (prefers-reduced-motion: reduce) {
  .carousel-item {
      transition: transform .6s ease-in-out!important;
  }
}
</style>
