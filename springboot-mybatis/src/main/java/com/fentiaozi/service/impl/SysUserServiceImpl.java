package com.fentiaozi.service.impl;

import com.fentiaozi.dao.SysUserDao;
import com.fentiaozi.entity.SysUserEntity;
import com.fentiaozi.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: fentiaozi
 * @date 2022/6/3
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    @Resource
    private SysUserDao sysUserDao;

    @Override
    public int save(SysUserEntity entity) {
        return sysUserDao.save(entity);
    }

    @Override
    public int delete(Integer id) {
        return sysUserDao.delete(id);
    }

    @Override
    public int update(SysUserEntity entity) {
        return sysUserDao.update(entity);
    }

    @Override
    public SysUserEntity get(Long id) {
        return sysUserDao.get(id);
    }

    @Override
    public List<SysUserEntity> selectList(Map<String, Object> map) {
        return sysUserDao.selectList(map);
    }

}
