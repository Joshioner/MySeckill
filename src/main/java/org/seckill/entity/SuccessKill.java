package org.seckill.entity;



/**
 * Created by junbiao on 2018/7/11.
 */

/**
 * 秒杀明细表
 */
public class SuccessKill {
    private Integer seckillId;

    private String userPhone;

    private String createTime;

    private Short state;

    private SecKill secKill;

    public Integer getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Integer seckillId) {
        this.seckillId = seckillId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public SecKill getSecKill() {
        return secKill;
    }

    public void setSecKill(SecKill secKill) {
        this.secKill = secKill;
    }

    @Override
    public String toString() {
        return "SuccessKill{" +
                "seckillId=" + seckillId +
                ", userPhone='" + userPhone + '\'' +
                ", createTime=" + createTime +
                ", state=" + state +
                ", secKill=" + secKill.toString() +
                '}';
    }
}
