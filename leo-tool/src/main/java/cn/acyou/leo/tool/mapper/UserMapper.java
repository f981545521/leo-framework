package cn.acyou.leo.tool.mapper;


import cn.acyou.leo.tool.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author youfang
 * @version [1.0.0, 2025/2/21 10:25]
 **/
@Repository
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from sys_user where user_id = #{userId}")
    User getById(@Param("userId") Long userId);

    User getByIdV2(@Param("userId") Long userId);
}
