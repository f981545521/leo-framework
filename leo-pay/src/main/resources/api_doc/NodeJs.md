## NodeJS

[Node.js 中文网 ](http://nodejs.cn/)

[Node.js 多版本下载](https://nodejs.cn/en/about/previous-releases)
1. 下载安装
- 安装教程【https://blog.csdn.net/jeansmy111/article/details/106943227】
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




在Node.js生态中，npm（Node Package Manager）是JavaScript包和模块的管理工具。默认情况下，npm客户端配置为使用npm官方仓库（即npmjs.com），但有时出于各种原因，用户可能需要切换到其他源，比如中国大陆用户常用的淘宝npm镜像（npm.taobao.org）以加速下载速度。

更改npm源
临时更改
你可以临时更改npm的源，直到当前终端会话结束。例如，要使用淘宝的npm镜像，可以运行：

npm config set registry https://registry.npm.taobao.org
这条命令会将npm的源设置为淘宝的镜像。要查看当前配置的源，可以使用：

npm config get registry
完成工作后，如果你想恢复到默认源（即官方源），可以运行：

npm config set registry https://registry.npmjs.org