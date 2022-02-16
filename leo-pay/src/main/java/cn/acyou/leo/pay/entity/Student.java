package cn.acyou.leo.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * Demo For Student
 * </p>
 *
 * @author youfang
 * @since 2022-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Student对象", description="Demo For Student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer age;

    private Date birth;


}
