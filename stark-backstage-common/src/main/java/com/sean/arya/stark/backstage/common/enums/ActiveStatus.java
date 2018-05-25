package com.sean.arya.stark.backstage.common.enums;

/**
 * @author Sean
 * @date 2018/5/8 9:44
 * @description 信息激活状态枚举
 * @vesion 1.0.0
 */
public enum ActiveStatus {
    ACTIVE_STATUS_ENABLE("0","启用"),
    ACTIVE_STATUS_DISABLE("1","禁用"),
    ;
    ActiveStatus(String code,String msg){
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
