## NodeJS

[Node.js 中文网 ](http://nodejs.cn/)

1. 下载安装
2. 验证
```
node --version
npm --version
npx --version
```

3. 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题

`npm install --registry=https://registry.npm.taobao.org`

2. 启动服务`npm run dev`


## 问题与解决
因为node-sass报错

npm ERR! node-sass@4.14.1 postinstall: node scripts/build.js

1. `npm config set sass_binary_site=https://npm.taobao.org/mirrors/node-sass`

2. 重新执行 `npm install`