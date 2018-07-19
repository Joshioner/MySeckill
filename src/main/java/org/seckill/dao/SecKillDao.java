package org.seckill.dao;

import org.seckill.entity.SecKill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by junbiao on 2018/7/11.
 */

/**
 * 商品库存表的数据访问层
 */
public interface SecKillDao {
    /**
     *  根据商品的id减少库存数量
     * @param seckillId  商品库存id
     * @param killTime   操作时间
     * @return   影响的行数（result > 0 操作成功 result <= 0 操作失败）
     */
    int reduceNumber(@Param("seckillId") Integer seckillId,@Param("killTime")  Date  killTime);

    /**
     * 根据商品id查询商品库存信息
     * @param seckillId  商品库存id
     * @return
     */
    SecKill queryById(@Param("seckillId") Integer seckillId);

    /**
     * 分页查询商品库存信息
     * @param offset   偏移量（查询的起始条数）
     * @param limit    数量限制
     * @return
     */
    List<SecKill> queryAll(@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 查询所有发商品库存信息
     * @return
     */
    List<SecKill> querySeckillList();

    /**
     * 调用存储过程执行秒杀
     */
    void killByProcedure(Map<String,Object> map);
}
