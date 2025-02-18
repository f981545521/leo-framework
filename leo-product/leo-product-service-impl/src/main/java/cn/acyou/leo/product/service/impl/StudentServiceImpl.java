package cn.acyou.leo.product.service.impl;

import cn.acyou.leo.framework.commons.AsyncManager;
import cn.acyou.leo.product.entity.Student;
import cn.acyou.leo.product.mapper.StudentMapper;
import cn.acyou.leo.product.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author youfang
 * @version [1.0.0, 2020-4-17 下午 08:56]
 **/
@Slf4j
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Override
    public String sayHello(String name) {
        return String.format("Hello, %s", name);
    }


    /**
     * 通过id获取学生
     *
     * @param id id
     * @return {@link Student}
     */
    @Override
    @Cacheable(value = "leo:product:student#-1", key = "#id")
    public Student getStudentById(Long id) {
        final Student student = getById(id);
        log.info("[DB] 获取到学生：{}", student);
        AsyncManager.schedule(() -> {
            log.info("计划任务执行成功~！");
        }, 20, TimeUnit.SECONDS);
        return student;
    }
}
