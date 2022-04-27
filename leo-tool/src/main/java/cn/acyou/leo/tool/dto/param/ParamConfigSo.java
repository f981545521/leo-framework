package cn.acyou.leo.tool.dto.param;

import cn.acyou.leo.framework.model.PageSo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2022/4/19 13:46]
 **/
@Data
@ApiModel(description = "支持排序字段：createTime，sort")
public class ParamConfigSo extends PageSo {

    private String namespace;

    private String code;

    @Override
    public Map<String, String> supportField() {
        Map<String, String> supportFieldMap = new HashMap<>();
        supportFieldMap.put("createTime", "create_time");
        supportFieldMap.put("sort", "sort");
        return supportFieldMap;
    }

}
