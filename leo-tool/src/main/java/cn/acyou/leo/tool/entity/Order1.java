package cn.acyou.leo.tool.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author youfang
 * @since 2025-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_order_1")
@ApiModel(value="Order1对象", description="")
public class Order1 implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;

    private Integer userId;

    private Long addressId;

    private String status;


}
