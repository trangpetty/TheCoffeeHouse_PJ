const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  lintOnSave: false,
  devServer: {
    proxy: {
      '/api': {
        target: 'http://192.168.100.11:8082',
        changeOrigin: true,
        pathRewrite: { '^/api': '' }
      }
    }
  }
});
