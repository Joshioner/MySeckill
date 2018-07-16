package org.seckill.exception;

/**
 * Created by junbiao on 2018/7/15.
 */
//秒杀已关闭异常
public class SeckillClosedException  extends SeckillException{
    public SeckillClosedException(String message) {
        super(message);
    }

    public SeckillClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}
