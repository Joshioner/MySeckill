package org.seckill.exception;

/**
 * Created by junbiao on 2018/7/15.
 */
//重复秒杀类
public class RepeatException extends SeckillException{
    public RepeatException(String message) {
        super(message);
    }

    public RepeatException(String message, Throwable cause) {
        super(message, cause);
    }
}
