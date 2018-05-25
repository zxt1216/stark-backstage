package com.sean.arya.stark.backstage.common.exceptions;

import lombok.Data;

/**
 * @author Sean
 * @date 2018/5/10 11:58
 * @description  入参异常
 * @vesion 1.0.0
 */
@Data
public class ParamException extends RuntimeException{
    private Integer code;
    private String msg;
    public ParamException(int code,String msg){
        this.code=code;
        this.msg=msg;
    }
    public ParamException(int code){
        this.code=code;
    }
}
