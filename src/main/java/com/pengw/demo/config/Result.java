package com.pengw.demo.config;

import lombok.Data;

@Data
public class Result<T> {
    private Object message;
    private int retCode;
    private T data;

    public Result(){

    }

    private Result(T data) {
        this.retCode = 0;
        this.message = "成功";
        this.data = data;
    }

    private Result(Integer code ,Object message) {
        this.retCode = code;
        this.message = message;
    }

    /**
     * 成功时候的调用
     * @return
     */
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }
    /**
     * 成功，不需要传入参数
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> success(){
        return (Result<T>) success("");
    }
    /**
     * 失败时候的调用
     * @return
     */

    public static <T> Result<T> error(Object message){
        return new Result<T>(-1,message);
    }

    /**
     * 失败时候的调用,扩展消息参数
     * @param code
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(Integer code, String msg){
        return new Result<T>(code,msg);
    }
}
