package cn.acyou.leo.tool.handler;


import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * @author youfang
 * @version [1.0.0, 2025/2/21 13:54]
 **/
@Slf4j
@MappedTypes({Object.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
public class JsonTypeHandlerV2 extends JacksonTypeHandler {
    public JsonTypeHandlerV2(Class<?> type) {
        super(type);
    }
}
