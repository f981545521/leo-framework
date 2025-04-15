package cn.acyou.leo.framework.commons;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.math.BigDecimal;

/**
 *
 * 此方式会全局生效，所有BigDecimal字段自动保留两位小数‌
 *
 * <pre>
 * ### 指定字段
 * {@code
 *     @ApiModelProperty(value = "账户余额")
 *     //@JsonSerialize(using = SerializerGlobalFeature.BigDecimalJsonSerializer.class)//指定自定义序列化器或内置类：
 *     private BigDecimal balance;
 * }
 * </pre>
 *
 * @author youfang
 * @version [1.0.0, 2021/1/20]
 **/
//@JsonComponent
public class SerializerGlobalFeature {
    /**
     * BigDecimal Json 序列化器
     *
     *
     */
    public static class BigDecimalJsonSerializer extends JsonSerializer<BigDecimal> {
        @Override
        public void serialize(BigDecimal value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (value != null) {
                jsonGenerator.writeString(value.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
            }
        }
    }

}
