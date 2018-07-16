package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by junbiao on 2018/7/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SuccessKillDaoTest {

    private final static Logger LOG = LoggerFactory.getLogger(SuccessKillDaoTest.class);

    @Autowired
    private SuccessKillDao successKillDao;
    @Test
    public void insertSuccessKillTest() throws Exception {
        int row = successKillDao.insertSuccessKill(1000,"12334344");
        System.out.println("------------------");
        System.out.println(row);
        System.out.println("------------------");
    }

    @Test
    public void queryByIdWithSecKill() throws Exception {
         SuccessKill successKill = successKillDao.queryByIdWithSecKill(1000,"12334344");
        System.out.println("------------------");
        System.out.println(successKill.toString());
        System.out.println("------------------");
    }

}