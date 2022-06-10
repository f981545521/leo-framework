package cn.acyou.leo.framework.commons;

import cn.acyou.leo.framework.constant.CommonErrorEnum;
import cn.acyou.leo.framework.context.AppContext;
import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.util.IPUtil;
import cn.acyou.leo.framework.util.IdUtil;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
