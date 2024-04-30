-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
                             `id` bigint(20) NULL DEFAULT NULL,
                             `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

INSERT INTO `sys_role` VALUES (1, '管理员');

-- ----------------------------
-- Table structure for sys_score
-- ----------------------------
DROP TABLE IF EXISTS `sys_score`;
CREATE TABLE `sys_score`  (
                              `score_id` bigint(20) NOT NULL,
                              `user_id` bigint(20) NULL DEFAULT NULL,
                              `score_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
                              `score` int(255) NULL DEFAULT NULL,
                              PRIMARY KEY (`score_id`) USING BTREE,
                              INDEX `fk_user`(`user_id`) USING BTREE,
                              CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

INSERT INTO `sys_score` VALUES (1, 100, '语文', 100);
INSERT INTO `sys_score` VALUES (2, 100, '数学', 90);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
                             `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                             `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
                             `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户真实姓名',
                             `signature` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户签名',
                             `sex` int(11) NOT NULL DEFAULT 1 COMMENT '性别 1男 2女',
                             `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户手机号',
                             `age` int(11) NULL DEFAULT 0 COMMENT '年龄',
                             `birthday` date NULL DEFAULT NULL COMMENT '生日',
                             `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '密码',
                             `status` int(11) NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1正常',
                             `perms` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户权限',
                             `create_user` bigint(20) NOT NULL COMMENT '创建人',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间,默认当前时间',
                             `update_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
                             `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                             PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (100, 1,    '小王2', '100000@qq.com', 1, '18205166207', NULL, NULL, '123456', 1, '*.*.*', 1, '2021-09-24 20:58:02', NULL, NULL);
INSERT INTO `sys_user` VALUES (101, NULL, '小王2', '100000@qq.com', 1, NULL, NULL, NULL,          '123456', 1, '*.*.*', 1, '2021-12-03 00:43:53', NULL, NULL);
INSERT INTO `sys_user` VALUES (102, NULL, '小王2', '100000@qq.com', 1, NULL, NULL, NULL,          '123456', 1, '*.*.*', 1, '2021-12-03 00:44:13', NULL, NULL);
