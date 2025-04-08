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

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Sensitive.Type type = sensitive.type();
        switch (type) {
            case EMAIL:
                jsonGenerator.writeString(DesensitizedUtil.email(s));
                break;
            case PHONE:
                jsonGenerator.writeString(DesensitizedUtil.mobilePhone(s));
                break;
            default:
                jsonGenerator.writeString(s);
        }
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
