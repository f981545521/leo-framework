package cn.acyou.leo.framework.commons;

import cn.acyou.leo.framework.base.ColorVo;
import cn.acyou.leo.framework.constant.CommonErrorEnum;
import cn.acyou.leo.framework.context.AppContext;
import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.util.*;
import cn.acyou.leo.framework.util.component.TencentMapUtil;
import cn.acyou.leo.framework.util.redis.RedisUtils;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 系统公共使用 允许匿名访问
 *
 * @author youfang
 * @version [1.0.0, 2020/6/29]
 **/
@Slf4j
@Controller
@RequestMapping("/sys/common")
@Api(tags = "系统公共使用", hidden = true)
public class SysCommonController {

    @Autowired(required = false)
    private RedisUtils redisUtils;

    @Autowired(required = false)
    private TencentMapUtil tencentMapUtil;

    @GetMapping(value = "/status")
    @ApiOperation(value = "状态检查")
    @ResponseBody
    @PermitAll
    public String check() {
        return "ok";
    }

    @GetMapping(value = "/fontNames")
    @ApiOperation(value = "字体列表")
    @ResponseBody
    @PermitAll
    public Result<List<String>> fontNames() {
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        return Result.success(Arrays.asList(fontNames));
    }

    @PostMapping(value = "/registerFont")
    @ApiOperation(value = "注册字体")
    @ResponseBody
    @PermitAll
    public Result<Void> registerFont(@RequestBody JSONObject req) {
        String url = req.getString("url");
        File fontFile = null;
        try {
            if (StringUtils.isNotBlank(url)) {
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                fontFile = FileUtil.createTempFile("font", ".ttf");
                HttpUtil.downloadFile(url, fontFile);
                log.info("字体下载成功：{}", fontFile.getAbsolutePath());
                Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                boolean b = ge.registerFont(font);
                log.info("注册字体 {} 结果：{}", url, b);
                if (!b) {
                    return Result.error("加载失败");
                }
            }
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("加载失败");
        } finally {
            if (fontFile != null) {
                fontFile.deleteOnExit();
            }
        }
        return Result.success();
    }


    @GetMapping(value = "/unauthorized")
    @ApiOperation(value = "没有权限")
    @ResponseBody
    @PermitAll
    public Result<Void> unauthorized() {
        return Result.error(CommonErrorEnum.E_UNAUTHORIZED);
    }

    @ApiOperation("打印请求信息")
    @RequestMapping(value = "print", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @PermitAll
    public Result<JSONObject> printRequest(HttpServletRequest request, @RequestBody(required = false) Object requestBody, @RequestHeader Map<String, Object> requestHeader) {
        Map<String, String[]> requestParameter = request.getParameterMap();
        JSONObject response = new JSONObject();
        response.put("requestParameter", requestParameter);
        response.put("requestBody", requestBody);
        response.put("requestHeader", requestHeader);
        return Result.success(response);
    }

    @ApiOperation("获取服务器外网IP")
    @GetMapping("ip")
    @ResponseBody
    @PermitAll
    public Result<JSONObject> ip() {
        String s = HttpUtil.get("http://httpbin.org/ip");
        log.info("服务器外网IP：{}", s);
        JSONObject jsonObject = JSON.parseObject(s);
        jsonObject.put("local", IPUtil.getLocalIP());
        jsonObject.put("client", AppContext.getIp());
        return Result.success(jsonObject);
    }

    @ApiOperation("获取IP属地")
    @GetMapping("ipAddr")
    @ResponseBody
    @PermitAll
    public Result<JSONObject> ipAddr(String ip) {
        if (tencentMapUtil == null) {
            throw new ServiceException("please create key at [https://lbs.qq.com/service/webService/webServiceGuide/webServiceIp]");
        }
        if (StringUtils.isBlank(ip)) {
            String s = HttpUtil.get("http://httpbin.org/ip");
            ip = JSON.parseObject(s).getString("origin");
        }
        final JSONObject ipLocation = tencentMapUtil.getIpLocation(ip);
        return Result.success(ipLocation);
    }

    @ApiOperation("获取IP属地V2")
    @GetMapping("ipAddrV2")
    @ResponseBody
    @PermitAll
    public Result<JSONObject> ipAddrV2(String ip) {
        return WorkUtil.doRetryWork(3, () -> {
            JSONObject res = new JSONObject(true);
            String s;
            if (StringUtils.isNotBlank(ip)) {
                s = HttpUtil.get("https://qifu.baidu.com/ip/geo/v1/district?ip=" + ip);
            } else {
                s = HttpUtil.get("https://qifu.baidu.com/ip/local/geo/v1/district");
            }
            JSONObject ipLocation = JSON.parseObject(s);
            res.put("ip", ip);
            res.put("continent", ipLocation.getJSONObject("data").getString("continent"));
            res.put("country", ipLocation.getJSONObject("data").getString("country"));
            res.put("prov", ipLocation.getJSONObject("data").getString("prov"));
            res.put("city", ipLocation.getJSONObject("data").getString("city"));
            res.put("district", ipLocation.getJSONObject("data").getString("district"));
            res.put("owner", ipLocation.getJSONObject("data").getString("owner"));
            res.put("zipcode", ipLocation.getJSONObject("data").getString("zipcode"));
            res.put("lat", ipLocation.getJSONObject("data").getString("lat"));
            res.put("lng", ipLocation.getJSONObject("data").getString("lng"));
            return Result.success(res);
        });
    }

    @GetMapping(value = "/color")
    @ApiOperation(value = "获取随机颜色")
    @ResponseBody
    @PermitAll
    public Result<List<ColorVo>> color(@RequestParam(value = "count", defaultValue = "1") Integer count, Boolean hasAlpla) {
        List<ColorVo> res = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            res.add(RandomUtil.randomColor(hasAlpla));
        }
        return Result.success(res);
    }

