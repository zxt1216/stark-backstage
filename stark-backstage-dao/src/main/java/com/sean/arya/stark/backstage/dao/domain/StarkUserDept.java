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
public class StarkUserDept implements Serializable{

    /**
     * 物理主键
     */
    private Long id;

    /**
     * 业务主键(用户-部门id)
     */
    private String userDeptId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 部门id
     */
    private Byte deptId;

    /**
     * 创建时间
     */
    private LocalDateTime gtmCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gtmUpdate;

}