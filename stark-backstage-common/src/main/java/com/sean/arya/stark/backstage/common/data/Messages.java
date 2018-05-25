package com.sean.arya.stark.backstage.common.data;

/**
 * @author Sean
 * @date 2018/5/10 10:24
 * @description Message简易封装工具类
 * @vesion 1.0.0
 */
public class Messages {
    public static <D> Message<D> success(D data){
        return new Message<>(Code.SUCCESS.code,Code.SUCCESS.msg,data);
    }
    public static <D> Message<D> success(String msg,D data){
        return new Message<>(Code.SUCCESS.code,msg,data);
    }
    public static <D> Message<D> fail(D data){
        return new Message<>(Code.FAIL.code,Code.FAIL.msg,data);
    }
    public static <D> Message<D> fail(int code,D data){
        return new Message<>(code,"",data);
    }
    public static <D> Message<D> fail(int code,String msg,D data){
        return new Message<>(code,msg,data);
    }
    //exception 和 error 不允许自定义返回Code
    public static <D> Message<D> exception(Code code,D data){
        return new Message<>(code.code,code.msg,data);
    }
    public static <D> Message<D> exception(Code code,String msg,D data){
        return new Message<>(code.code,msg,data);
    }
    public static <D> Message<D> exception(Code[] codes,D data){
        int c=0;
        String msg="";
        for(Code code:codes){
            c=c|code.code;
            msg=msg+","+code.msg;
        }
        return new Message<>(c,msg,data);
    }
    public static <D> Message<D> exception(Code[] codes,String msg,D data){
        int c=0;
        for(Code code:codes){
            c=c|code.code;
        }
        return new Message<>(c,msg,data);
    }
    public static <D> Message<D> error(String msg,D data){
        return new Message<>(Code.SYSTEM_ERROR.code,msg,data);
    }
    public static <D> Message<D> error(D data){
        return new Message<>(Code.SYSTEM_ERROR.code, Code.SYSTEM_ERROR.msg,data);
    }
}
