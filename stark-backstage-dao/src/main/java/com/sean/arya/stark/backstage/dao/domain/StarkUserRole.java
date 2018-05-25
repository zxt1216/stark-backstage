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
public class StarkUserRole implements Serializable{

    /**
     * 物理主键
     */
    private Long id;

    /**
     * 业务主键(用户角色id)
     */
    private String userRoleId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 创建时间
     */
    private LocalDateTime gtmCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gtmUpdate;

}