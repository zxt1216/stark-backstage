package com.sean.arya.stark.backstage.common.data;

/**
 * @author Sean
 * @date 2018/5/10 10:27
 * @description  Message 返回码枚举
 * @vesion 1.0.0
 */
public enum Code {
    SUCCESS(0,"成功"),
    FAIL(1<<30,"失败"),
    SYSTEM_ERROR(3<<29,"系统错误"),//包含 未捕获异常
    SYSTEM_EXCEPTION(5<<28,"系统异常"),
    ILLEGAL_AUTH(11<<27,"权限异常"),
    ILLEGAL_PARAM(21<<26,"参数异常"),
    ILLEGAL_DATA(41<<25,"数据异常"),//中间数据,结果集
    ILLEGAL_FORMAT(81<<24,"数据格式化异常"),
    ILLEGAL_CONFIGATION(161<<23,"配置异常"),//包含数据,环境配置
    ILLEAGAL_REFLECTION(321<<22,"反射异常"),//类未找到,反射非法
    BUSINESS_EXCEPTION(641<<21,"业务异常"),
    ;
    public Integer code;
    public String msg;
    Code(int code,String msg){
        this.code=code;
        this.msg=msg;
    }
}
