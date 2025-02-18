package cn.acyou.leo.product.dto.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author youfang
 * @version [1.0.0, 2021-8-6]
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAddBo implements Serializable {

    private static final long serialVersionUID = -5715714565051947967L;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品价格")
    private BigDecimal price;

}
