package cn.acyou.leo.order.web.tests.es.bo;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author youfang
 * @version [1.0.0, 2025/3/1 10:10]
 **/
@Data
public class Order2025 {
    private String id;
    private Date createTime;
    private Long userId;
    private BigDecimal money;
    private String memo;
    private Integer point;
    private String phone;
    private String storeName;
}
