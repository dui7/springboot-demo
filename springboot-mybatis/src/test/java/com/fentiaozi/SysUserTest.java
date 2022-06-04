package com.fentiaozi;

import com.fentiaozi.entity.SysUserEntity;
import com.fentiaozi.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = SpringBootDemoApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SysUserTest {

    private Logger log = LoggerFactory.getLogger(SysUserTest.class);

    @Resource
    private SysUserService sysUserService;

    @Test
    public void testInsert() {
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setUserName("zhangsan");
        sysUserEntity.setPassword("123456");
        sysUserEntity.setGender(1);
        sysUserEntity.setRealName("张三");
        sysUserEntity.setCreator(1L);
        sysUserEntity.setCreateDate(new Date());

        sysUserService.save(sysUserEntity);
        log.info("插入数据成功");
    }

    @Test
    public void testDelete() {
        sysUserService.delete(4);
        log.info("删除数据成功");
    }

    @Test
    public void testUpdate() {
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setId(2L);
        sysUserEntity.setUserName("zhangsan");
        sysUserEntity.setPassword("123456");
        sysUserEntity.setGender(2);
        sysUserEntity.setRealName("小张");
        sysUserEntity.setUpdater(1L);
        sysUserEntity.setUpdateDate(new Date());
        sysUserService.update(sysUserEntity);
        log.info("修改数据成功");
    }

    @Test
    public void testGet() {
        Long id = 2L;
        SysUserEntity sysUserEntity = sysUserService.get(id);
        log.info("查询id:{}的数据为:{}", id, sysUserEntity.toString());
    }


    @Test
    public void testSelectList() {
        Map<String, Object> map = new HashMap();
        map.put("userName","admin");
        List<SysUserEntity> list = sysUserService.selectList(map);
        log.info("查询参数:{}的数据为:{}", map.toString(), list.toString());
    }
}
