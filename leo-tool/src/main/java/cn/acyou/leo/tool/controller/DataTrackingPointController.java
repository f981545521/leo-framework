/*
package cn.acyou.leo.tool.controller;

import cn.acyou.leo.framework.mapper.ExecuteMapper;
import cn.acyou.leo.framework.model.Result;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/dataPoint")
public class DataTrackingPointController {
    @Autowired
    private DataBuryingPointService dataBuryingPointService;
    @Autowired
    private UserService userService;
    @Autowired
    private BaseConfigService baseConfigService;

    @Autowired
    private ManagerEventHandler managerEventHandler;
    @Autowired
    private ExecuteMapper executeSqlMapper;

    @PostMapping(value = "saveBatch")
    @CrossOrigin
    @ApiOperation(value = "数据埋点 批量保存", httpMethod = "POST")
    public Result<Object> saveBatch(@RequestBody List<DataBuryingPoint> data,
                                    @RequestHeader(value = "token", required = false) String token, HttpServletRequest request) {
        if (CollectionUtil.isNotEmpty(data)) {
            Integer projectType = 1;
            String ip = IpUtils.getIp(request);
            List<String> pointNoList = new ArrayList<>();
            String trackId = null;
            for (DataBuryingPoint datum : data) {
                datum.setIp(ip);
                pointNoList.add(datum.getPointNo());
                projectType = datum.getProjectType();
                JSONObject remarkObj = StrUtil.toJSONObject(datum.getRemark());
                if(remarkObj.containsKey("trackId")) {
                    String ti = remarkObj.getString("trackId");
                    if(StringUtils.isNotEmpty(ti) && StringUtils.isEmpty(trackId)) {
                        trackId = ti;
                    }
                }
            }
            if(StringUtils.isNotEmpty(trackId)) {
                DataBuryingPoint dataBuryingPoint = dataBuryingPointService.queryFirstVisit(ip);
                if(dataBuryingPoint == null) {  //说明这个ip是首次
                    BiliAdCallbackEvent event = BiliAdCallbackEvent.builder()
                            .convType(BiliConvTypeEnum.APP_FIRST_ACTIVE)
                            .trackId(trackId)
                            .convTime(System.currentTimeMillis())
                            .build();
                    managerEventHandler.handle(event);
                }
            }

            Map<String, ConfigVo> configMap = baseConfigService.getConfigMap("DataBuryingPoint" + projectType, StringUtils.join(pointNoList, ","));
            for (DataBuryingPoint datum : data) {
                ConfigVo configVo = configMap.get(datum.getPointNo());
                if (configVo != null) {
                    String extValue = configVo.getExtValue();
                    try {
                        BeanCopyUtil.merge(datum, JSON.parseObject(extValue, DataBuryingPoint.class));
                    } catch (Exception e) {
                        //ignore
                    }
                }
            }
            //用户信息
            if (StringUtils.isNotBlank(token)) {
                FfoUser ffoUser = userService.getUserByToken(token);
                if (ffoUser != null) {
                    for (DataBuryingPoint datum : data) {
                        datum.setUserId(ffoUser.getId());
                    }
                }
            }
            dataBuryingPointService.saveBatch(data);
        }
        return Result.success();
    }


    @GetMapping(value = "dataPointExport")
    @ApiOperation(value = "数据埋点-导出", httpMethod = "GET", notes = "")
    public void dataPointExport(HttpServletResponse response, String startDate, String endDate) {
        try {
            String startDateStr = startDate + " 00:00:00";
            String endDateStr = endDate + " 00:00:00";
            List<LinkedHashMap<String, Object>> linkedHashMaps = executeSqlMapper.executeQuerySql(
                    "select dbp.event_id as '事件ID', dbp.event_name as '事件名称', count(*) as '总数量' , count(distinct dbp.user_id) as '独立用户数量'\n" +
                            "from \n" +
                            "`ffo-toc`.data_burying_point dbp \n" +
                            "\n" +
                            "where dbp.project_type = 100 and dbp.create_time >= '" + startDateStr + "' and dbp.create_time < '" + endDateStr + "' -- and dbp.event_id = 'AITool_2creation_create'\n" +
                            "group by dbp.event_id , dbp.event_name");
            ExcelUtil.exportExcel(response, linkedHashMaps, "数据埋点");
        } catch (Exception e) {
            //ignore
        }
    }

    */
/**
     * DROP TABLE IF EXISTS `data_burying_point`;
     * CREATE TABLE `data_burying_point`  (
     *   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
     *   `project_type` int NULL DEFAULT 1 COMMENT '项目类型：1 CN',
     *   `point_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
     *   `page_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面ID',
     *   `page` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面',
     *   `event_name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件名称',
     *   `event_id` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件ID',
     *   `event_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件说明',
     *   `reason` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '埋点原因',
     *   `source` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源',
     *   `field_1` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扩展字段1',
     *   `field_2` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扩展字段2',
     *   `field_3` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扩展字段3',
     *   `business_scenario` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务场景',
     *   `user_id` int NULL DEFAULT NULL COMMENT '用户id',
     *   `corp_id` int NULL DEFAULT NULL COMMENT '企业id',
     *   `ext` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '扩展参数JSON',
     *   `del_flag` int NULL DEFAULT 0 COMMENT '删除标识 0.有效 1.无效',
     *   `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     *   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     *   `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备注',
     *   `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问IP',
     *   `count(1)` bigint NOT NULL,
     *   `json_extract(dbp.remark, '$.id')` json NULL,
     *   `json_extract(dbp.remark, '$.robotName')` json NULL,
     *   PRIMARY KEY (`id`) USING BTREE
     * ) ENGINE = InnoDB AUTO_INCREMENT = 26168 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据埋点' ROW_FORMAT = Dynamic;
     *
     *
     *
     *
     *//*

}
*/
