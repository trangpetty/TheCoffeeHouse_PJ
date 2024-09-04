<template>
  <Carousel />
  <div class="py-5">
    <div class="d-flex container">
      <div class="menu_banner">
        <a href="#">
          <img :src="banner" alt="">
        </a>
      </div>
      <ProductCard v-for="(item, index) in newestProducts" :key="index" :product="item"/>
    </div>
    <div v-if="discountProduct" class="mb-3 container">
      <h3 class="title">
        Sản phẩm khuyến mãi
      </h3>
      <div class="card-slider-container">
        <div class="card-slider">
          <div class="cards-wrapper">
            <ProductCard v-for="(item, index) in discountProduct" :key="index" :product="item"/>
          </div>
        </div>
      </div>
    </div>
    <div class="container">
      <div class="news-tab">
        <a v-for="(item, index) in tabs" :key="index" class="news-tab-text nav-link" :class="{ active: activeName === item.name }" @click="setActiveTab(item.name)">{{item.label}}</a>
        <div class="card-product-note-item">
          <input type="text" v-model="queryHashtag" class="card-product-text" placeholder="Tìm hashtag" @change="searchHashtag">
        </div>
      </div>
      <div class="card-slider-container pt-4">
        <div class="card-slider">
          <div class="cards-wrapper">
            <ProductCard v-for="(item, index) in products" :key="index" :product="item"/>
          </div>
        </div>
      </div>
    </div>
    <section class="cloudtea index">
      <div class="cloudtea-block right">
        <div class="container">
          <div class="d-flex justify-content-between align-items-center flex-wrap">
            <div class="info-block">
              <div class="img-title">
                <img :src="tagline" alt="">
              </div>
              <div class="cloudtea-desc">
                Được trồng trọt và chăm chút kỹ lưỡng, nuôi dưỡng từ thổ nhưỡng phì nhiêu, nguồn nước mát lành, bao bọc bởi mây và sương cùng nền nhiệt độ mát mẻ quanh năm, những búp trà ở Tây Bắc mập mạp và xanh mướt, hội tụ đầy đủ dưỡng chất, sinh khí, và tinh hoa đất trời.Chính khí hậu đặc trưng cùng phương pháp canh tác của đồng bào dân tộc nơi đây đã tạo ra Trà Xanh vị mộc dễ uống, dễ yêu, không thể trộn lẫn với bất kỳ vùng miền nào khác.
              </div>
              <div class="cloudtea-btn3">
                Thử ngay
              </div>
            </div>
            <div class="img-block">
              <img src="" alt="">
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="blog_home py-3">
      <h2 class="blog_home_title">
        <img :src="cup" alt="">
        Chuyện Nhà
      </h2>
      <div class="container">
        <h3 class="blog_home_blogtitle">
          Blog
        </h3>
        <div class="d-flex flex-wrap">
          <div class="blog_item d-flex flex-column" v-for="item in news.slice(0, 3)" :key="item.id">
            <router-link :to="{ name: 'blog-detail', params: { id: item.id } }" class="article_item_image">
              <div class="article_img" :style="{ backgroundImage: `url(${item.image})` }"></div>
            </router-link>
            <div class="article_item_info">
              <div class="article_published_at">
                <span>{{ item.createTime }}</span>
              </div>
              <h3>
                <span>{{ item.title }}</span>
              </h3>

            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>
<script setup lang="ts">
import banner from "@/assets/images/banner-newest.jpg";
import Carousel from "@/components/Carousel.vue";
import {onMounted, ref, watch} from "vue";
import axiosClient from '@/utils/axiosConfig';
import ProductCard from '@/components/user/main/ProductCard.vue';
import tagline from '@/assets/images/tagline1.webp';
import cup from '@/assets/images/cup.png';

const tabs = ref([
  { name: 'most-like', label: 'Yêu thích' },
  { name: 'best-seller', label: 'Bán chạy' },
  { name: 'highest-rate', label: 'Đánh giá' }
]);

const activeName = ref(tabs.value[0].name);
const type = ref(activeName.value);
const products = ref([]);
const newestProducts = ref([]);
const discountProduct = ref([]);
const news = ref([]);
const queryHashtag = ref('');

const setActiveTab = (tabName) => {
  activeName.value = tabName;
  type.value = tabName;
  fetchData()
}

