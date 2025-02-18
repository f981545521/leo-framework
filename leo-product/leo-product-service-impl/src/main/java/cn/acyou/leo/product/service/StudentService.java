package cn.acyou.leo.product.service;

import cn.acyou.leo.product.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author youfang
 * @version [1.0.0, 2020-4-17 下午 08:55]
 **/
public interface StudentService extends IService<Student> {

    String sayHello(String name);

    /**
     * 通过id获取学生
     *
     * @param id id
     * @return {@link Student}
     */
    Student getStudentById(Long id);
}
