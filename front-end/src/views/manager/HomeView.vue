<template>
  <div>
    <div class="container">
      <div class="d-flex justify-content-center py-2">
        <div class="px-5">
          <a href="#" class="text-brown">
            <font-awesome-icon icon="fa-solid fa-location-dot" class="me-1"/>
            <span>Cua hang khap ca nuoc</span>
          </a>
        </div>
        <div>
          <a href="#" class="text-brown">
            <font-awesome-icon icon="fa-solid fa-phone" class="me-1"/>
            <span>Dat hang: 1800.0000</span>
          </a>
        </div>
      </div>
    </div>
  </div>
  <header>
    <div class="container">
      <nav class="navbar navbar-expand-lg justify-content-center">
      <div class="container">
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-center" id="navbarNav">
          <ul class="navbar-nav fw-bold">
            <li class="nav-item px-3">
              <a class="navbar-brand fw-bolder" href="#">THE COFFEE HOUSE</a>  
            </li>
            <li class="nav-item px-3">
              <a class="nav-link" href="#">Coffee</a>
            </li>
            <li class="nav-item px-3">
              <a class="nav-link" href="#">Tea</a>
            </li>
            <li class="nav-item px-3 dropdown">
              <a class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Menu
              </a>
              <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                <a class="dropdown-item" href="#">Action</a>
                <a class="dropdown-item" href="#">Another action</a>
                <div class="dropdown-divider"></div>
                <a class="dropdown-item" href="#">Something else here</a>
              </div>
            </li>
            <li class="nav-item px-3 dropdown">
              <a class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Menu
              </a>
              <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                <a class="dropdown-item" href="#">Action</a>
                <a class="dropdown-item" href="#">Another action</a>
                <div class="dropdown-divider"></div>
                <a class="dropdown-item" href="#">Something else here</a>
              </div>
            </li>
            <li class="nav-item px-3">
              <a class="nav-link" href="#">Coffee</a>
            </li>
            <li class="nav-item px-3">
              <a class="nav-link" href="#">Coffee</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>

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
