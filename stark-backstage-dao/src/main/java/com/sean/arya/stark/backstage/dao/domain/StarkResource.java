package com.sean.arya.stark.backstage.dao.domain;

import java.lang.Long;
import java.lang.Integer;
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
public class StarkResource implements Serializable{

    /**
     * 物理主键
     */
    private Long id;

    /**
     * 业务主键(资源id)
     */
    private String resourceId;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源类型:菜单,按钮,超链接
     */
    private Byte resourceType;

    /**
     * 菜单资源父节点
     */
    private String resourceMenuParent;

    /**
     * 是否是叶子节点,0:是,1 不是
     */
    private Byte resourceMenuLeaf;

    /**
     * 节点排序号
     */
    private Integer resourceMenuIdx;

    /**
     * 资源访问路径
     */
    private String resourceUrl;

    /**
     * 资源状态:禁用1,启用0
     */
    private Byte resourceState;

    /**
     * 创建时间
     */
    private LocalDateTime gtmCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gtmUpdate;

}