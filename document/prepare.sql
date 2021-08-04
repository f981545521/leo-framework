-- ORDER模块
drop database leo_order;
CREATE DATABASE IF NOT EXISTS `leo_order` DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_bin;
use leo_order;

CREATE TABLE `t_order`
(
    `order_id`   bigint       NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `product_id` bigint       NOT NULL COMMENT '订单产品ID',
    `order_name` varchar(200) NOT NULL DEFAULT '' COMMENT '订单名称',
    `status`     int(3)       NOT NULL DEFAULT '0' COMMENT '订单状态',
    PRIMARY KEY (`order_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 100
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='订单表';

CREATE TABLE `student`
(
    `id`    bigint       NOT NULL AUTO_INCREMENT,
    `name`  varchar(200) NOT NULL DEFAULT '',
    `age`   int          NOT NULL DEFAULT '0',
    `birth` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_student_age` (`age`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='Demo For Student';

CREATE TABLE `undo_log`
(
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT,
    `branch_id`     bigint(20)   NOT NULL,
    `xid`           varchar(100) NOT NULL,
    `context`       varchar(128) NOT NULL,
    `rollback_info` longblob     NOT NULL,
    `log_status`    int(11)      NOT NULL,
    `log_created`   datetime     NOT NULL,
    `log_modified`  datetime     NOT NULL,
    `ext`           varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 Comment = 'seata日志表';


-- PRODUCT模块
drop database leo_product;
CREATE DATABASE IF NOT EXISTS `leo_product` DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_bin;
use leo_product;

CREATE TABLE `t_product`
(
    `product_id`   bigint       NOT NULL AUTO_INCREMENT COMMENT '产品ID',
    `product_name` varchar(200) NOT NULL DEFAULT '' COMMENT '产品名称',
    `status`       int(3)       NOT NULL DEFAULT '0' COMMENT '产品状态',
    `stock_number` int(3)       NOT NULL DEFAULT '0' COMMENT '库存数量',
    PRIMARY KEY (`product_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 100
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='产品表';
INSERT INTO `leo_product`.`t_product`(`product_id`, `product_name`, `status`, `stock_number`)
VALUES (1, '笔记本', 0, 1000);

CREATE TABLE `student`
(
    `id`    bigint       NOT NULL AUTO_INCREMENT,
    `name`  varchar(200) NOT NULL DEFAULT '',
    `age`   int          NOT NULL DEFAULT '0',
    `birth` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_student_age` (`age`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='Demo For Student';

CREATE TABLE `undo_log`
(
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT,
    `branch_id`     bigint(20)   NOT NULL,
    `xid`           varchar(100) NOT NULL,
    `context`       varchar(128) NOT NULL,
    `rollback_info` longblob     NOT NULL,
    `log_status`    int(11)      NOT NULL,
    `log_created`   datetime     NOT NULL,
    `log_modified`  datetime     NOT NULL,
    `ext`           varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 Comment = 'seata日志表';
