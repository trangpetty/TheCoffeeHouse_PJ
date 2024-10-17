const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  lintOnSave: false,
  devServer: {
    allowedHosts: 'all',
    headers: {
      'Access-Control-Allow-Origin': '*',
    },
    client: {
      webSocketURL: {
        protocol: 'wss',
        hostname: '122c-2401-d800-21f1-9bde-ddcb-3d6e-76a4-f8e2.ngrok-free.app',
        port: 443,
        pathname: '/ws',
      },
    },
    proxy: {
      '/api': {
        target: 'https://3caf-2401-d800-21f1-9bde-ddcb-3d6e-76a4-f8e2.ngrok-free.app',
        changeOrigin: true,
        secure: false,
      },
    }
  }
});
