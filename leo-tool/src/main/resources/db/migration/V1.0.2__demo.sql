-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
     `id` bigint(20) NULL DEFAULT NULL,
     `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ;

INSERT INTO `sys_role` VALUES (1, '管理员');


DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
     `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
     `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
     `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
     `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
     `email` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户邮箱',
     `signature` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户签名',
     `sex` int NOT NULL DEFAULT 1 COMMENT '性别 1男 2女',
     `area_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '用户手机号区号 86',
     `phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户手机号',
     `age` int NULL DEFAULT 0 COMMENT '年龄',
     `source` varchar(20) NULL DEFAULT 0 COMMENT '用户来源 H5 PC ',
     `birthday` date NULL DEFAULT NULL COMMENT '生日',
     `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '密码',
     `status` int NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1正常',
     `expiration_time` datetime DEFAULT NULL COMMENT '账号过期时间',
     `type` tinyint DEFAULT NULL COMMENT '账号类型',
     `perms` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户权限',
     `create_user` bigint(20) NOT NULL COMMENT '创建人',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间,默认当前时间',
     `update_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
     `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
     PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统用户表';

