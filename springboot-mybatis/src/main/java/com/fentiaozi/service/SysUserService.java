package com.fentiaozi.service;

import com.fentiaozi.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: fentiaozi
 * @date 2022/6/3
 */
public interface SysUserService {
    /**
     * 插入
     *
     * @param entity
     * @return
     */
    int save(SysUserEntity entity);

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
     * 通过id查询
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
