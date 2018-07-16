package org.seckill.enums;

/**
 * Created by junbiao on 2018/7/15.
 */
//秒杀信息枚举类
public enum  SeckillStateEnums {
    //定义一系列枚举常量
    SUCCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复秒杀"),
    INNER_ERROR(-2,"系统异常"),
    DATA_REWRITE(-3,"数据篡改");
    private int state;

    private String stateInfo;

    SeckillStateEnums(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    //静态方法
    public static SeckillStateEnums stateOf(int state){
        for (SeckillStateEnums seckillStateEnums:values()){
            if (seckillStateEnums.getState() == state){
                return seckillStateEnums;
            }
        }
        return null;
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
}
