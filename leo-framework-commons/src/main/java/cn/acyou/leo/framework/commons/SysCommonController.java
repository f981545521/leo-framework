package cn.acyou.leo.framework.commons;

import cn.acyou.leo.framework.base.ColorVo;
import cn.acyou.leo.framework.constant.CommonErrorEnum;
import cn.acyou.leo.framework.context.AppContext;
import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.util.*;
import cn.acyou.leo.framework.util.component.TencentMapUtil;
import cn.acyou.leo.framework.util.redis.RedisUtils;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
    public String check() {
        return "ok";
    }

    @GetMapping(value = "/unauthorized")
    @ApiOperation(value = "没有权限")
    @ResponseBody
    public Result<Void> unauthorized() {
        return Result.error(CommonErrorEnum.E_UNAUTHORIZED);
    }

    @ApiOperation("打印请求信息")
    @RequestMapping(value = "print", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
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

    @GetMapping(value = "/color")
    @ApiOperation(value = "获取随机颜色")
    @ResponseBody
    public Result<List<ColorVo>> color(@RequestParam(value = "count", defaultValue = "1") Integer count, Boolean hasAlpla) {
        List<ColorVo> res = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            res.add(RandomUtil.randomColor(hasAlpla));
        }
        return Result.success(res);
    }

    @GetMapping(value = "/hashAvatar")
    @ApiOperation(value = "Hash头像生成")
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
    public Result<String> rsa() throws Exception {
        String token = AppContext.getToken();
        Map<Integer, String> key = RSAUtils.genKeyPair();
        redisUtils.set("USER_RSA:" + token, JSON.toJSONString(key), 1, TimeUnit.DAYS);
        return Result.success(key.get(0));
    }

    @GetMapping(value = "/idempotent")
    @ApiOperation(value = "获取幂等序列 prefix前缀（必传）。sequence：空时生成，非空时判断。operate：校验并删除")
    @ResponseBody
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

    @GetMapping(value = "/error")
    @ApiOperation(value = "MV错误页面")
    @ResponseBody
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
