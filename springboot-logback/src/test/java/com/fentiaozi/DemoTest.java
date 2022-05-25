package com.fentiaozi;

import com.fentiaozi.service.LogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @description
 * @author: com.fentiaozi
 * @date 2022/5/25
 */
@SpringBootTest(classes = SpringBootDemoApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class DemoTest {

    @Resource
    private LogService logService;

    private Logger log = LoggerFactory.getLogger(DemoTest.class);

    @Test
    public void testPrintLog() {
        log.info("测试打印日志开始------");
        logService.printLogInfo();
        log.info("测试打印日志结束------");
    }
}
