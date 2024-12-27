CREATE TABLE `test_2`(
    `id`    int(10) unsigned NOT NULL AUTO_INCREMENT,
    `name`  varchar(200) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
    `age`   int(3) NOT NULL DEFAULT '0',
    `birth` datetime                           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`) USING BTREE,
    KEY     `idx_student_age` (`age`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='测试表2';