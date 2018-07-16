package org.seckill.dto;

import java.util.Date;

/**
 * Created by junbiao on 2018/7/15.
 */
public class Exposer {

    //商品id
    private int seckillId;
    //是否开启秒杀
    private boolean exposed;
    //md5加密
    private String md5;
    //秒杀开始时间
    private Date startTime;
    //秒杀结束时间
    private Date endTime;
    //系统时间
    private Date now;

    public Exposer(boolean exposed,int seckillId, String md5) {
        this.seckillId = seckillId;
        this.exposed = exposed;
        this.md5 = md5;
    }

    public Exposer(boolean exposed, String md5,int seckillId,Date startTime, Date endTime, Date now) {
        this.md5 = md5;
        this.seckillId = seckillId;
        this.exposed = exposed;
        this.startTime = startTime;
        this.endTime = endTime;
        this.now = now;
    }

    public Exposer(boolean exposed,int seckillId) {
        this.seckillId = seckillId;
        this.exposed = exposed;
    }

    public int getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(int seckillId) {
        this.seckillId = seckillId;
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "seckillId=" + seckillId +
                ", exposed=" + exposed +
                ", md5='" + md5 + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", now=" + now +
                '}';
    }
}
