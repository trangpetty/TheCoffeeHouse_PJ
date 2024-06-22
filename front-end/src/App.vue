<template>
  <router-view/>
</template>

<style>
:root {
  --brown: #724E2C;
  --white: #fff;
  --white-1: #f5f5f5;
  --blue: #0084ff;
  --black: #000;
  --black-2: #666;
  --black-3: #262626;
  --black-6: rgba(38,38,38,.5);
  --orange-1: rgba(232, 130, 21, .7);
  --orange-2: #fa8c16;
  --orange-3: rgba(255, 231, 186, .6);
  --orange-4: #ff792c;
  --orange-5: #e57905;
  --radial-gradient-orange-1: radial-gradient(100% 501.4% at 0% 100%, #ffb141 0%, #fb8d17 100%);
  --radial-gradient-orange-9: radial-gradient(75.9% 488.28% at 24.1% 100%, #e87800 0%, #fa8c16 100%);
  --radial-gradient-orange-10: radial-gradient(100% 828.83% at 0% 100%,#e87800 0%,#fa8c16 100%);
  --nude-1: #fff7e6;
  --nude-2: #ffe7ba;
  --smoky-gray-2: #b2b2b2;
  --smoky-gray-3: rgba(0, 0, 0, .6);
  --smoky-gray-4: #ededee;
  --smoky-gray-5: hsla(0, 0%, 77%, .3);
  --smoky-gray-6: #e4e4e4;
  --scale-unit: 1rem;
  --space-2: calc(var(--scale-unit)* 0.125);
  --space-4: calc(var(--scale-unit)* 0.25);
  --space-6: calc(var(--scale-unit)* 0.375);
  --space-8: calc(var(--scale-unit)* 0.5);
  --space-10: calc(var(--scale-unit)* 0.625);
  --space-12: calc(var(--scale-unit)* 0.75);
  --space-14: calc(var(--scale-unit)* 0.875);
  --space-15: calc(var(--scale-unit)* 0.9375);
  --space-16: calc(var(--scale-unit)* 0.8375);
  --space-18: calc(var(--scale-unit)* 1.125);
  --space-20: calc(var(--scale-unit)* 1.25);
  --space-22: calc(var(--scale-unit)* 1.375);
  --space-24: calc(var(--scale-unit)* 1.5);
  --space-26: calc(var(--scale-unit)* 1.625);
  --space-28: calc(var(--scale-unit)* 1.75);
  --space-30: calc(var(--scale-unit)* 1.875);
  --space-32: calc(var(--scale-unit)* 2);
  --space-36: calc(var(--scale-unit)* 2.25);
  --space-38: calc(var(--scale-unit)* 2.375);
  --space-40: calc(var(--scale-unit)* 2.5);
  --space-42: calc(var(--scale-unit)* 2.625);
  --space-44: calc(var(--scale-unit)* 2.75);
  --space-46: calc(var(--scale-unit)* 2.875);
  --space-48: calc(var(--scale-unit)* 3);
  --space-50: calc(var(--scale-unit)* 3.125);
  --space-52: calc(var(--scale-unit)* 3.25);
  --space-54: calc(var(--scale-unit)* 3.375);
  --space-56: calc(var(--scale-unit)* 3.5);
  --space-58: calc(var(--scale-unit)* 3.625);
  --space-60: calc(var(--scale-unit)* 3.75);
  --space-62: calc(var(--scale-unit)* 3.875);
  --space-64: calc(var(--scale-unit)* 4);
  --space-66: calc(var(--scale-unit)* 4.125);
  --space-68: calc(var(--scale-unit)* 4.25);
  --space-70: calc(var(--scale-unit)* 4.375);
  --space-72: calc(var(--scale-unit)* 4.5);
  --space-74: calc(var(--scale-unit)* 4.625);
  --space-76: calc(var(--scale-unit)* 4.75);
  --space-78: calc(var(--scale-unit)* 4.875);
  --space-80: calc(var(--scale-unit)* 5);
  --space-84: calc(var(--scale-unit)* 5.25);
  --space-86: calc(var(--scale-unit)* 5.375);
  --space-88: calc(var(--scale-unit)* 5.5);
  --space-90: calc(var(--scale-unit)* 5.625);
  --space-94: calc(var(--scale-unit)* 5.875);
  --space-98: calc(var(--scale-unit)* 6.125);
  --space-100: calc(var(--scale-unit)* 6.25);
  --space-376: calc(var(--scale-unit)*23.5);
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
  margin-right: 2rem;
}

.header-delivery .icon-delivery {
  border-radius: 50%;
  flex-shrink: 0;
  height: 2.5rem;
  width: 2.5rem;
}

.cursor-pointer {
  cursor: pointer;
}

.btn.btn--white,
.btn.btn--white:hover,
.btn.btn--white:active{
  background: var(--white);
  border-radius: var(--space-100);
}

.text-limit-1-line {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin: 0;
  font-size: 14px;
}

.box-shadow {
  box-shadow: 0px 12px 32px 4px rgba(0, 0, 0, .04), 0px 8px 20px rgba(0, 0, 0, .08);
}

.box-shadow-1 {
  box-shadow: 0 8px 16px rgba(0, 0, 0, .12) !important;
}

.box {
  display: block;
  background-color: #fff;
  padding: 1rem 1.5rem;
}

.text-orange {
  color: var(--orange-2)!important;
}

.btn.btn--orange-1,
.btn.btn--orange-1:hover,
.btn.btn--orange-1:active,
.el-radio__input.is-checked .el-radio__inner

{
  background: var(--orange-2)!important;
  border-color: var(--orange-2)!important;
}

.btn.btn--smoky-gray,
.btn.btn--smoky-gray:hover,
.btn.btn--smoky-gray:active {
  background: var(--smoky-gray-4)!important;
  color: rgba(36, 36, 36, .3)!important;
  border-bottom-right-radius: var(--space-4) !important;
  border-top-right-radius: var(--space-4) !important;
  border: none!important;
}

.el-radio__input.is-checked+.el-radio__label {
  color: var(--el-radio-text-color)!important;
}

.delivery__input {
  background: #fafafa;
  border: 1px solid rgba(0, 0, 0, .15);
  border-radius: 0!important;
  font-size: 1rem;
  line-height: 1.5;
  outline: 0;
  padding: 11px 23px!important;
  height: var(--space-44);
}

.delivery__input::placeholder {
  font-size: var(--space-14)!important;
}

.input-group-prepend {
  margin-right: -1px;
}

.pagination {
  margin-top: 1rem;
  justify-content: center;
}

.el-dialog {
  padding: 0 !important;
}
.dialog {
  .el-dialog__header {
    padding: 15px 20px !important;
    background: #545c64 !important;

    .el-dialog__title {
      font-size: 17px;
      font-family: SourceHanSansCN-Regular, Helvetica Neue, Helvetica, Arial, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;
      color: #fffdfd;
    }

    .el-dialog__headerbtn .el-dialog__close {
      color: #fff;
      font-weight: bold;
    }

  }
    .el-dialog__body {
      padding: 1rem!important;
    }

  .needwrap {
    .el-form-item__label {
      font-family: SourceHanSansCN-Regular, Helvetica Neue, Helvetica, Arial, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;
      color: #686868;
      line-height: 30px;
    }
  }

  .confirm {
    margin-top: 13px;
  }
}

</style>
