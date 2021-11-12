package cn.acyou.leo.framework.commons;

import cn.acyou.leo.framework.constant.CommonErrorEnum;
import cn.acyou.leo.framework.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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
