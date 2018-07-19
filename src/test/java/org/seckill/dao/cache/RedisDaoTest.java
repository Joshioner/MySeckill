package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SecKill;
import org.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by junbiao on 2018/7/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class RedisDaoTest {
    //依赖注入
    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillService seckillService;
    @Test
    public void getSeckill() throws Exception {
        int seckillId = 1000;
        SecKill secKill = redisDao.getSeckill(seckillId);
        //不存在缓存
        if (secKill == null){
            secKill = seckillService.qetById(seckillId);
            if (secKill != null){
                System.out.println("------------");
                System.out.println(secKill);
                redisDao.putSeckill(secKill);
                secKill = redisDao.getSeckill(seckillId);
                System.out.println(secKill);
                System.out.println("------------");
            }
        }else{
            System.out.println("------------");
            System.out.println(secKill.toString());
            System.out.println("------------");
        }
    }

}