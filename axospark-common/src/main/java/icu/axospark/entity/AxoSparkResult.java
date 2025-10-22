package icu.axospark.entity;

import lombok.Data;

import java.io.Serializable;

import static icu.axospark.constant.ResultCode.SUCCESS;
import static icu.axospark.constant.ResultCode.SUCCESS_MSG;

@Data
public class AxoSparkResult<T> implements Serializable {
    private Integer code;//编码
    private String msg;
    private T data;
    private AxoSparkResult(){

    }
    public static <T> AxoSparkResult<T> success(){
        AxoSparkResult<T> result = new AxoSparkResult<>();
        result.code = SUCCESS;
        result.msg=SUCCESS_MSG;
        return result;
    }
    public static <T> AxoSparkResult<T> success(T data) {
        AxoSparkResult<T> result = new AxoSparkResult<T>();
        result.data = data;
        result.code = 1;
        result.msg=SUCCESS_MSG;
        return result;
    }

    // 失败响应
    public static <T> AxoSparkResult<T> error(String msg) {
        AxoSparkResult<T> result = new AxoSparkResult<>();
        result.code = 0;
        result.msg = msg;
        return result;
    }

    public static <T> AxoSparkResult<T> error(Integer code, String msg) {
        AxoSparkResult<T> result = new AxoSparkResult<>();
        result.code = code;
        result.msg = msg;
        return result;
    }
    public AxoSparkResult<T> code(Integer code){
        this.code = code;
        return this;
    }
    public AxoSparkResult<T> msg(Integer code){
        this.code = code;
        return this;
    }
    public AxoSparkResult<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public AxoSparkResult<T> data(T data) {
        this.data = data;
        return this;
    }




}
