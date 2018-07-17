package org.seckill.service.Imp;

import org.seckill.dao.SecKillDao;
import org.seckill.dao.SuccessKillDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExcution;
import org.seckill.entity.SecKill;
import org.seckill.entity.SuccessKill;
import org.seckill.enums.SeckillStateEnums;
import org.seckill.exception.RepeatException;
import org.seckill.exception.SeckillClosedException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by junbiao on 2018/7/15.
 */
//seckillService实现类
@Service
@Transactional
public class SeckillServiceImp implements SeckillService {
    private final static Logger LOG = LoggerFactory.getLogger(SeckillServiceImp.class);
    //用于加密的混淆字符串，随机串
    private String base = "a;scq|12158*(*^&^%:Ksflknvw";
    @Autowired
    private SecKillDao secKillDao;

    @Autowired
    private SuccessKillDao successKillDao;
    @Override
    public SecKill qetById(int seckillId) {
        return secKillDao.queryById(seckillId);
    }

    @Override
    public List<SecKill> getSeckillList(int offset, int limit) {
        return secKillDao.queryAll(offset,limit);
    }

    @Override
    public List<SecKill> getAll() {
        return secKillDao.querySeckillList();
    }

    //暴露秒杀接口
    @Override
    public Exposer exposeSeckillUrl(int seckillId) {
        SecKill secKill = secKillDao.queryById(seckillId);
        if (secKill == null){
            return new Exposer(false,seckillId);
        }
        Date now = new Date();
        Date start = secKill.getStartTime();
        Date end = secKill.getEndTime();
        //若时间不在秒杀范围内，不进行秒杀接口暴露
        if(now.getTime() < start.getTime() || now.getTime() > end.getTime()){
            return new Exposer(false,null,seckillId,start,end,now);
        }
        //否则，则进行秒杀接口暴露
        String md5 = getMd5(seckillId);
        return new Exposer(true,seckillId,md5);
    }

    private String getMd5(int seckillId){
        String string = seckillId + "/" + base;
        String md5 = DigestUtils.md5DigestAsHex(string.getBytes());
        return md5;
    }

    @Override
    public SeckillExcution executeSeckill(int seckillId, String userPhone, String md5) throws RepeatException, SeckillClosedException, SeckillException {
        String md = getMd5(seckillId);
        if (md5 == null || !md5.trim().equals(md)){
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑:减库存+记录购买行为
        try {
           //获取系统时间
            Date nowTime = new Date();
            //减库存
            int updateCount = secKillDao.reduceNumber(seckillId,nowTime);
            if (updateCount <= 0){
                //没有减库存，秒杀结束
                throw new SeckillClosedException("seckill is closed");
            }else {
                //记录购买行为
                int insertCount = successKillDao.insertSuccessKill(seckillId,userPhone);
                if (insertCount <= 0){
                    //重复秒杀
                    throw new RepeatException("seckilled repeat");
                }else {
                    //秒杀成功，执行commit
                    SuccessKill successKill = successKillDao.queryByIdWithSecKill(seckillId,userPhone);
                    return new SeckillExcution(SeckillStateEnums.SUCCESS,seckillId,successKill);
                }
            }

        }catch(SeckillClosedException e1){
            throw e1;
        } catch(RepeatException e2){
            throw e2;
        }catch (SeckillException ex){
            LOG.error(ex.getMessage());
            throw new SeckillException("seckill inner error,{}" ,ex.getCause());

        }
    }
}
