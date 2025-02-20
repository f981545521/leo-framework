package cn.acyou.leo.member.web.controller;

import cn.acyou.leo.framework.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fangyou
 * @version [1.0.0, 2021/7/29]
 */
@Slf4j
@RestController
@RequestMapping("/student")
@Api(value = "会员", description = "会员的增删改查", tags = "会员接口")
public class MemberController {

    @GetMapping(value = "get")
    @ApiOperation("测试缓存")
    public Result<?> getStudentById(Long memberId){
        return Result.success();
    }
}
