package cn.acyou.leo.framework.commons;

import cn.acyou.leo.framework.constant.CommonErrorEnum;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.util.BeanUtil;
import cn.acyou.leo.framework.util.StringUtils;
import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * https://www.cnblogs.com/54chensongxia/archive/2020/11/20/14007696.html
 * @author youfang
 * @version [1.0.0, 2021/1/26]
 **/
@Api(tags = "默认错误处理")
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomErrorController extends BasicErrorController {

    public CustomErrorController(ServerProperties serverProperties) {
        super(new DefaultErrorAttributes(), serverProperties.getError());
    }

    /**
     * 覆盖Springboot 默认的JSON响应
     */
    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus httpStatus = getStatus(request);
        Map<String, Object> originalMsgMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        Integer status = (Integer) originalMsgMap.get("status");
        String error = (String) originalMsgMap.get("error");
        String message = (String) originalMsgMap.get("message");
        if (StringUtils.isNotBlank(message)) {
            error = error + "|" +  message;
        }
        Result<Object> resultError = Result.error(status, error);
        if (HttpStatus.NOT_FOUND.equals(httpStatus)) {
            resultError = Result.error(CommonErrorEnum.E_NOT_FOUNT);
        }
        return new ResponseEntity<>(BeanUtil.convertToMap(resultError), httpStatus);
    }

    /**
     * 覆盖Springboot 默认的ModelAndView响应
     */
    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        response.setStatus(getStatus(request).value());
        Map<String, Object> model = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        return (modelAndView == null ? new ModelAndView("/sys/common/error", model) : modelAndView);
    }
}
