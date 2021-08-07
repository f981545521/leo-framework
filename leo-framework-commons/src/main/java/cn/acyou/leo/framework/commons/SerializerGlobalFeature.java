package cn.acyou.leo.framework.commons;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author youfang
 * @version [1.0.0, 2021/1/20]
 **/
@JsonComponent
public class SerializerGlobalFeature {
    /**
     * BigDecimal Json 序列化器
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
