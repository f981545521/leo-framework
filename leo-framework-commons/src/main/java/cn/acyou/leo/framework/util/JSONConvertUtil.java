package cn.acyou.leo.framework.util;


import cn.acyou.leo.framework.base.ColorVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author youfang
 * @version [1.0.0, 2025/3/19 9:52]
 **/
public class JSONConvertUtil {
    @Getter
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            //
        }
        return "{}";
    }

    public static void main(String[] args) throws JsonProcessingException {
        ColorVo colorVo = new ColorVo(10, 102,230);
        colorVo.setAlphaRatio("0.86");
        System.out.println(JSONConvertUtil.getObjectMapper().writeValueAsString(colorVo));

    }
}
