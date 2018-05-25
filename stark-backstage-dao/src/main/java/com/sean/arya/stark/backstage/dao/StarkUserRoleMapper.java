package com.sean.arya.stark.backstage.dao;

import java.lang.Integer;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.sean.arya.stark.backstage.dao.domain.StarkUserRole;

/**
 *  Dao
 * @author Sean
 * @date 2018-05-09
 */
public interface StarkUserRoleMapper{
    /**
     * 根据主键查询
     * 
     * @param userRoleId 主键值
     * @return 根据主键查询到的对象
     */
     StarkUserRole get(String userRoleId);

    /**
     * 根据主键更新[更新非空字段]
     * 
     * @param entity 待更新的对象
     * @return 更新where条件影响条数
     */
     int update(StarkUserRole entity);

    /**
     * 根据条件统计
     * 
     * @param entity 条件对象
     * @return 统计条数
     */
     int count(StarkUserRole entity);

    /**
     * 根据主键更新[覆盖]
     * 
     * @param entity 待更新的对象
     * @return 更新where条件影响条数
     */
     int cover(StarkUserRole entity);

    /**
     * 插入一条数据
     * 
     * @param entity 待插入的对象
     */
     void insert(StarkUserRole entity);

    /**
     * 根据条件查询
     * 
     * @param entity 条件对象
     * @return 条件符合数据
     */
     List<StarkUserRole> query(StarkUserRole entity);

    /**
     * 根据条件分页查询
     * 
     * @param entity 条件对象
     * @param start 起始条数
     * @param size 条数
     * @return 条件符合数据
     */
     List<StarkUserRole> page(@Param("entity") StarkUserRole entity, @Param("start") Integer start, @Param("size") Integer size);

    /**
     * 根据主键删除
     * 
     * @param userRoleId 主键值
     * @return 删除条数
     */
     int delete(String userRoleId);

    /**
     * 批量插入
     * 
     * @param entities 待更新的对象
     */
     void batchInsert(@Param("entities") List<StarkUserRole> entities);

}