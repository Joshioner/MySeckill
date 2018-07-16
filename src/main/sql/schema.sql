-- 数据库初始脚本


-- 创建数据库
CREATE DATABASE seckill;

-- 使用数据库
use seckill;

--创建秒杀库存表
CREATE TABLE secKill(
seckill_id BIGINT NOT NULL AUTO_INCREMENT COMMENT  '商品库存id',
name  VARCHAR(20) NOT  NULL  COMMENT '商品名称',
number int NOT  NULL COMMENT '库存数量',
start_time TIMESTAMP NOT  NULL COMMENT '秒杀开始时间',
end_time TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY(seckill_id)
)ENGINE = InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET = utf8   COMMENT = '秒杀库存表'



-- 初始化数据
insert into
     seckill(name,number,start_time,end_time)
values
  ("100元秒杀iPhone",10,"2018-05-20 00:00:00","2018-05-21 00:00:00"),
  ("50元秒杀ipad",5,"2018-04-15 00:00:00","2018-04-16 00:00:00"),
  ("10元秒杀吹风筒",40,"2018-06-20 10:00:00","2018-06-21 10:00:00")

-- 秒杀成功明细表
-- 用户登录验证相关的信息
CREATE TABLE successKill(
 seckill_id bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
 user_phone bigint NOT NULL COMMENT '用户手机号',
 create_time TIMESTAMP NOT NULL  COMMENT '创建时间',
 state TINYINT DEFAULT -1 COMMENT '秒杀的状态 -1：无效 0：秒杀成功 1：已付款 2：已发货',
 PRIMARY KEY (seckill_id,user_phone)
)ENGINE = InnoDB  DEFAULT charset =utf8 COMMENT = "秒杀成功明细表"