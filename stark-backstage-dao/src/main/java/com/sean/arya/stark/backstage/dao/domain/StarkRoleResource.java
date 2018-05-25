package com.sean.arya.stark.backstage.dao.domain;

import java.lang.Long;
import java.time.LocalDateTime;
import java.lang.String;
import lombok.Data;
import java.io.Serializable;

/**
 *  Entity
 * @author Sean
 * @date 2018-05-09
 */
@Data
public class StarkRoleResource implements Serializable{

    /**
     * 物理主键
     */
    private Long id;

    /**
     * 业务主键(角色资源id)
     */
    private String roleResourceId;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 资源id
     */
    private String resourceId;

    /**
     * 权限CRUD:0000,全权限,0011,读创建,0001,读修改,1011,只读
     */
    private String permission;

    /**
     * 创建时间
     */
    private LocalDateTime gtmCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gtmUpdate;

}