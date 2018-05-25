package com.sean.arya.stark.backstage.common.enums;

/**
 * @author Sean
 * @date 2018/5/8 9:55
 * @description  操作日志操作类型枚举
 * @vesion 1.0.0
 */
public enum OperateType {
    OPERATE_TYPE_CREATE("1","创建"),
    OPERATE_TYPE_READ("0","查询"),
    OPERATE_TYPE_UPDATE("2","修改"),
    OPERATE_TYPE_DELETE("3","删除"),
    ;
    OperateType(String code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    private String code;
    private String msg;
}
