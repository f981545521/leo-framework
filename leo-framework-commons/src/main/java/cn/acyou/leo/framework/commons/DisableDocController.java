package cn.acyou.leo.framework.commons;

import cn.acyou.leo.framework.constant.CommonErrorEnum;
import cn.acyou.leo.framework.model.Result;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author youfang
 * @version [1.0.0, 2022/2/22 11:28]
 **/
@Controller
@ConditionalOnProperty(value = "leo.api.enable", havingValue = "false")
public class DisableDocController {

    @GetMapping("/v2/api-docs")
    @ResponseBody
    public Result<?> disableApiDoc(){
        return Result.error(CommonErrorEnum.E_NOT_FOUNT);
    }
    @GetMapping("/doc.html")
    @ResponseBody
    public Result<?> docHtml(){
        return Result.error(CommonErrorEnum.E_NOT_FOUNT);
    }

}
