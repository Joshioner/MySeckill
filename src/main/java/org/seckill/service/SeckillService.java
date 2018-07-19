package org.seckill.service;

/**
 * Created by junbiao on 2018/7/15.
 */

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExcution;
import org.seckill.entity.SecKill;
import org.seckill.exception.RepeatException;
import org.seckill.exception.SeckillClosedException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * 库存商品service类
 */
public interface SeckillService {

    //根据ID查询商品信息
    SecKill qetById(int seckillId);

    //分页查询
    List<SecKill> getSeckillList(int offset,int limit);

    //查询所有
    List<SecKill> getAll();

    //暴露秒杀网址
    Exposer exposeSeckillUrl(int seckillId);

    //执行秒杀
    SeckillExcution executeSeckill(int seckillId, String userPhone, String md5)throws RepeatException,SeckillClosedException,SeckillException;

    //执行秒杀，使用存储过程
    SeckillExcution executeSeckillByProcedure(int seckillId, String userPhone, String md5);

}
