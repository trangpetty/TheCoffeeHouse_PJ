<template>
  <div :class="{ 'expanded': expanded }">
    <h5>{{ title }}</h5>
    <div class="d-flex flex-wrap">
      <el-tag
          v-for="(item, index) in props.items"
          :key="index"
          class="cursor-pointer m-1"
          @click="selectItem(index)"
      >
        {{ item.name }}
      </el-tag>
    </div>
    <div v-if="selectedIndex !== null">
      <h6 class="h6 fw-bold mb-0 mt-2">Đánh giá về sản phẩm {{ props.items[selectedIndex].name }}</h6>
      <div class="star-rating">
        <span
            v-for="star in 5"
            :key="star"
            @click="!props.items[selectedIndex].rated && setRating(selectedIndex, star)"
            :class="{ 'filled': star <= props.items[selectedIndex].rating }"
        >
          ★
        </span>
      </div>
      <el-input
          type="textarea"
          v-model="props.items[selectedIndex].comment"
          placeholder="Comment..."
          :disabled="props.items[selectedIndex].rated"
      />
      <button
          class="btn btn--orange-5 mt-2 float-end px-4 fw-medium py-2 btn--radius-100"
          @click="submitRating(selectedIndex)"
          :disabled="props.items[selectedIndex].rated"
      >
        Gửi
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, ref } from 'vue';
import axios from 'axios';

const props = defineProps(['title', 'items', 'userId']); // Added userId to props

const selectedIndex = ref<number | null>(null);
const expanded = ref(false);

const selectItem = (index: number) => {
  selectedIndex.value = index;
  expanded.value = true;
};

const setRating = (index: number, star: number) => {
  if (!props.items[index].rated) { // Allow rating only if not already rated
    props.items[index].rating = star;
  }
};

const submitRating = async (index: number) => {
  const item = props.items[index];
  const response = await axios.post('http://localhost:8082/api/products/rate', {
    productId: item.id,
    userId: props.userId,
    rate: item.rating,
    comment: item.comment
  });

  console.log(response);

  // Mark the item as rated
  item.rated = true;

  // Close the comment form
  selectedIndex.value = null;
  expanded.value = false;
};
</script>

<style scoped>
.expanded {
  width: 100%;
}

.star-rating {
  cursor: pointer;
}

.star-rating span {
  font-size: 30px;
  color: #ccc;
  margin-right: 5px;
}

.star-rating span.filled {
  color: gold;
}
</style>
