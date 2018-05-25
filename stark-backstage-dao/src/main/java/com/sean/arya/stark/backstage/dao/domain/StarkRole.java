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
public class StarkRole implements Serializable{

    /**
     * 物理主键
     */
    private Long id;

    /**
     * 业务主键(角色id)
     */
    private String roleId;

    /**
     * 角色姓名
     */
    private String roleName;

    /**
     * 角色状态:禁用1,启用0
     */
    private Byte roleState;

    /**
     * 创建时间
     */
    private LocalDateTime gtmCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gtmUpdate;

}