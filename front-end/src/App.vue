<template>
  <router-view/>
</template>

<style>
:root {
  --brown: #724E2C;
  --white: #fff;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.text-brown {
  color: var(--brown);
}

a { 
  text-decoration: none!important;
}

.bg-brown {
  background-color: var(--brown)!important;
  border-color: var(--brown)!important;
}

.btn-outline-brown {
  --bs-btn-color: var(--brown) !important;
  --bs-btn-border-color: var(--brown) !important;
  --bs-btn-hover-color: #fff!important;
  --bs-btn-hover-bg: var(--brown)!important;
  --bs-btn-hover-border-color: var(--brown)!important;
  --bs-btn-focus-shadow-rgb: 25, 135, 84!important;
  --bs-btn-active-color: #fff!important;
  --bs-btn-active-bg: var(--brown) !important;
  --bs-btn-active-border-color: var(--brown) !important;
  --bs-btn-active-shadow: inset 0 3px 5px rgba(0, 0, 0, 0.125)!important;
  --bs-btn-disabled-color: var(--brown)!important;
  --bs-btn-disabled-bg: transparent!important;
  --bs-btn-disabled-border-color: var(--brown)!important;
  --bs-gradient: none;
}

nav a button.btn{
  font-weight: bold;
  color: var(--white);
}

nav a button.btn:hover,
nav a.router-link-exact-active button.btn {
  background-color: var(--white);
  color: var(--brown);
}

nav a button.btn:hover {
  opacity: 0.7;
}
</style>
