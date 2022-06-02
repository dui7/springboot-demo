package com.fentiaozi;

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
public class LogTest {
    private static final Logger LOGGER = LogManager.getLogger(LogTest.class);

    @Test
    public static void main(String[] args) {
        LOGGER.debug("Debug level log message");
        LOGGER.info("Info level log message");
        LOGGER.warn("Warn level log message");
        LOGGER.error("Error level log message");
    }

}
