package cn.acyou.leo.framework.commons;

import cn.acyou.leo.framework.annotation.Sensitive;
import cn.acyou.leo.framework.util.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * @author youfang
 * @version [1.0.0, 2025/4/8 19:42]
 **/
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private Sensitive sensitive;


    /**
     * 用户信息脱敏:      s.replaceAll("(\S)\S(\S*)", "$1*$2")
     * 身份证信息脱敏:    s.replaceAll("(\d{4})\d{10}(\w{4})", "$1****$2")
     * 手机号脱敏:       s.replaceAll("(\d{3})\d{4}(\d{4})", "$1****$2")
     *
     * @param s
     * @param jsonGenerator
     * @param serializerProvider
     * @throws IOException
     */
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Sensitive.Type type = sensitive.value();
        switch (type) {
            case EMAIL:
                s = DesensitizedUtil.email(s);
                break;
            case PHONE:
                s = s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                break;
        }
        if ('*' != sensitive.mark()) {
            s = s.replaceAll("\\*", sensitive.mark() + "");
        }
        jsonGenerator.writeString(s);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        Sensitive sensitive = beanProperty.getAnnotation(Sensitive.class);
        if (sensitive != null) {
            return new SensitiveSerializer(sensitive);
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }
}
