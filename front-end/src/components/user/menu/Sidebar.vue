<template>
  <aside>
    <el-menu
        active-text-color="#000"
        background-color="#fff"
        class="el-menu-vertical-demo h-100 border-0"
        :default-active="$route.path"
    >
      <template v-for="(item, index) in menu" :key="index">
        <el-sub-menu v-if="item.children && item.children.length" :index="item.path">
          <template #title>
            <span>{{ item.label }}</span>
          </template>
          <template v-for="(child, childIndex) in item.children" :key="childIndex">
            <el-menu-item v-if="!child.children" :index="`${child.path}`" @click="changeTab(child.path, child.label)">
              <span>{{ child.label }}</span>
            </el-menu-item>
            <el-sub-menu v-else :index="child.path">
              <template #title>
                <span>{{ child.label }}</span>
              </template>
              <el-menu-item
                  v-for="(subChild, subChildIndex) in child.children"
                  :key="subChildIndex"
                  :index="`${subChild.path}`"
                  @click="changeTab(subChild.path, subChild.label)"
              >
                <span>{{ subChild.label }}</span>
              </el-menu-item>
            </el-sub-menu>
          </template>
        </el-sub-menu>
        <el-menu-item v-else @click="changeTab(item.path, item.label)" :index="item.path">
          <span>{{ item.label }}</span>
        </el-menu-item>
      </template>
    </el-menu>
  </aside>
</template>

<script setup lang="ts">
import { ref, defineEmits } from 'vue';
import { useRouter } from 'vue-router';

const emits = defineEmits(['tab-change']);

const menu = ref([
  {
    label: "Cà phê",
    children: [
      { label: "Cà phê đá", path: "Cà phê" },
      { label: "Cà phê nóng", path: "Món nóng" },
    ]
  },
  {
    label: "Trà trái cây - Hi Tea",
    children: [
      { label: "Trà trái cây", path: "Trà Trái Cây" },
      { label: "Hi Tea", path: "" },
    ]
  },
  {
    label: "Trà sữa",
    children: [
      { label: "Trà sữa", path: "Trà sữa" },
    ]
  },
  { label: "Thức uống đá xay", path: "Đá xay" },
  { label: "Trà Xanh", path: "Trà Xanh" },
  { label: "Trái Cây Đá Xay 0°C", path: "Trái Cây Đá Xay 0°C" },
  {
    label: "Bánh",
    children: [
      {label: "Bánh mặn", path: "Bánh mặn"},
      {label: "Bánh ngọt", path: "Bánh ngọt"},
    ]
  }
]);

const router = useRouter();

const changeTab = (path, label) => {
  // router.push(path);
  emits('tab-change', {label: label, type: path});
};
</script>

<style scoped>
::v-deep .el-sub-menu .el-icon {
  display: none!important;
}

::v-deep .el-menu-item.is-active {
  color: var(--black)!important;
  font-weight: 600;
}

::v-deep .el-sub-menu__title,
::v-deep .el-menu-item{
  color: var(--orange-2)!important;
}

.el-menu-item.is-active:before {
  content: '';
  width: 16px;
  height: 16px;
  position: absolute;
  top: calc(50% - 12px);
  left: 0;
  background-image: url("~@/assets/images/leaf-solid.svg");
}

.el-menu-item:hover,
::v-deep .el-sub-menu__title:hover {
  background-color: var(--orange-3)!important;
}
</style>