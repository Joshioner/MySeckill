package org.seckill.service.Imp;

import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SecKillDao;
import org.seckill.dao.SuccessKillDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExcution;
import org.seckill.dto.SeckillResult;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private RedisDao redisDao;
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
        /**
         * 利用redis缓存优化，进行接口暴露
         *  if 有缓存
         *     直接从缓存中取
         *  else
         *     从数据库中取数据
         *     将数据放入缓存中
         */
        //从缓存中取数据
        SecKill secKill = redisDao.getSeckill(seckillId);
        //缓存没数据
        if (secKill == null){
            //访问数据库
            secKill = secKillDao.queryById(seckillId);
            //查询不到数据
            if (secKill == null){
                return new Exposer(false,seckillId);
            }else {
                //数据库查询数据出来之后，放入缓存中
                redisDao.putSeckill(secKill);
            }
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
            //记录购买行为
            int insertCount = successKillDao.insertSuccessKill(seckillId,userPhone);
            if (insertCount <= 0){
                //重复秒杀
                throw new RepeatException("seckilled repeat");
            }else {
                //减库存
                int updateCount = secKillDao.reduceNumber(seckillId,nowTime);
                if (updateCount <= 0){
                    //没有减库存，秒杀结束
                    throw new SeckillClosedException("seckill is closed");
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

    /**
     * 调用存储过程，执行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    @Override
    public SeckillExcution executeSeckillByProcedure(int seckillId, String userPhone, String md5) {
       if (md5 == null || !md5.equals(getMd5(seckillId))){
           throw new SeckillException("seckill data rewrite");
       }
           //执行秒杀逻辑:减库存+记录购买行为
            Date now = new Date();

            Map<String,Object> map = new HashMap<String,Object>();
            map.put("seckillId",seckillId);
            map.put("killPhone",userPhone);
            map.put("killTime",now);
            map.put("result",null);
        try {
            //调用存储过程
            secKillDao.killByProcedure(map);
            int result = MapUtils.getIntValue(map,"result",-2);
            if (result == 1){
                SuccessKill successKill = successKillDao.queryByIdWithSecKill(seckillId,userPhone);
                return new SeckillExcution(SeckillStateEnums.SUCCESS,seckillId,successKill);
            }else
            {
                return new SeckillExcution(SeckillStateEnums.stateOf(result),seckillId);
            }
        }catch (Exception ex){
            return new SeckillExcution(SeckillStateEnums.INNER_ERROR,seckillId);
        }

    }


}
