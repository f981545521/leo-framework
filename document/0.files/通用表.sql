CREATE TABLE `param_config`
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
    UNIQUE KEY `idex_namespace_code` (`namespace`,`code`) USING BTREE COMMENT '命名空间与编码唯一'
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='参数配置表';