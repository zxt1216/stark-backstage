package com.sean.arya.stark.backstage.common.data;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Sean
 * @date 2018/5/10 10:16
 * @description  接口/Vo返回值包装类
 * @vesion 1.0.0
 */
@Data
public class Message<D> implements Serializable {
    private Integer code;
    private String msg;
    private D data;

    public Message(int code){
        this(code,null,null);
    }
    public Message(int code,String msg){
        this(code,msg,null);
    }
    public Message(int code,D data){
        this(code,null,data);
    }
    public Message(int code,String msg,D data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }

}
