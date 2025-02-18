package cn.acyou.leo.product.web.controller;

import cn.acyou.leo.framework.commons.PageQuery;
import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.product.dto.so.StudentSo;
import cn.acyou.leo.product.entity.Student;
import cn.acyou.leo.product.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.groovy.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author fangyou
 * @version [1.0.0, 2021/7/29]
 */
@Slf4j
@RestController
@RequestMapping("/student")
@Api(value = "学生", description = "学生的增删改查", tags = "学生接口")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "studentsPage", method = {RequestMethod.GET})
    @ApiOperation("测试分页")
    public Result<PageData<Student>> studentsPage(Integer pageNum, Integer pageSize) {
        PageData<Student> convertType =  PageQuery.startPage(pageNum, pageSize).selectMapper(studentService.list());
        return Result.success(convertType);
    }
    @PostMapping(value = "pageSo")
    @ApiOperation("测试分页")
    public Result<PageData<Student>> pageSo(@Validated @RequestBody StudentSo studentSo) {

        PageData<Student> convertType =  PageQuery.startPage(studentSo)
                .selectMapper(studentService.listByMap(Maps.of("name", studentSo.getName(), "age", studentSo.getAge())));
        return Result.success(convertType);
    }
    @GetMapping(value = "get")
    @ApiOperation("测试缓存")
    public Result<Student> getStudentById(Long id){
        Student student = studentService.getStudentById(id);
        return Result.success(student);
    }
}
