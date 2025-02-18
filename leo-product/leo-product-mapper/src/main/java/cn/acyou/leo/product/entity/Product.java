package cn.acyou.leo.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.io.Serializable;

/**
 * @author youfang
 * @date 2018-04-15 下午 07:36
 **/
@Data
@EqualsAndHashCode
@TableName("t_product")
public class Product implements Serializable {

    private static final long serialVersionUID = 5350645545628778721L;

    @TableId(type = IdType.AUTO)
    private Long productId;

    private String productName;

    private Integer stockNumber;

    private Integer status;


}
