package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExcution;
import org.seckill.entity.SecKill;
import org.seckill.exception.RepeatException;
import org.seckill.exception.SeckillClosedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by junbiao on 2018/7/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
    private final static Logger log = LoggerFactory.getLogger(SeckillServiceTest.class);
    @Autowired
    private SeckillService seckillService;
    @Test
    public void qetById() throws Exception {
        int seckillId = 1000;
        SecKill secKill = seckillService.qetById(seckillId);
        log.info("-------------------");
        log.info(secKill.toString());
        log.info("-------------------");
    }

    @Test
    public void getSeckillList() throws Exception {
        List<SecKill> list = seckillService.getSeckillList(0,4);
        log.info("-------------------");
        log.info(list.toString());
        log.info("-------------------");
    }

    @Test
    public void getAll() throws Exception {
        List<SecKill> list = seckillService.getAll();
        log.info("-------------------");
        log.info(list.toString());
        log.info("-------------------");
    }

    @Test
    public void exposeSeckillUrl() throws Exception {
        int seckillId = 1001;
        Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
        log.info("-------------------");
        log.info(exposer.toString());
        log.info("-------------------");
    }

    @Test
    public void executeSeckill() throws Exception {
        int seckillId = 1001;
        String userPhone = "12233";
        String md5 = "ff6a1f17137fac9eb6542cbc233fd0ca";
        SeckillExcution seckillExcution =seckillService.executeSeckill(seckillId,userPhone,md5);
        log.info("-------------------");
        log.info(seckillExcution.toString());
        log.info("-------------------");
    }
    //集成测试代码完整逻辑，注意代码的可重复性
    @Test
    public void testSeckillLogic(){
        int seckillId = 1002;
        String userPhone = "12231312";
        Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
        if (exposer.isExposed()){
             log.info("-------------------");
             log.info("exposer={}",exposer);
            log.info("-------------------");
             String md5 = exposer.getMd5();
             try {
                 SeckillExcution result = seckillService.executeSeckill(seckillId,userPhone,md5);
                 log.info("-------------------");
                 log.info("result={}", result);
                 log.info("-------------------");
             }catch (RepeatException ex1){
                 log.info("-------------------");
                 log.error("重复秒杀");
                 log.info("-------------------");
             }catch (SeckillClosedException ex2){
                 log.error("秒杀已经结束");
             }
        }else {
            log.warn("exposer={}",exposer);
        }
    }

}