const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  lintOnSave: false,
  devServer: {
    proxy: {
      '/api': {
        target: 'http://192.168.0.104:8082',
        changeOrigin: true,
        pathRewrite: { '^/api': '' }
      }
    }
  }
});
