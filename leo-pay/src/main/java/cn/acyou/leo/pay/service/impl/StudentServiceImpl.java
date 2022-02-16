package cn.acyou.leo.pay.service.impl;

import cn.acyou.leo.pay.entity.Student;
import cn.acyou.leo.pay.mapper.StudentMapper;
import cn.acyou.leo.pay.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Demo For Student 服务实现类
 * </p>
 *
 * @author youfang
 * @since 2022-02-16
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

}
