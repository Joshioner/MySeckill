package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SecKill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by junbiao on 2018/7/12.
 */

/**
 * 商品库存表测试类
 */
//spring和JUnit整合
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SecKillDaoTest {


    private final static Logger LOG = LoggerFactory.getLogger(SecKillDaoTest.class);
    //注入依赖类Dao
    @Autowired
    private SecKillDao secKillDao;
    @Test
    public void reduceNumberTest() throws Exception {
       int id = 1000;
       int row = secKillDao.reduceNumber(id,new Date());
        System.out.println("------------------");
        System.out.println(row);
        System.out.println("------------------");
    }

    @Test
    public void queryByIdTest() throws Exception {
        int seckillId = 1000;
        SecKill secKill = secKillDao.queryById(seckillId);
        System.out.println("------------------");
        LOG.info(secKill.toString());
        System.out.println("------------------");
    }

    @Test
    public void queryAllTest() throws Exception {
        List<SecKill> list = secKillDao.queryAll(0,3);
        System.out.println("------------------");
        for (SecKill secKill:list){
          LOG.info(secKill.toString());
        }
        System.out.println("------------------");
    }

}