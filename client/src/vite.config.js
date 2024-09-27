const { createProxyMiddleware } = require('http-proxy-middleware');
import react from "@vitejs/plugin-react";

module.exports = app => {

  app.use(
      '/',
      createProxyMiddleware({
          target: 'http://localhost:8080',
          changeOrigin: true
      }));
  app.listen(3000);
}