package org.seckill.dto;

/**
 * Created by junbiao on 2018/7/16.
 */
public class SeckillResult<T>{

    private boolean success;

    private T data;

    private String error;

    //执行成功，返回数据信息
    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
     //执行失败，返回错误信息
    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
