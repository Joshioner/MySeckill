package org.seckill.dto;

import org.seckill.entity.SecKill;
import org.seckill.entity.SuccessKill;
import org.seckill.enums.SeckillStateEnums;

/**
 * Created by junbiao on 2018/7/15.
 */
//秒杀结果dto
public class SeckillResult {
    //秒杀状态
    private int state;
    //秒杀状态信息描述
    private String stateInfo;
    //商品id
    private int seckillId;
    //购买行为
    private SuccessKill successKill;
    //秒杀成功的构造函数
    public SeckillResult(SeckillStateEnums stateEnums, int seckillId, SuccessKill successKill) {
        this.stateInfo = stateEnums.getStateInfo();
        this.seckillId = seckillId;
        this.successKill = successKill;
    }
    //秒杀失败的构造函数

    public SeckillResult(SeckillStateEnums stateEnums, int seckillId) {
        this.stateInfo = stateEnums.getStateInfo();
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(int seckillId) {
        this.seckillId = seckillId;
    }

    public SuccessKill getSuccessKill() {
        return successKill;
    }

    public void setSuccessKill(SuccessKill successKill) {
        this.successKill = successKill;
    }

    @Override
    public String toString() {
        return "SeckillResult{" +
                "state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", seckillId=" + seckillId +
                ", successKill=" + successKill +
                '}';
    }
}
