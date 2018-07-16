package org.seckill.dao;

/**
 * Created by junbiao on 2018/7/11.
 */

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKill;

/**
 * 秒杀明细表的数据访问层
 * 注意：商品id和用户手机号组成了联合主键，保证了记录的唯一性
 */
public interface SuccessKillDao {
    /**
     * 增加一条秒杀成功记录
     * @param seckillId  商品库存id
     * @param userPhone  用户手机号码
     * @return    影响的行数（大于等于1，操作成功）
     */
    int insertSuccessKill(@Param("secckillId") int seckillId, @Param("userPhone") String userPhone);

    /**
     * 根据商品id和手机号查询秒杀成功记录
     * @param seckillId  商品库存id
     * @param userPhone  用户手机号码
     * @return    影响的行数（大于等于1，操作成功）
     */
    SuccessKill queryByIdWithSecKill(@Param("seckillId") int seckillId,@Param("userPhone") String userPhone);
}