    @GetMapping(value = "/hashAvatar")
    @ApiOperation(value = "Hash头像生成")
    @PermitAll
    public void hashAvatar(@RequestParam(value = "num", required = false) Integer num, HttpServletResponse response) throws Exception {
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        final ServletOutputStream os = response.getOutputStream();
        AvatarHelper.create(num != null ? num : RandomUtil.createRandomInt(), os);
    }

    @GetMapping(value = "/rsa")
    @ApiOperation(value = "获取公钥")
    @ResponseBody
    @PermitAll
    public Result<String> rsa() throws Exception {
        String token = AppContext.getToken();
        Map<Integer, String> key = RSAUtils.genKeyPair();
        redisUtils.set("USER_RSA:" + token, JSON.toJSONString(key), 1, TimeUnit.DAYS);
        return Result.success(key.get(0));
    }

    @GetMapping(value = "/idempotent")
    @ApiOperation(value = "获取幂等序列 prefix前缀（必传）。sequence：空时生成，非空时判断。operate：校验并删除")
    @ResponseBody
    @PermitAll
    public Result<String> idempotent(@RequestParam("prefix") String prefix, String sequence, String operate) {
        if (sequence == null || StringUtils.isBlank(sequence)) {
            String uuid = IdUtil.uuidStrWithoutLine();
            redisUtils.set(RedisKeyConstant.AUTO_IDEMPOTENT_SEQUENCE + prefix + uuid, "1", 1, TimeUnit.HOURS);
            return Result.success(uuid);
        } else {
            String key = RedisKeyConstant.AUTO_IDEMPOTENT_SEQUENCE + prefix + sequence;
            String v = null;
            try {
                v = redisUtils.get(key);
                if (!org.springframework.util.StringUtils.hasText(v)) {
                    throw new ServiceException(CommonErrorEnum.REPETITIVE_OPERATION);
                }
                return Result.success(sequence);
            } finally {
                if (StringUtils.isNotBlank(operate) && "delete".equalsIgnoreCase(operate)) {
                    //使用过就删除序列
                    redisUtils.delete(key);
                }
            }
        }
    }

    @PostMapping("curl_request")
    @ApiOperation("接口测试请求")
    @ResponseBody
    @PermitAll
    public Result<?> curlRequest(@RequestBody(required = false) JSONObject param) {
        log.info("接口测试请求 {}", param);
        String httpType = param.getString("httpType");
        String httpHost = param.getString("httpHost");
        JSONArray httpHeaders = param.getJSONArray("httpHeaders");
        String httpBody = param.getString("httpBody");

        HttpRequest request = HttpUtil.createRequest(Method.valueOf(httpType), httpHost);
        request.setFollowRedirects(false);
        request.setConnectionTimeout(60000);
        if (httpHeaders != null) {
            for (int i = 0; i < httpHeaders.size(); i++) {
                JSONObject headerItem = httpHeaders.getJSONObject(i);
                String key = headerItem.getString("key");
                String value = headerItem.getString("value");
                if (StringUtils.isNotBlank(key)) {
                    request.header(key, value);
                }
            }
        }
        if (StringUtils.isNotBlank(httpBody)) {
            request.body(httpBody);
        }
        HttpResponse response = request.execute();
        Map<String, Object> res = new LinkedHashMap<>();
        Map<String, Object> requestHeadersMap = new LinkedHashMap<>();
        Map<String, Object> responseHeadersMap = new LinkedHashMap<>();
        request.headers().forEach((k,v)->{
            if (StringUtils.isNotBlank(k)) {
                requestHeadersMap.put(k, v.get(0));
            }
        });
        response.headers().forEach((k,v)->{
            if (StringUtils.isNotBlank(k)) {
                responseHeadersMap.put(k, v.get(0));
            }
        });
        res.put("requestHeaders", requestHeadersMap);
        res.put("responseHeaders", responseHeadersMap);
        res.put("responseBody", "");
        if (response.header("Content-Type").contains("json")) {
            res.put("responseBody", response.body());
        }
        return Result.success(res);
    }

    @GetMapping(value = "/error")
    @ApiOperation(value = "MV错误页面")
    @ResponseBody
    @PermitAll
    public Result<?> error(HttpServletRequest request) {
        int status = (Integer) request.getAttribute("status");
        String error = (String) request.getAttribute("error");
        String message = (String) request.getAttribute("message");
        Result<Object> resultError = Result.error(status, error + "|" + message);
        if (HttpStatus.NOT_FOUND.value() == status) {
            resultError = Result.error(CommonErrorEnum.E_NOT_FOUNT);
        }
        return resultError;
    }


}
