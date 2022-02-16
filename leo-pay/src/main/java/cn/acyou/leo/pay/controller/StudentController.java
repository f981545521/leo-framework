package cn.acyou.leo.pay.controller;


import cn.acyou.leo.framework.commons.PageQuery;
import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.framework.model.PageSo;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.pay.entity.Student;
import cn.acyou.leo.pay.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * Demo For Student 前端控制器
 * </p>
 *
 * @author youfang
 * @since 2022-02-16
 */
@RestController
@RequestMapping("/student")
@Api(tags = "Student 前端控制器")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping(value = "/all")
    @ApiOperation("获取所有数据")
    public Result<List<Student>> test1() {
        return Result.success(studentService.list());
    }

    @GetMapping(value = "/page")
    @ApiOperation("分页获取数据")
    public Result<PageData<Student>> page(PageSo pageSo) {
        PageData<Student> studentPageData = PageQuery.startPage(pageSo).selectMapper(studentService.list());
        return Result.success(studentPageData);
    }

}