const fetchData = async () => {
  try {
    const response = await axiosClient.get(`/products/${type.value}`);
    products.value = response.data;
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};

const getNewestProducts = async () => {
  const response = await axiosClient.get(`/products/newest`);
  newestProducts.value = response.data;
}

const getDiscountProducts = async () => {
  const response = await axiosClient.get(`/products/has-discount`);
  discountProduct.value = response.data;
}

const searchHashtag = async () => {
  const response = await axiosClient.get(`/hashtags/search?name=${queryHashtag.value}`);
  products.value = response.data;
}

onMounted(async () => {
  getNewestProducts();
  fetchData();
  getDiscountProducts();

  const response = await axiosClient.get(`/news/newest`);
  news.value = response.data;
})

</script>

<style scoped>
.header-meta-list {
  display: flex;
  margin: 0;
  padding: 10px 0;
  justify-content: center;
}

.menu_banner {
  flex: 0 0 calc(50% - 30px);
  margin: 0 15px 40px 15px;
}

.menu_banner > a {
  display: block;
  padding-top: 65%;
  position: relative;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0px 4px 16px 0px #0000001F;
}

.menu_banner > a > img {
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  position: absolute;
}

.news-tab {
  align-items: center;
  background: #fff;
  border: 1px solid #ededed;
  border-radius: 100px;
  cursor: pointer;
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  margin: auto;
  overflow-x: auto;
  padding: 10px 30px;
}

.news-tab-text {
  color: rgba(38, 38, 38, .5);
  flex-shrink: 0;
  font-size: 12px;
  font-weight: 600;
  line-height: 30px;
  margin-right: 30px;
  padding: 0 10px;
  text-align: center;
  text-transform: uppercase;
}

.news-tab-text.active {
  align-items: center;
  background: radial-gradient(100% 488.28% at 0 100%, #e87800 0, #fa8c16 100%);
  border-radius: 100px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, .12);
  color: #fff;
  height: 32px;
}

.card-slider-container {
  position: relative;
}

.card-slider {
  overflow: hidden;
  width: 100%;
}

.cards-wrapper {
  display: flex;
  flex-wrap: wrap;
  transition: transform 0.3s ease-in-out;
}

.title {
  font-size: 24px;
  margin: 24px 0;
  padding-left: 12px;
  border-left: 4px solid var(--orange-5);
}

.cloudtea.index {
  padding: 48px 0 45px;
  background: url('@/assets/images/news1.webp') top center / contain no-repeat;
}

.cloudtea .cloudtea-block {
  margin-bottom: 8px;
}

.cloudtea .cloudtea-block.right .info-block {
  order: 1;
  flex: 0 0 49%;
  width: 49%;
}

.cloudtea.index .cloudtea-block .info-block .img-title {
  margin-bottom: 12px;
}

.cloudtea.index .top-desc, .cloudtea.index .cloudtea-desc {
  padding: 15px 0;
  font-size: 16px;
  line-height: 25px;
  color: rgba(0, 0, 0, 0.6);
  text-align: justify;
}

.cloudtea .cloudtea-desc {
  font-size: 14px;
  line-height: 20px;
  margin-bottom: 16px;
}

.cloudtea.index .cloudtea-btn3 {
  display: block;
  max-width: 587px;
  background: #778B37;
  text-align: center;
  font-size: 16px;
  line-height: 40px;
  padding: 0 15px;
  font-weight: 600;
  border-radius: 8px;
  color: #fff;
  cursor: pointer;
}

.cloudtea .cloudtea-block .img-block {
  flex: 0 0 49%;
  width: 49%;
}

.blog_home {
  background: #FFF7E6;
}

.blog_home .blog_home_title {
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: 'SF Pro Display', sans-serif;
  font-size: 28px;
  margin-top: 40px;
}

.blog_home .blog_home_title > img {
  margin-right: 8px;
  max-width: 28px;
}

.blog_home .blog_home_blogtitle {
  font-size: 24px;
  margin: 24px 0;
  padding-left: 12px;
  border-left: 4px solid #E57905;
}

.blog_item {
  margin-bottom: 30px;
  -ms-flex: 0 0 calc(33.333% - 30px);
  -webkit-flex: 0 0 calc(33.333% - 30px);
  flex: 0 0 calc(33.333% - 30px);
  overflow: hidden;
  margin: 0 15px 0;
  min-height: 342px;
}

.blog_item .article_item_image {
  position: relative;
  display: block;
  overflow: hidden;
  border-radius: 8px;
}

.blog_item .article_item_image:before {
  content: '';
  position: relative;
  display: block;
  padding-top: 48%;
}

.blog_item .article_item_image .article_img {
  transition: all .4s;
  border-radius: 10px;
  background-size: cover;
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  background-position: center center;
  background-repeat: no-repeat
}

.blog_item .article_item_image:hover > .article_img {
  transform: scale(1.1);
}

.blog_item .article_item_info {
  -webkit-flex-grow: 1;
  -ms-flex-grow: 1;
  flex-grow: 1;
  background: #FFF7E6 50%;
  padding: 16px 0;
}

.blog_item .article_published_at {
  color: #00000099;
}

.blog_item .article_item_info h3 {
  font-size: 18px;
  margin: 8px 0;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  display: -webkit-box;
  height: 25px;
}

.card-product-note-item {
  background: var(--white-1);
  border: 1px solid var(--smoky-gray-4);
  border-radius: var(--space-4);
  display: flex;
  height: var(--space-44);
}

.card-product-text {
  border: none;
  flex: 1;
  font-size: var(--space-16);
  height: 100%;
  outline: none;
  padding: 0 var(--space-16);
  width: 80%;
  width: var(--space-200);
}

.card-product-note-icon {
  height: 20px;
  margin: 12px 11px;
  width: 18px;
  color: var(--orange-1);
  cursor: pointer;
}


@media (min-width: 992px) {
  .news-tab {
    justify-content: center;
    width: 50%;
  }
}
</style>