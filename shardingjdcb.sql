CREATE TABLE user_config
(
    `id`           bigint(20)    NOT NULL AUTO_INCREMENT,
    `user_id`      bigint(20)    NOT NULL COMMENT '用户id',
    `name`         varchar(256)  NOT NULL COMMENT '配置名',
    `content`      longtext      NOT NULL COMMENT '配置项',
    `config_desc`  varchar(1024) NOT NULL COMMENT '配置描述',
    `gmt_create`   datetime               DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户配置表';

CREATE TABLE `user`
(
    `id`           bigint(20) NOT NULL,
    `name`         varchar(64)         DEFAULT NULL COMMENT '名称',
    `city_id`      int                 DEFAULT NULL COMMENT '城市',
    `sex`          tinyint(1)          DEFAULT NULL COMMENT '性别',
    `phone`        varchar(32)         DEFAULT NULL COMMENT '电话',
    `email`        varchar(32)         DEFAULT NULL COMMENT '邮箱',
    `password`     varchar(32)         DEFAULT NULL COMMENT '密码',
    `gmt_create`   datetime            DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_name_idx` (`name`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';

CREATE TABLE `t_order`
(
    `order_id`     bigint(20)  NOT NULL,
    `order_type`   int         NOT NULL COMMENT '订单类型',
    `user_id`      bigint(20)  NOT NULL COMMENT '用户id',
    `user_name`    varchar(64) NOT NULL COMMENT '用户名',
    `gmt_create`   datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`order_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='订单表';

CREATE TABLE `order_item`
(
    `order_id`     bigint(20)     NOT NULL COMMENT '订单编码',
    `type`         int            NOT NULL COMMENT '订单类型',
    `user_id`      bigint(20)     NOT NULL COMMENT '用户id',
    `user_name`    varchar(64)    NOT NULL COMMENT '用户名',
    `goods_id`     bigint(20)     NOT NULL COMMENT '商品id',
    `goods_name`   varchar(1024)  NOT NULL COMMENT '商品名称',
    `goods_count`  int(11)        NOT NULL DEFAULT '0' COMMENT '商品个数',
    `goods_price`  decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '商品价格',
    `gmt_create`   datetime                DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='订单表';

CREATE TABLE `warehouse`
(
    `id`                bigint(20)  NOT NULL,
    `warehouse_name`    varchar(64) NOT NULL COMMENT '仓库名',
    `warehouse_code`    varchar(64) NOT NULL COMMENT '仓库编码',
    `warehouse_address` varchar(512)         default '' COMMENT '仓库地址',
    `gmt_create`        datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`      datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `warehouse_code_idx` (`warehouse_code`) USING BTREE,
    KEY `warehouse_warehouse_name_IDX` (`warehouse_name`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='仓库信息表';

CREATE TABLE `hint_table`
(
    `id`           bigint(20)  NOT NULL AUTO_INCREMENT,
    `hit_name`     varchar(64) NOT NULL COMMENT 'hint名称',
    `gmt_create`   datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='hint测试表';


