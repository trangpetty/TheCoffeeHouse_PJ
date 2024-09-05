<template>
  <el-card class="card-item rounded text-white" shadow="always" :style="{backgroundColor: cardColor}">
    <div>
      <h1 ref="countElement">{{ displayValue }}</h1>
      <h5>{{ title }}</h5>
    </div>
    <font-awesome-icon class="fs-1" :icon="icon" />
  </el-card>
</template>

<script>
import { CountUp } from 'countup.js';

export default {
  props: {
    value: {
      type: Number,
      required: true
    },
    title: {
      type: String,
      required: true
    },
    icon: {
      type: String,
      required: true
    },
    cardColor: {
      type: String,
      default: '#16A2B9'
    }
  },
  data() {
    return {
      displayValue: 0,
    };
  },
  watch: {
    value: {
      handler(newVal) {
        this.startCountUp(newVal);
      },
      immediate: true
    }
  },
  methods: {
    startCountUp(toValue) {
      const countUp = new CountUp(this.$refs.countElement, toValue, {
        startVal: this.displayValue,
        duration: 2,
        separator: ',',
        decimal: '.'
      });
      if (!countUp.error) {
        countUp.start();
        this.displayValue = toValue;
      } else {
        console.error(countUp.error);
      }
    }
  }
};
</script>

<style scoped>
.card-item {
  width: calc(50% - 40px)!important;
  float: left;
  margin: 0 10px;
}

::v-deep .el-card__body {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

@media (min-width: 992px) {
  .card-item {
    width: calc(25% - 20px) !important;
  }
}

@media (max-width: 991px) {
  .card-item {
    height: 140px;
    margin-bottom: 1rem;
  }
}
</style>
