package cn.acyou.leo.order.service.impl;

import cn.acyou.leo.framework.commons.PageQuery;
import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.order.client.StudentClient;
import cn.acyou.leo.order.dto.so.StudentSo;
import cn.acyou.leo.order.entity.Student;
import cn.acyou.leo.order.mapper.StudentMapper;
import cn.acyou.leo.order.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * @author youfang
 * @version [1.0.0, 2020-4-17 下午 08:56]
 **/
@Slf4j
@Service
@DubboService(interfaceClass = StudentClient.class)
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService, StudentClient {

    @Override
    public String sayHello(String name) {
        return String.format("Hello, %s", name);
    }

    @Override
    public PageData<?> page(StudentSo studentSo) {
        return PageQuery.startPage(studentSo).selectMapper(lambdaQuery().list());
    }
}
