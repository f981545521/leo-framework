package cn.acyou.leo.pay.service.impl;

import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.util.DateUtil;
import cn.acyou.leo.pay.entity.Student;
import cn.acyou.leo.pay.mapper.StudentMapper;
import cn.acyou.leo.pay.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Demo For Student 服务实现类
 * </p>
 *
 * @author youfang
 * @since 2022-02-16
 */
@Slf4j
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Override
    public void addStudent(String name, Integer age) {
        Student student = new Student();
        student.setName(name);
        student.setAge(age);
        student.setBirth(DateUtil.randomDate());
        log.info("addStudent :" + student);
        save(student);
        if (age > 100) {
            throw new ServiceException("age too long");
        }
    }
}
