package cn.acyou.leo.pay.controller;

import cn.acyou.leo.framework.util.StringUtils;
import cn.acyou.leo.pay.config.PayPalBean;
import cn.acyou.leo.pay.config.PayPalConfig;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.kit.PayKit;
import com.ijpay.paypal.PayPalApi;
import com.ijpay.paypal.PayPalApiConfig;
import com.ijpay.paypal.PayPalApiConfigKit;
import com.ijpay.paypal.accesstoken.AccessToken;
import com.ijpay.paypal.accesstoken.AccessTokenKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/payPal")
@Api(tags = "PayPal支付示例")
public class PayPalController {

    @Autowired
    PayPalBean payPalBean;

    private final static String RETURN_URL = "/payPal/return";
    private final static String CANCEL_URL = "/payPal/cancel";


    @GetMapping("")
    @ResponseBody
    public String index() {
        log.info(payPalBean.toString());
        return ("欢迎使用 PayPal 支付");
    }

    @GetMapping("test")
    @ResponseBody
    public Map<String, PayPalConfig> test() {
        return payPalBean.getConfigMap();
    }

    public PayPalApiConfig getConfig() {
        PayPalApiConfig config = new PayPalApiConfig();
        PayPalConfig payPalConfig = payPalBean.getConfigMap().get("DEFAULT");
        config.setClientId(payPalConfig.getClientId());
        config.setSecret(payPalConfig.getSecret());
        config.setSandBox(payPalConfig.getSandBox());
        config.setDomain(payPalConfig.getDomain());
        PayPalApiConfigKit.setThreadLocalApiConfig(config);
        return config;
    }

