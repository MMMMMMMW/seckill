-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE seckill;
-- 使用数据库
use seckill;
CREATE TABLE goods
(
    `id`  BIGINT       NOT NUll AUTO_INCREMENT COMMENT '商品库存ID',
    `name`        VARCHAR(120) NOT NULL COMMENT '商品名称',
    `price`       int            NOT NULL COMMENT '价格',
    `number`      int          NOT NULL COMMENT '库存数量',
    `null_time`  TIMESTAMP    NULL COMMENT 'NULL',
    `start_time`  TIMESTAMP    NOT NULL COMMENT '秒杀开始时间',
    `end_time`    TIMESTAMP    NOT NULL COMMENT '秒杀结束时间',
    PRIMARY KEY (id)
) ENGINE = INNODB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='秒杀库存表';

-- 初始化数据
INSERT into goods(name,price, number, start_time, end_time)
VALUES ('iphone6', 1000,100, '2016-01-01 00:00:00', '2016-01-02 00:00:00'),
       ('ipad', 1000,200, '2016-01-01 00:00:00', '2016-01-02 00:00:00'),
       ('mac book pro',1000, 300, '2016-01-01 00:00:00', '2016-01-02 00:00:00'),
       ('iMac', 7000,400, '2016-01-01 00:00:00', '2016-01-02 00:00:00');

-- 秒杀成功明细表
CREATE TABLE orders
(
    `id`    BIGINT    NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `goods_id`  BIGINT    NOT NULL COMMENT '秒杀商品ID',
    `user_id`  BIGINT    NOT NULL COMMENT '用户手机号',
    `user_name` VARCHAR(30) NOT NULL COMMENT '用户名',
    `user_address` VARCHAR(150) NOT NULL COMMENT '用户地址',
    `state`       TINYINT   NOT NULL DEFAULT -1 COMMENT '状态标识: <=0:无效  1:秒杀成功 2:已付款 3:已发货',
    `create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY(goods_id, user_id) /*一个用户只能秒杀一个商品*/
) ENGINE = INNODB
  AUTO_INCREMENT = 10000
  DEFAULT CHARSET = utf8 COMMENT ='订单表';

INSERT INTO orders(goods_id, user_id,user_name,user_address, state, create_time)
VALUES (1001,9001,'小马','广东',1,'2016-01-02 00:00:00'),
       (1002,9001,'小马','广东',1,'2016-01-02 00:00:00'),
       (1003,9001,'小马','广东',0,'2016-01-02 00:00:00');

--添加索引
alter table `goods` add index index_end_time(end_time);


-- 建表时可能出现【Invalid default value for 】的错误，需要输入
-- set @@sql_mode ='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';

-- 连接MySQL时可能会出现
-- Server returns invalid timezone. Go to 'Advanced' tab and set 'serverTimezone' property manually.
-- 需要输入
-- set global time_zone='+8:00';