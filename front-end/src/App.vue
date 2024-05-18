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

.box-shadow {
  box-shadow: 0px 12px 32px 4px rgba(0, 0, 0, .04), 0px 8px 20px rgba(0, 0, 0, .08);
}

.box {
  display: block;
  background-color: #fff;
  padding: 1rem 1.5rem;
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
