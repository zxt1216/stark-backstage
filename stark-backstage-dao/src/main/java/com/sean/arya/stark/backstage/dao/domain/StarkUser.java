package com.sean.arya.stark.backstage.dao.domain;

import java.lang.Long;
import java.time.LocalDateTime;
import java.lang.String;
import java.lang.Byte;
import lombok.Data;
import java.io.Serializable;

/**
 *  Entity
 * @author Sean
 * @date 2018-05-09
 */
@Data
public class StarkUser implements Serializable{

    /**
     * 物理主键
     */
    private Long id;

    /**
     * 业务主键(用户id)
     */
    private String userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 用户密码[加密]
     */
    private String userPassword;

    /**
     * 用户密码盐
     */
    private String userSalt;

    /**
     * 用户状态:禁用1,启用0
     */
    private Byte userState;

    /**
     * 创建时间
     */
    private LocalDateTime gtmCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gtmUpdate;

}