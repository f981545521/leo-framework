-- ## Flyway注意事项
-- 1. 命名格式必须规范
--      1、仅需要执行一次的脚本，以V开头，后面跟上0~9的数字组合，数字之间可以使用.或者_进行分割。然后再以两个下划线 __进行分割，其后跟上文件名称，最后以.sql结尾。
--      2、需要重复执行的SQL，则需要以R开头。后面再以两个下划线分割，其后跟文件名称，最后以.sql结尾。
--          【注意】这里的 R 重复执行脚本，并不是说是启动项目后不断执行！而是，每次启动项目，都会重新校验对应的 R__add_user_info.sql 内容是否变更，如果变更则重新执行。
-- 2. 项目启动时，会执行db/migration目录下迁移文件
-- 3. 文件一旦执行后不能删除（必须将`flyway_schema_history`表中执行记录也删除）


-- ----------------------------
-- 参数配置表
-- ----------------------------
DROP TABLE IF EXISTS `sys_param_config`;
CREATE TABLE `sys_param_config` (
                                    `id`          bigint                                                 NOT NULL AUTO_INCREMENT COMMENT '参数主键(PK)',
                                    `namespace`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '命名空间/组',
                                    `code`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          DEFAULT NULL COMMENT '参数编码(键名)',
                                    `value`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          DEFAULT NULL COMMENT '参数键值',
                                    `ext_value`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          DEFAULT NULL COMMENT '参数扩展值',
                                    `is_system`   int                                                             DEFAULT '0' COMMENT '系统内置（1是 0否）',
                                    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          DEFAULT NULL COMMENT '参数名称',
                                    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          DEFAULT '' COMMENT '参数详细描述',
                                    `status`      int                                                    NOT NULL DEFAULT '1' COMMENT '是否启用（1是 0否）',
                                    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          DEFAULT NULL COMMENT '备注',
                                    `sort`        int                                                             DEFAULT NULL COMMENT '排序值',
                                    `version`     int                                                             DEFAULT 1    COMMENT '版本号',
                                    `deleted`     int                                                    NOT NULL DEFAULT '0' COMMENT '是否删除  0-正常 1-删除',
                                    `create_user` bigint                                                          DEFAULT NULL COMMENT '创建人',
                                    `create_time` datetime                                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，默认当前创建时间',
                                    `update_user` bigint                                                          DEFAULT NULL COMMENT '最后修改人',
                                    `update_time` datetime                                                        DEFAULT NULL COMMENT '最后修改时间',
                                    PRIMARY KEY (`id`),
                                    KEY `idx_namespace` (`namespace`) USING BTREE COMMENT '命名空间'
) ENGINE = InnoDB AUTO_INCREMENT = 100 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT ='参数配置表';

