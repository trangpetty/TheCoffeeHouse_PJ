<template>
  <header>
    <div class="container">
      <div class="d-flex align-items-center justify-content-center">
        <button class="menu-toggle" @click="toggleSidebar">☰</button>
        <div class="header_logo">
          <router-link to="/" class="navbar-brand fw-bolder me-5">PETTY COFFEE</router-link>
        </div>
        <div class="header_menu">
          <ul class="clearfix">
            <li v-for="(item, index) in menu" :key="index" :class="{ 'has-child': item.children && item.children.length > 0 }">
              <router-link :to="`/menu/${item.path || ''}`" class="d-flex align-items-center">
                {{ item.label }}
                <font-awesome-icon v-if="item.children && item.children.length > 0" icon="fa-solid fa-sort-down" class="ms-1" style="height: 8px; width: 8px"/>
              </router-link>
              <ul v-if="item.children && item.children.length > 0" class="menu_child">
                <li v-for="(child, childIndex) in item.children" :key="childIndex" class="lv2_title">
                  <router-link :to="`/menu/${child.path || ''}`" class="d-flex align-items-center">
                    {{ child.label }}
                  </router-link>
                  <!-- Check for grandchildren -->
                  <ul v-if="child.children && child.children.length > 0" class="menu_child_lv3">
                    <li v-for="(grandchild, grandchildIndex) in child.children" :key="grandchildIndex" class="child-lv3">
                      <router-link :to="`/menu/${grandchild.path || ''}`" class="d-flex align-items-center">
                        {{ grandchild.label }}
                      </router-link>
                    </li>
                  </ul>
                </li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <!-- Sidebar -->
    <div class="sidebar" :class="{ 'menu-open': sidebarOpen }">
      <button class="close-btn" @click="toggleSidebar">×</button>
      <ul class="sidebar-menu">
        <li v-for="(item, index) in menu" :key="index">
          <router-link :to="`/menu/${item.path || ''}`">{{ item.label }}</router-link>
          <ul v-if="item.children && item.children.length > 0">
            <li v-for="(child, childIndex) in item.children" :key="childIndex">
              <router-link :to="`/menu/${child.path || ''}`">{{ child.label }}</router-link>
              <ul v-if="child.children && child.children.length > 0">
                <li v-for="(grandchild, grandchildIndex) in child.children" :key="grandchildIndex">
                  <router-link :to="`/menu/${grandchild.path || ''}`">{{ grandchild.label }}</router-link>
                </li>
              </ul>
            </li>
          </ul>
        </li>
      </ul>
    </div>
    <!-- Overlay -->
    <div class="overlay" v-if="sidebarOpen" @click="toggleSidebar"></div>
  </header>
</template>
<script lang="ts" setup>
import { ref } from "vue";

const sidebarOpen = ref(false);

const toggleSidebar = () => {
  sidebarOpen.value = !sidebarOpen.value;
};

const menu = ref([
  { label: "Cà phê", path: "Cà phê" },
  { label: "Trà", path: "Trà trái cây" },
  {
    label: "Menu",
    children: [
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
      },
    ]
  },
  { label: "Blog", path: "" }
]);
</script>
<style scoped>
a {
  text-decoration: none;
  color: var(--black);
}

header {
  position: sticky;
  background: rgba(255, 255, 255, 0.8);
  top: 0;
  left: 0;
  z-index: 999;
  height: 60px;
  width: 100%;
  border-bottom: 1px solid #00000026;
}

.header_menu > ul {
  padding: 0;
  margin: 0;
  display: flex;
}

.header_menu > ul > li {
  display: inline-block;
  padding: 19px 10px 19px;
}

.header_menu > ul > li a {
  display: block;
  color: #000;
  font-size: 14px;
  line-height: 22px;
  font-weight: 600;
  font-family: 'SF Pro Text', sans-serif;
}

.header_menu > ul > li .menu_child > li:hover > a,
.header_menu ul li.active > a,
.header_menu ul li:hover > a {
  color: #EA8025;
}

.header_menu > ul > li .menu_child > li {
  padding: 12px 2.2%;
  float: none;
  display: block;
}

.header_menu > ul > li .menu_child > li.lv2_title {
  flex: 1;
}

.header_menu > ul > li .menu_child > li > a {
  color: #000;
  border-bottom: 2px solid;
  font-size: 16px;
}

.header_menu > ul > li:hover .menu_child {
  background: rgba(255, 255, 255, 0.95);
}

ul {
  list-style-type: none;
}

.header_menu .menu_child_lv3 > li > a {
  font-weight: 400;
  color: rgba(0, 0, 0, 0.6);
}

.clearfix:before,
.clearfix:after {
  content: " ";
  display: table;
}


/* Sidebar Styles */
.menu-toggle {
  display: none; /* Ẩn theo mặc định */
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 24px;
  background: none;
  border: none;
  cursor: pointer;
  z-index: 1001; /* Đảm bảo nút nằm trên các phần tử khác */
}
.sidebar {
  position: fixed;
  top: 0;
  left: -250px;
  width: 250px;
  height: 100%;
  background-color: #ffffff;
  z-index: 1000;
  overflow-y: auto;
  transition: left 0.3s ease-in-out;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.2);
}

.sidebar.menu-open {
  left: 0;
}

.sidebar-menu {
  padding: 20px;
  list-style: none;
}

.sidebar-menu li {
  margin-bottom: 15px;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  margin: 15px;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999;
}

/* Responsive Styles */
@media (min-width: 769px) {
  .header_menu {
    display: flex;
  }

  .header_menu ul {
    list-style: none;
    display: flex;
    gap: 20px;
  }

  .header_menu li {
    margin: 0;
  }

  .sidebar {
    display: none;
  }

  .menu-toggle {
    display: none;
  }
}

@media (max-width: 769px) {
  .header_menu {
    display: none;
  }

  .menu-toggle {
    display: block;
  }

  .sidebar {
    display: block;
  }
}

@media (min-width: 1200px) {
  .header_menu > ul > li {
    padding: 19px 16px 19px;
  }
}

@media (min-width: 992px) {
  .header_menu .menu_child_lv3 {
    padding: 0;
    margin-top: 16px;
  }

  .header_menu .menu_child_lv3 > li {
    margin-bottom: 4px;
  }

  .header_menu > ul > li .menu_child {
    padding: 24px 15.4% 32px;
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    width: 100%;
    pointer-events: none;
    position: absolute;
    top: 200%;
    left: 0;
    z-index: 99;
    background: #fff;
    color: #000;
    visibility: hidden;
    opacity: 0;
    -webkit-transition: all .5s ease;
    -moz-transition: all .5s ease;
    -o-transition: all .5s ease;
    transition: all .5s ease;
  }

  .header_menu > ul > li:hover .menu_child {
    top: 100%;
    visibility: visible;
    opacity: 1;
    pointer-events: auto;
  }
}
</style>
