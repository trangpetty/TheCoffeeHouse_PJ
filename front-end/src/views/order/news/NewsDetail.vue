<template>
  <div>
    <div class="py-5">
      <div class="container-lg container-fluid custom">
        <h2 class="mb-4">{{ news?.title }}</h2>
        <div class="article-body" v-html="news?.content"></div>
      </div>
      <div class="custom-detail-header-news">
        <router-link to="/blogs" class="d-flex text-decoration-none">
          <span class="custom-detail-txt-news">Xem tất cả tin tức</span>
          <font-awesome-icon icon="fa-solid fa-arrow-right" class="custom-detail-txt-news" />
        </router-link>
      </div>
    </div>
    <!-- Display other news details as needed -->
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, defineProps } from 'vue';
import axios from 'axios';

// Define props if needed
const props = defineProps(['id']);
// Reactive variable for storing news details
const news = ref(null); // Use 'any' for flexibility in typing

// Fetch news details using the provided ID
onMounted(async () => {
  if (props.id) {
    try {
      const response = await axios.get(`http://localhost:8082/api/news/${props.id}`);
      news.value = response.data;
    } catch (error) {
      console.error('Error fetching news detail:', error);
    }
  }
});
</script>

<style scoped>
.custom {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, .1);
  padding: 50px;
}

.article-body ::v-deep img {
  width: 100%!important;
  height: auto;
  display: block; /* Ensures images behave as block elements */
  margin: 0 auto; /* Optional: Centers images if needed */
}

.custom-detail-header-news {
  align-items: center;
  cursor: pointer;
  display: flex;
  flex-direction: row;
  justify-content: center;
  margin: 50px auto;
}

.custom-detail-txt-news {
  color: #f2994a !important;
  font-size: 18px;
  margin-left: 8px;
  text-decoration: none !important;
}

@media (min-width: 992px) {
  .custom {
    width: 900px;
  }
}

</style>
