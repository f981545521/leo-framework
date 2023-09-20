package cn.acyou.leo.tool.dto.req;

import cn.acyou.leo.framework.model.PageSo;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2023/9/20 15:17]
 **/
@Data
public class TaskSo extends PageSo {

    @Override
    public Map<String, String> supportField() {
        Map<String, String> supportFieldMap = new HashMap<>();
        supportFieldMap.put("createTime", "create_time");
        return supportFieldMap;
    }
}
