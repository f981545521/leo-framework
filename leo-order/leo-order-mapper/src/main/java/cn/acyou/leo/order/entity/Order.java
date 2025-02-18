package cn.acyou.leo.order.entity;

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
@TableName("t_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 5350645545628778721L;

    @TableId(type = IdType.AUTO)
    private Long orderId;

    private Long productId;

    private String orderName;

    private Integer status;

}
