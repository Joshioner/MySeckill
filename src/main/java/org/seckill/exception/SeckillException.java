package org.seckill.exception;

/**
 * Created by junbiao on 2018/7/15.
 */
//秒杀异常类
public class SeckillException extends RuntimeException{
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
