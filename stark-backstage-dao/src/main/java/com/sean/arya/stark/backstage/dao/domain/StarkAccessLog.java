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
public class StarkAccessLog implements Serializable{

    /**
     * 物理主键
     */
    private Long id;

    /**
     * 业务主键(用户角色id)
     */
    private String accessLogId;

    /**
     * ip
     */
    private String accessIp;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 操作类型:查询0,创建1,修改2,删除3
     */
    private Byte operateType;

    /**
     * 操作参数json
     */
    private String operateReq;

    /**
     * 备注,失败原因
     */
    private String operatorNote;

    /**
     * 创建时间
     */
    private LocalDateTime gtmCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gtmUpdate;

}