<template>
  <div class="blog-card d-flex flex-column">
    <div class="blog-card__image" :style="{ backgroundImage: `url('${image}')` }"></div>
    <div class="blog-card__content d-flex flex-column">
      <div class="blog-card__content__header d-flex flex-column">
        <h5 class="blog-card__title text-uppercase">{{ title }}</h5>
      </div>
      <div class="mt-auto">
        <router-link :to="{ name: 'blog-detail', params: { id } }" class="btn btn--orange-1 btn--radius-100 d-inline-block align-items-center float-end">
          <span class="text">ĐỌC TIẾP</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
const props = defineProps({
  image: {
    type: String,
    required: true
  },
  title: {
    type: String,
    required: true
  },
  id: {
    type: [String, Number],
    required: true
  }
});
</script>

<style scoped>
/* Styles from your original blog card */
.blog-card {
  background-color: var(--white);
  border-radius: var(--space-8);
  box-shadow: var(--product-box-shadow);
  height: 100%;
  width: 100%;
}

.blog-card__image {
  background-size: cover !important;
  border-top-left-radius: var(--space-8);
  border-top-right-radius: var(--space-8);
  flex-shrink: 0;
  height: 150px;
  padding-top: 53%;
  width: 100%;
}

.blog-card__content {
  flex: 1;
  padding: var(--space-16);
  padding-top: var(--space-12);
}

.blog-card__title {
  color: var(--black-3);
  display: -webkit-box;
  font-size: var(--space-16);
  height: calc(var(--space-24) * 2);
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  font-weight: 600;
  line-height: var(--space-24);
  overflow: hidden;
}

.btn .text {
  line-height: var(--space-20);
  font-size: var(--space-12);
  color: var(--white);
}

.btn .text:hover {
  text-decoration: underline;
}
</style>
