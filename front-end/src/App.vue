<template>
  <router-view/>
</template>

<style>
:root {
  --brown: #724E2C;
  --white: #fff;
  --orange-1: rgba(232, 130, 21, .7);
  --radial-gradient-orange-1: radial-gradient(100% 501.4% at 0% 100%, #ffb141 0%, #fb8d17 100%);
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.bg-header {
  background: var(--radial-gradient-orange-1)!important;
}

.header-delivery--bg {
    background-color: var(--orange-1);
}

.header-delivery {
  border-radius: 50px;
  cursor: pointer;
  height: 3.25rem;
  width: 16rem;
  padding: 0.5rem;
}

.header-delivery .icon-delivery {
    border-radius: 50%;
    flex-shrink: 0;
    height: 2.5rem;
    width: 2.5rem;
}

</style>
