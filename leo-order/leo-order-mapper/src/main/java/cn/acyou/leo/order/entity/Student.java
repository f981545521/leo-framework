package cn.acyou.leo.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author youfang
 * @date 2018-04-15 下午 07:36
 **/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("student")
public class Student implements Serializable {

    private static final long serialVersionUID = 5350645545628778721L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    @TableLogic
    private Integer age;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date birth;

}
