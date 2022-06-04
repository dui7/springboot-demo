package com.fentiaozi.dao;

import com.fentiaozi.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: fentiaozi
 * @date 2022/6/3
 */
@Mapper
public interface SysUserDao {
    /**
     * 插入
     *
     * @param user
     * @return
     */
    int save(SysUserEntity user);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    int delete(Integer id);

    /**
     * 更新
     *
     * @param entity
     * @return
     */
    int update(SysUserEntity entity);

    /**
     * 通过id查询数据
     *
     * @param id
     * @return
     */
    SysUserEntity get(Long id);

    /**
     * 查询列表
     *
     * @param map
     * @return
     */
    List<SysUserEntity> selectList(Map<String, Object> map);
}
