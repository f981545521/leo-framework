package cn.acyou.leo.pay.controller;

import cn.acyou.leo.framework.annotation.AccessLimit;
import cn.acyou.leo.framework.annotation.AutoIdempotent;
import cn.acyou.leo.framework.commons.PageQuery;
import cn.acyou.leo.framework.ftp.FTPUtil;
import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.framework.model.PageSo;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.pay.dto.StudentReq;
import cn.acyou.leo.pay.entity.Student;
import cn.acyou.leo.pay.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * Demo For Student 前端控制器
 * </p>
 *
 * @author youfang
 * @since 2022-02-16
 */
@Slf4j
@RestController
@RequestMapping("/student")
@Api(tags = "Student 前端控制器")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private FTPUtil ftpUtil;

    @GetMapping(value = "/all")
    @ApiOperation("获取所有数据")
    public Result<List<Student>> test1() {
        return Result.success(studentService.list());
    }

    @GetMapping(value = "/get")
    @ApiOperation("根据ID获取数据")
    @AccessLimit(value = "#id")
    public Result<Student> get(@RequestParam Integer id) {
        return Result.success(studentService.getById(id));
    }

    @PostMapping(value = "/update")
    @ApiOperation("根据ID获取数据")
    @AccessLimit(value = "#studentReq")
    public Result<StudentReq> update(@Validated @RequestBody StudentReq studentReq) {
        log.info("参数：{}", studentReq);
        return Result.success(studentReq);
    }
    @PostMapping(value = "/upload")
    @ApiOperation("上传文件")
    public Result<Void> upload(String type, MultipartFile file) throws Exception {
        log.info("参数：{}", file.getOriginalFilename());
        ftpUtil.uploadFile("/upload", file.getOriginalFilename(), file.getInputStream());
        return Result.success();
    }

    @GetMapping(value = "/page")
    @ApiOperation("分页获取数据")
    @AutoIdempotent(prefix = "student.page")
    public Result<PageData<Student>> page(PageSo pageSo) {
        PageData<Student> studentPageData = PageQuery.startPage(pageSo).selectMapper(studentService.list());
        return Result.success(studentPageData);
    }
    @GetMapping(value = "/addStudent")
    @ApiOperation("添加一个学生")
    public Result<Void> addStudent(String name, Integer age) {
        studentService.addStudent(name, age);
        return Result.success();
    }

}
