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
public class StarkDept implements Serializable{

    /**
     * 物理主键
     */
    private Long id;

    /**
     * 业务主键(部门id)
     */
    private String deptId;

    /**
     * 部门姓名
     */
    private String deptName;

    /**
     * 父级部门id
     */
    private String deptParentId;

    /**
     * 部门状态:禁用1,启用0
     */
    private Byte deptState;

    /**
     * 创建时间
     */
    private LocalDateTime gtmCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gtmUpdate;

}