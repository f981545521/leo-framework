CREATE TABLE `t_param_config`
(
    `id`          bigint                                                 NOT NULL AUTO_INCREMENT COMMENT '参数主键(PK)',
    `namespace`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '命名空间/组',
    `code`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '参数编码(键名)',
    `value`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '参数键值',
    `ext_value`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          DEFAULT NULL COMMENT '参数扩展值',
    `is_system`   int                                                             DEFAULT '0' COMMENT '系统内置（1是 0否）',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '参数名称',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          DEFAULT '' COMMENT '参数详细描述',
    `status`      int                                                    NOT NULL DEFAULT '1' COMMENT '是否启用（1是 0否）',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          DEFAULT NULL COMMENT '备注',
    `sort`        int                                                             DEFAULT NULL COMMENT '排序值',
    `is_delete`   int                                                    NOT NULL DEFAULT '0' COMMENT '是否删除  0-正常 1-删除',
    `create_time` datetime                                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，默认当前创建时间',
    `create_user` bigint                                                          DEFAULT NULL COMMENT '创建人',
    `update_time` datetime                                                        DEFAULT NULL COMMENT '最后修改时间',
    `update_user` bigint                                                          DEFAULT NULL COMMENT '最后修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_namespace_code` (`namespace`, `code`) USING BTREE COMMENT '命名空间与编码唯一'
) ENGINE = InnoDB
  AUTO_INCREMENT = 100
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='参数配置表';


