## 友盟推送

- [官方-开发手册-产品介绍](https://developer.umeng.com/docs/67966/detail/149292)
- [官方-集成示例](https://developer.umeng.com/docs/67966/detail/149296)
- [友盟消息推送api、python sdk问题、测试demo代码](https://www.cnblogs.com/kakawith/p/10110472.html)

#### 名词解释

- **Appkey**：应用唯一标识。友盟消息推送服务提供的appkey和友盟统计分析平台使用的同一套appkey。
- **Umeng Message Secret**：Umeng Message Secret是和AppKey相对应的，用来关联唯一的应用，用于SDK集成。
- **App Master Secret**：服务器秘钥，用于服务器端调用API请求时对发送内容做签名验证。
- **Device Token**：友盟消息推送服务对设备的唯一标识。Android的device_token是44位字符串，iOS的device_token是64位。
- **Alia**s：开发者自有账号，开发者可以在SDK中调用setAlias(alias, alias_type)
  接口将alias+alias_type与device_token做绑定，之后开发者就可以根据自有业务逻辑筛选出alias进行消息推送。
- **单播(unicast)**：向指定的设备发送消息。
- **列播(listcast)**：向指定的一批设备发送消息。
- **广播(broadcast)**：向安装该App的所有设备发送消息。
- **组播(groupcast)**：向满足特定条件的设备集合发送消息，例如: “特定版本”、”特定地域”等。
- **文件播(filecast)**：开发者将批量的device_token或者alias存放到文件，通过文件ID进行消息发送。
- **自定义播(customizedcast)**：开发者通过自有的alias进行推送，可以针对单个或者一批alias进行推送，也可以将alias存放到文件进行发送。
  > type为customizedcast时, 参数 alias、file_id 必须要有一个

#### 服务端集成文档

- [官方-集成示例 开发者文档](https://developer.umeng.com/docs/67966/detail/68343)

```
  # 测试模式？？
  "production_mode":"true/false",    // 可选，true正式模式，false测试模式。默认为true
  // 测试模式只对“广播”、“组播”类消息生效，其他类型的消息任务（如“文件播”）不会走测试模式
  // 测试模式只会将消息发给测试设备。测试设备需要到web上添加
  // Android:测试设备属于正式设备的一个子集
```

#### 发送限制

为了提供更加稳定高效的推送环境，生产环境下有以下发送限制（同测试环境）：

- 广播(broadcast)默认推送频次每分钟5次，每天可推送10次
- 组播(groupcast)默认推送频次每分钟5次，每天推送次数无限制
- 文件播(filecast)默认推送频次每分钟5次，每天推送次数无限制
- 自定义播(customizedcast, 且file_id不为空)默认推送频次每分钟5次，每天推送次数无限制
- 单播类消息暂无推送限制

#### 过滤条件

目前开放的筛选字段有:

- “app_version”(应用版本)
- “channel”(渠道)
- “device_model”(设备型号)
- “province”(省)
- “tag”(用户标签)    **客户端手动做的标记**
- “country”(国家和地区) //“country”和”province”的类型定义请参照 文档示例
- “language”(语言)
- “launch_from”(一段时间内活跃)
- “not_launch_from”(一段时间内不活跃)
- “install_in”(设备注册时间在最近)
- “install_before”(设备注册时间在之前)
- “push_switch”(通知开关状态)

#### 使用别名推送

``` java
 
    /**
     * 定制化推送信息给IOS用户
     *
     * @param vo
     */
    public static void sendIOSCustomizedcast(CustomizedcastVo vo) {
        try {
            IOSCustomizedcast customizedcast = new IOSCustomizedcast(
                    vo.getAppkey(), vo.getAppMasterSecret());
            customizedcast.setAlias(vo.getAlias(), vo.getAliasType());
            customizedcast.setAlert(vo.getTitle(), "", vo.getText());
            customizedcast.setBadge(0);
            customizedcast.setSound("default");
            //设置额外字段
            if (!CollectionUtils.isEmpty(vo.getCustomField())) {
                for (Map.Entry<String, String> entry : vo.getCustomField().entrySet()) {
                    customizedcast.setCustomizedField(entry.getKey(), entry.getValue());
                }
            }
            if (BooleanUtils.toBoolean(vo.isTest())) {
                customizedcast.setTestMode();
            } else {
                customizedcast.setProductionMode();
            }
            client.send(customizedcast);
            log.info("sendIOSCustomizedcast 推送信息给IOS用户完成 userId:{}", vo.getUserId());
        } catch (Exception e) {
            log.error("sendIOSCustomizedcast 推送信息给IOS用户异常，异常信息：{}",
                    ExceptionUtils.getMessage(e));
        }
    }


    /**
     * 定制化推送信息给安卓用户
     *
     * @param vo
     */
    public static void sendAndroidCustomizedcast(CustomizedcastVo vo) {
        try {
            AndroidCustomizedcast customizedcast =
                    new AndroidCustomizedcast(vo.getAppkey(), vo.getAppMasterSecret());
            customizedcast.setAlias(vo.getAlias(), vo.getAliasType());
            customizedcast.setTicker(vo.getTicker());
            customizedcast.setTitle(vo.getTitle());
            customizedcast.setText(vo.getText());
            customizedcast.goAppAfterOpen();
            customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
            //设置额外字段
            if (!CollectionUtils.isEmpty(vo.getCustomField())) {
                JSONObject custom = new JSONObject();
                for (Map.Entry<String, String> entry : vo.getCustomField().entrySet()) {
                    custom.put(entry.getKey(), entry.getValue());
                }
                customizedcast.setCustomField(custom);
            }
            if (BooleanUtils.toBoolean(vo.isTest())) {
                customizedcast.setTestMode();
            } else {
                customizedcast.setProductionMode();
            }
            client.send(customizedcast);
            log.info("sendAndroidCustomizedcast 推送信息给Android用户完成 userId:{}", vo.getUserId());
        } catch (Exception e) {
            log.error("sendAndroidCustomizedcast 推送信息给Android用户异常，异常信息：{}",
                    ExceptionUtils.getMessage(e));
        }
    }
```
