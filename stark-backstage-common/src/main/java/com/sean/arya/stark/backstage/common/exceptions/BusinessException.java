package com.sean.arya.stark.backstage.common.exceptions;

/**
 * @author Sean
 * @date 2018/5/10 12:43
 * @description  业务异常
 * @vesion 1.0.0
 */
public class BusinessException extends RuntimeException {
    private Integer code;
    private String msg;
    public BusinessException(int code,String msg){
        this.code=code;
        this.msg=msg;
    }
    public BusinessException(int code){
        this.code=code;
    }
}
