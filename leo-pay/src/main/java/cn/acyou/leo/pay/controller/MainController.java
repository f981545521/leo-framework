package cn.acyou.leo.pay.controller;

import cn.acyou.leo.framework.commons.AsyncManager;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.util.UrlUtil;
import cn.acyou.leo.pay.entity.Student;
import cn.acyou.leo.pay.service.StudentService;
import cn.hutool.http.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URL;


/**
 * @author youfang
 * @version [1.0.0, 2020/9/2]
 **/
@Slf4j
@RestController
@RequestMapping("/main")
@Api(value = "MainController", tags = "从此开始")
public class MainController {

    @Autowired
    private StudentService studentService;

    @GetMapping(value = "/start")
    @ApiOperation("测试一下")
    public Result<String> test1(String key) {
        Student student = studentService.getById(1);
        AsyncManager.execute(() -> {
            try {
                student.setAge(11);
                studentService.updateById(student);
                String fileUrl = "https://guiyu-tici.oss-cn-shanghai.aliyuncs.com/tici/403-1-202204241115229.MOV";
                log.info("下载中");
                long contentLength = UrlUtil.getContentLength(new URL(fileUrl));
                File file = new File("E:\\media\\6\\G1.MOV");
                HttpUtil.downloadFile(fileUrl, file);
                log.info("下载结束");
                student.setAge(12);
                studentService.updateById(student);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("发生了异常...........");
                student.setAge(0);
                studentService.updateById(student);
            } finally {
                log.info("结束 ...........");
            }


        });
        return Result.success(key);
    }

}