INSERT INTO `sys_param_config` (`id`, `namespace`, `code`, `value`, `ext_value`, `is_system`, `name`, `description`, `status`, `remark`, `sort`, `deleted`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (100, 'icon', '抖音', 'https://vshow.guiji.ai/nfs/tici/icon/douyin.png', NULL, 0, '', '短视频平台图标', 1, NULL, NULL, 0, '2024-09-13 14:38:03', NULL, NULL, NULL);
INSERT INTO `sys_param_config` (`id`, `namespace`, `code`, `value`, `ext_value`, `is_system`, `name`, `description`, `status`, `remark`, `sort`, `deleted`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (101, 'icon', '小红书', 'https://vshow.guiji.ai/nfs/tici/icon/xiaohongshu.png', NULL, 0, '', '短视频平台图标', 1, NULL, NULL, 0, '2024-09-13 14:38:03', NULL, NULL, NULL);
INSERT INTO `sys_param_config` (`id`, `namespace`, `code`, `value`, `ext_value`, `is_system`, `name`, `description`, `status`, `remark`, `sort`, `deleted`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (102, 'icon', '快手', 'https://vshow.guiji.ai/nfs/tici/icon/kuaishou.png', NULL, 0, '', '短视频平台图标', 1, NULL, NULL, 0, '2024-09-13 14:38:03', NULL, NULL, NULL);


-- ----------------------------
-- 数据字典表
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`
(
    `id`          bigint(20)                                             NOT NULL AUTO_INCREMENT COMMENT '字典ID',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典名称',
    `code`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典码',
    `parent_id`   bigint(20)                                             NOT NULL DEFAULT 0 COMMENT '父节点ID 如果是0代表是顶级字典',
    `sort`        int(11)                                                NOT NULL DEFAULT 1 COMMENT '排序值',
    `remark`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '备注',
    `status`      int(11)                                                NOT NULL DEFAULT 1 COMMENT '状态:  0-停用 1-正常',
    `international`      text                                            DEFAULT NULL COMMENT '国际化',
    `create_time` timestamp                                              NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                              NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '数据字典表';
-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict`
VALUES (1132, '测试', '111', 1131, 1, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (2155, '计量单位', 'unit', 0, 1, NULL, 1, '{"name_en":"unit","name_jp":"けいりょうたんい"}', now(), now());
INSERT INTO `sys_dict`
VALUES (2156, '把', '把', 2155, 0, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (2157, '板', '板', 2155, 1, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (2159, '包', '包', 2155, 3, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (2175, '个', '个', 2155, 19, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (2176, '根', '根', 2155, 20, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (2180, '盒', '盒', 2155, 24, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (2181, '件', '件', 2155, 25, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (2206, '瓶', '瓶', 2155, 50, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (2229, 'kg', 'kg', 2155, 73, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (2230, 'ml', 'ml', 2155, 74, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (2232, '杯', '杯', 2155, 76, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (2667, '职称', 'pro_title', 0, 1, NULL, 1, '{"name_en":"Professional title","name_jp":"役職名"}', now(), now());
INSERT INTO `sys_dict`
VALUES (2668, '主任', '主任', 2667, 1, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (3179, '副主任', '副主任', 2667, 1, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (3180, '副主任护师', '副主任护师', 2667, 1, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (3181, '主治医师', '主治医师', 2667, 1, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (3182, '副主任医师', '副主任医师', 2667, 1, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (3183, '主管护师', '主管护师', 2667, 1, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (3186, '护师', '护师', 2667, 1, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (3187, '护士', '护士', 2667, 1, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (3188, '讲师', '讲师', 2667, 1, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (3189, '医师', '医师', 2667, 1, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (3190, '主任护师', '主任护师', 2667, 1, NULL, 1, NULL, now(), now());
INSERT INTO `sys_dict`
VALUES (3191, '主任医师', '主任医师', 2667, 1, NULL, 1, NULL, now(), now());

-- ----------------------------
-- 定时任务
-- ----------------------------
DROP TABLE IF EXISTS `sys_schedule_job`;
CREATE TABLE `sys_schedule_job` (
                                    `job_id`          bigint(20)                                              NOT NULL AUTO_INCREMENT COMMENT '任务id',
                                    `bean_name`       varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT 'spring bean名称',
                                    `params`          varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '参数',
                                    `cron_expression` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT 'cron表达式',
                                    `status`          int(11)                                                 NOT NULL DEFAULT 1 COMMENT '任务状态  0：暂停  1：正常',
                                    `remark`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '备注',
                                    `create_time`     datetime                                                NULL     DEFAULT NULL COMMENT '创建时间',
                                    PRIMARY KEY (`job_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '定时任务';
INSERT INTO `sys_schedule_job` VALUES (10, 'myDynamicTask', NULL, '0/5 * * * * *', 0, '测试', '2020-04-04 21:56:48');

-- ----------------------------
-- 定时任务日志
-- ----------------------------
DROP TABLE IF EXISTS `sys_schedule_job_log`;
CREATE TABLE `sys_schedule_job_log` (
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
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '定时任务日志';
