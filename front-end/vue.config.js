const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  lintOnSave: false,
  devServer: {
    proxy: {
      '/api': {
        target: 'http://10.30.100.178:8082',
        changeOrigin: true,
        pathRewrite: { '^/api': '' }
      }
    }
  }
});