-- ----------------------------
-- 数据字典表
-- ----------------------------
DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict`
(
    `id`          bigint(20)                                             NOT NULL AUTO_INCREMENT COMMENT '字典ID',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典名称',
    `code`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典码',
    `parent_id`   bigint(20)                                             NOT NULL DEFAULT 0 COMMENT '父节点ID 如果是0代表是顶级字典',
    `sort`        int(11)                                                NOT NULL DEFAULT 1 COMMENT '排序值',
    `remark`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '备注',
    `status`      int(11)                                                NOT NULL DEFAULT 1 COMMENT '状态:  0-停用 1-正常',
    `create_time` timestamp                                              NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                              NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uidx_t_dict_codeparentid` (`parent_id`, `code`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 100
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '数据字典表';
-- ----------------------------
-- Records of t_dict
-- ----------------------------
INSERT INTO `t_dict`
VALUES (1132, '测试', '111', 1131, 1, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2155, '计量单位', 'unit', 0, 1, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2156, '把', '把', 2155, 0, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2157, '板', '板', 2155, 1, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2158, '版', '版', 2155, 2, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2159, '包', '包', 2155, 3, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2160, '本', '本', 2155, 4, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2161, '并', '并', 2155, 5, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2162, '部', '部', 2155, 6, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2163, '层', '层', 2155, 7, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2164, '串', '串', 2155, 8, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2165, '床', '床', 2155, 9, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2166, '次', '次', 2155, 10, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2167, '打', '打', 2155, 11, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2168, '袋', '袋', 2155, 12, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2169, '点', '点', 2155, 13, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2170, '顶', '顶', 2155, 14, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2171, '对', '对', 2155, 15, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2172, '吨', '吨', 2155, 16, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2173, '付', '付', 2155, 17, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2174, '副', '副', 2155, 18, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2175, '个', '个', 2155, 19, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2176, '根', '根', 2155, 20, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2177, '公斤', '公斤', 2155, 21, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2178, '股', '股', 2155, 22, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2179, '罐', '罐', 2155, 23, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2180, '盒', '盒', 2155, 24, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2181, '件', '件', 2155, 25, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2182, '节', '节', 2155, 26, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2183, '斤', '斤', 2155, 27, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2184, '具', '具', 2155, 28, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2185, '卷', '卷', 2155, 29, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2186, '卡', '卡', 2155, 30, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2187, '颗', '颗', 2155, 31, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2188, '克', '克', 2155, 32, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2189, '孔', '孔', 2155, 33, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2190, '块', '块', 2155, 34, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2191, '捆', '捆', 2155, 35, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2192, '厘米', '厘米', 2155, 36, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2193, '立方厘米', '立方厘米', 2155, 37, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2194, '粒', '粒', 2155, 38, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2195, '辆', '辆', 2155, 39, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2196, '令', '令', 2155, 40, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2197, '枚', '枚', 2155, 41, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2198, '米', '米', 2155, 42, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2199, '面', '面', 2155, 43, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2200, '排', '排', 2155, 44, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2201, '盘', '盘', 2155, 45, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2202, '批', '批', 2155, 46, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2203, '片', '片', 2155, 47, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2204, '平方', '平方', 2155, 48, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2205, '平方厘米', '平方厘米', 2155, 49, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2206, '瓶', '瓶', 2155, 50, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2207, '圈', '圈', 2155, 51, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2208, '扇', '扇', 2155, 52, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2209, '升', '升', 2155, 53, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2210, '束', '束', 2155, 54, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2211, '双', '双', 2155, 55, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2212, '台', '台', 2155, 56, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2213, '套', '套', 2155, 57, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2214, '条', '条', 2155, 58, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2215, '贴', '贴', 2155, 59, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2216, '听', '听', 2155, 60, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2217, '桶', '桶', 2155, 61, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2218, '筒', '筒', 2155, 62, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2219, '托', '托', 2155, 63, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2220, '箱', '箱', 2155, 64, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2221, '页', '页', 2155, 65, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2222, '扎', '扎', 2155, 66, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2223, '张', '张', 2155, 67, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2224, '支', '支', 2155, 68, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2225, '只', '只', 2155, 69, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2226, '组', '组', 2155, 70, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2227, 'T', 'T', 2155, 71, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2228, 'g', 'g', 2155, 72, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2229, 'kg', 'kg', 2155, 73, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2230, 'ml', 'ml', 2155, 74, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2231, '半口', '半口', 2155, 75, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2232, '杯', '杯', 2155, 76, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2233, '测试', '测试', 2155, 77, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2234, '刀', '刀', 2155, 78, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2235, '份', '份', 2155, 79, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2236, '管', '管', 2155, 80, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2237, '毫居', '毫居', 2155, 81, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2667, '职称', 'pro_title', 0, 1, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (2668, '主任', '主任', 2667, 1, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (3179, '副主任', '副主任', 2667, 1, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (3180, '副主任护师', '副主任护师', 2667, 1, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (3181, '主治医师', '主治医师', 2667, 1, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (3182, '副主任医师', '副主任医师', 2667, 1, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (3183, '主管护师', '主管护师', 2667, 1, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (3186, '护师', '护师', 2667, 1, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (3187, '护士', '护士', 2667, 1, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (3188, '讲师', '讲师', 2667, 1, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (3189, '医师', '医师', 2667, 1, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (3190, '主任护师', '主任护师', 2667, 1, NULL, 1, now(), now());
INSERT INTO `t_dict`
VALUES (3191, '主任医师', '主任医师', 2667, 1, NULL, 1, now(), now());


-- ----------------------------
-- Table structure for t_task_schedule_job
-- ----------------------------
DROP TABLE IF EXISTS `t_schedule_job`;
CREATE TABLE `t_schedule_job`
(
    `job_id`          bigint(20)                                              NOT NULL AUTO_INCREMENT COMMENT '任务id',
    `bean_name`       varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT 'spring bean名称',
    `params`          varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '参数',
    `cron_expression` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT 'cron表达式',
    `status`          int(11)                                                 NOT NULL DEFAULT 1 COMMENT '任务状态  0：暂停  1：正常',
    `remark`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '备注',
    `create_time`     datetime                                                NULL     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`job_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '定时任务';

-- ----------------------------
-- Records of t_task_schedule_job
-- ----------------------------
INSERT INTO `t_schedule_job`
VALUES (10, 'myDynamicTask', NULL, '0/5 * * * * *', 0, '测试', '2020-04-04 21:56:48');

-- ----------------------------
-- Table structure for t_task_schedule_job_log
-- ----------------------------
DROP TABLE IF EXISTS `t_schedule_job_log`;
CREATE TABLE `t_schedule_job_log`
(
    `log_id`      bigint(20)                                              NOT NULL AUTO_INCREMENT COMMENT '任务日志id',
    `job_id`      bigint(20)                                              NOT NULL COMMENT '任务id',
    `bean_name`   varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT 'spring bean名称',
    `params`      varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '参数',
    `status`      int(11)                                                 NOT NULL COMMENT '任务状态    0：失败    1：成功',
    `error`       varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '失败信息',
    `times`       int(11)                                                 NOT NULL COMMENT '耗时(单位：毫秒)',
    `create_time` datetime                                                NULL DEFAULT NULL COMMENT '创建时间',
    `remark`      varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    `logs`        longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL DEFAULT NULL COMMENT '运行日志',
    `local_ip`    varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '运行设备IP',
    PRIMARY KEY (`log_id`) USING BTREE,
    INDEX `job_id` (`job_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 100
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '定时任务日志';