    @GetMapping(value = "/getAccessToken")
    @ResponseBody
    public AccessToken getAccessToken() {
        try {
            getConfig();
            return AccessTokenKit.get();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @GetMapping(value = "/createOrder")
    @ResponseBody
    @ApiOperation("创建订单")
    public void createOrder(HttpServletResponse response, @RequestParam(value = "price", required = false) String price,
                            @RequestParam(value = "customId", required = false) String customId) {
        log.info("PayPal支付 调用createOrder");
        try {
            PayPalApiConfig config = getConfig();

            //参数请求参数文档 https://developer.paypal.com/docs/api/orders/v2/#orders_create
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("intent", "CAPTURE");

            ArrayList<Map<String, Object>> list = new ArrayList<>();

            Map<String, Object> amount = new HashMap<>();
            amount.put("currency_code", "USD");
            amount.put("value", StringUtils.defaultString(price, "0.01"));

            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("amount", amount);
            itemMap.put("description", "购买订单:" + customId);
            itemMap.put("custom_id", customId);

            list.add(itemMap);

            dataMap.put("purchase_units", list);

            Map<String, String> applicationContext = new HashMap<>();
            //取消按钮转跳地址,这里用异步通知地址的兼容的做法
            applicationContext.put("cancel_url", config.getDomain().concat(CANCEL_URL));
            //发起付款后的页面转跳地址
            applicationContext.put("return_url", config.getDomain().concat(RETURN_URL));

            dataMap.put("application_context", applicationContext);

            String data = JSONUtil.toJsonStr(dataMap);
            log.info("PayPal创建订单" + data);
            IJPayHttpResponse resData = PayPalApi.createOrder(config, data);
            log.info(resData.toString());
            if (resData.getStatus() == 201) {
                String resultStr = resData.getBody();

                JSONObject jsonObject = JSONUtil.parseObj(resultStr);
                JSONArray links = jsonObject.getJSONArray("links");
                for (int i = 0; i < links.size(); i++) {
                    JSONObject item = links.getJSONObject(i);
                    String rel = item.getStr("rel");
                    String href = item.getStr("href");
                    if ("approve".equalsIgnoreCase(rel)) {
                        response.sendRedirect(href);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @GetMapping(value = "/updateOrder")
    @ResponseBody
    @ApiOperation("修改订单 （修改已创建的订单信息）支持修改订单的金额")
    public String updateOrder(@RequestParam("id") String id) {
        log.info("PayPal支付 调用 updateOrder id:{}", id);
        try {
            PayPalApiConfig config = getConfig();
            // https://developer.paypal.com/docs/api/orders/v2/#orders_patch

            ArrayList<Map<String, Object>> updateData = new ArrayList<>();

            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("op", "replace");
            itemMap.put("path", "/purchase_units/@reference_id=='default'/amount");

            Map<String, Object> amount = new HashMap<>();
            amount.put("currency_code", "USD");
            amount.put("value", "199.00");

            itemMap.put("value", amount);

            updateData.add(itemMap);

            String data = JSONUtil.toJsonStr(updateData);
            log.info(data);
            IJPayHttpResponse resData = PayPalApi.updateOrder(config, id, data);
            log.info(resData.toString());
            if (resData.getStatus() == 204) {
                return "success";
            }
            return "接口请求错误码为："+resData.getStatus();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @GetMapping(value = "/queryOrder")
    @ResponseBody
    @ApiOperation("查询订单")
    public String queryOrder(@RequestParam("id") String id) {
        log.info("PayPal支付 调用 queryOrder id:{}", id);
        try {
            PayPalApiConfig config = getConfig();
            IJPayHttpResponse response = PayPalApi.queryOrder(config, id);
            log.info(response.toString());
            if (response.getStatus() == 200) {
                return response.getBody();
            } else {
                return "接口请求错误码为：" + response.getStatus();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @GetMapping(value = "/captureOrder")
    @ResponseBody
    @ApiOperation("捕获订单 （用户支付后APPROVED状态时，捕获设置为支付完成）")
    public String captureOrder(@RequestParam("id") String id) {
        log.info("PayPal支付 调用 captureOrder id:{}", id);
        try {
            PayPalApiConfig config = getConfig();
            IJPayHttpResponse response = PayPalApi.captureOrder(config, id, "");
            log.info(response.toString());
            if (response.getStatus() == 200 || response.getStatus() == 201) {
                return response.getBody();
            } else {
                return "接口请求错误码为：" + response.getStatus();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @GetMapping(value = "/captureQuery")
    @ResponseBody
    @ApiOperation("查询捕获订单")
    public String captureQuery(@RequestParam("captureId") String captureId) {
        log.info("PayPal支付 调用 captureQuery captureId:{}", captureId);
        try {
            PayPalApiConfig config = getConfig();
            IJPayHttpResponse response = PayPalApi.captureQuery(config, captureId);
            log.info(response.toString());
            if (response.getStatus() == 200) {
                return response.getBody();
            } else {
                return "接口请求错误码为：" + response.getStatus();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @GetMapping(value = "/refund")
    @ResponseBody
    @ApiOperation("退款")
    public String refund(@RequestParam("id") String id) {
        log.info("PayPal支付 调用 refund id:{}", id);
        try {
            PayPalApiConfig config = getConfig();
            System.out.println("id>" + id);

            Map<String, Object> map = new HashMap<>();
            map.put("invoice_id", PayKit.generateStr());
            map.put("note_to_payer", "test product");

            Map<String, String> amount = new HashMap<>();
            amount.put("value", "1.00");
            amount.put("currency_code", "USD");

            map.put("amount", amount);

            String data = JSONUtil.toJsonStr(map);
            log.info("refund data：" + data);
            IJPayHttpResponse response = PayPalApi.refund(config, id, data);
            log.info(response.toString());
            if (response.getStatus() == 201) {
                return response.getBody();
            } else {
                return "接口请求错误码为：" + response.getStatus();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @GetMapping(value = "/refundQuery")
    @ResponseBody
    @ApiOperation("退款查询")
    public String refundQuery(@RequestParam("id") String id) {
        log.info("PayPal支付 调用 refundQuery id:{}", id);
        try {
            PayPalApiConfig config = getConfig();
            IJPayHttpResponse response = PayPalApi.refundQuery(config, id);
            log.info(response.toString());
            if (response.getStatus() == 200) {
                return response.getBody();
            } else {
                return "接口请求错误码为：" + response.getStatus();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @GetMapping(value = "/return")
    @ResponseBody
    @ApiOperation("点击支付成功 后的跳转路径")
    public String returnUrl(HttpServletRequest request) {
        final PayPalApiConfig config = getConfig();
        Map<String, String[]> parameterMap = request.getParameterMap();
        log.info("PayPal支付 调用 return 参数列表:{}", JSON.toJSONString(parameterMap));
        try {
            String token = request.getParameter("token");
            String payerId = request.getParameter("PayerID");
            log.info("token:" + token);
            log.info("payerId:" + payerId);
            String resStr = "您已经完成授权，点击：%s 进行确认！";
            return String.format(resStr, config.getDomain() + "/payPal/captureOrder?id=" + token);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @GetMapping(value = "/cancel")
    @ResponseBody
    @ApiOperation("点击取消支付 后的跳转路径")
    public String cancelUrl(HttpServletRequest request, HttpServletResponse response) {
        log.info("PayPal支付 调用 cancel ");
        String token = request.getParameter("token");
        System.out.println(token);
        return token;
    }
}
