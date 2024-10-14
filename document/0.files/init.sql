CREATE DATABASE IF NOT EXISTS `scorpio2` DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_bin;

use scorpio2;

CREATE TABLE IF NOT EXISTS t_order_0 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, address_id BIGINT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
CREATE TABLE IF NOT EXISTS t_order_1 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, address_id BIGINT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));

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

INSERT INTO `scorpio2`.`sys_param_config` (`id`, `namespace`, `code`, `value`, `ext_value`, `is_system`, `name`, `description`, `status`, `remark`, `sort`, `deleted`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (100, 'icon', '抖音', 'https://vshow.guiji.ai/nfs/tici/icon/douyin.png', NULL, 0, '', '短视频平台图标', 1, NULL, NULL, 0, '2024-09-13 14:38:03', NULL, NULL, NULL);
INSERT INTO `scorpio2`.`sys_param_config` (`id`, `namespace`, `code`, `value`, `ext_value`, `is_system`, `name`, `description`, `status`, `remark`, `sort`, `deleted`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (101, 'icon', '小红书', 'https://vshow.guiji.ai/nfs/tici/icon/xiaohongshu.png', NULL, 0, '', '短视频平台图标', 1, NULL, NULL, 0, '2024-09-13 14:38:03', NULL, NULL, NULL);
INSERT INTO `scorpio2`.`sys_param_config` (`id`, `namespace`, `code`, `value`, `ext_value`, `is_system`, `name`, `description`, `status`, `remark`, `sort`, `deleted`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (102, 'icon', '快手', 'https://vshow.guiji.ai/nfs/tici/icon/kuaishou.png', NULL, 0, '', '短视频平台图标', 1, NULL, NULL, 0, '2024-09-13 14:38:03', NULL, NULL, NULL);
