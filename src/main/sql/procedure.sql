-- 定义存储过程
-- ROW_COUNT():表示上一条sql执行影响的行数 >1(执行成功) =0(主键冲突，插入失败) <1(sql执行错误)
DELIMITER $$ -- console ;

CREATE PROCEDURE execute_seckill
(in v_seckillId BIGINT,in v_kill_phone BIGINT,
 in v_kill_time TIMESTAMP,out r_result_count int)
BEGIN
   DECLARE insert_count INT DEFAULT 0;    -- 声明一个变量，默认值为0
   START TRANSACTION;    -- 开始事务
   -- 先执行 更新明细表
     INSERT IGNORE into successkill(seckill_id,user_phone,create_time)
     values(v_seckillId,v_kill_phone,v_kill_time);

     SELECT ROW_COUNT() into insert_count;

     IF(insert_count =0) THEN   -- 重复秒杀
        ROLLBACK;
        SET r_result_count = 0;
     ELSEIF(insert_count < 0) THEN   -- sql错误
        ROLLBACK;
        SET r_result_count = -1;
     ELSE                   -- 影响行数等于1，执行减库存
        update seckill set number = number - 1
            where seckill_id = v_seckillId
            and  start_time <= v_kill_time
            and  end_time >= v_kill_time
            and number > 0;
        SELECT ROW_COUNT() into insert_count;
        IF(insert_count = 0) THEN
            ROLLBACK;
            SET r_result_count = 0;
        ELSEIF(insert_count < 0) THEN
            ROLLBACK;
            SET r_result_count = -2;
        ELSE
            COMMIT;
            SET r_result_count = 1;
        END IF;
      END if;
   END;
END;
-- 存储过程结束标志
$$

-- 将结束符号修改为;
DELIMITER ;
-- 设置变量
SET @r_result_count = 0;
-- 调用存储过程
call execute_seckill(1001,123444524555,now(),@r_result_count);

DROP PROCEDURE execute_seckill
