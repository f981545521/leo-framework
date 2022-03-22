package cn.acyou.leo.pay.service;

import cn.acyou.leo.pay.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * Demo For Student 服务类
 * </p>
 *
 * @author youfang
 * @since 2022-02-16
 */
public interface StudentService extends IService<Student> {

    void addStudent(String name, Integer age);

}
