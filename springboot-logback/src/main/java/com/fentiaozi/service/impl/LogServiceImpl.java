package com.fentiaozi.service.impl;

import com.fentiaozi.service.LogService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;


/**
 * @description
 * @author: fentiaozi
 * @date 2022/5/25
 */
@Service
public class LogServiceImpl implements LogService {
    Logger log = LoggerFactory.getLogger(LogServiceImpl.class);

    @Override
    public void printLogInfo() {
        log.trace("{} this is trace log ...", "打印日志信息");
        log.debug("{} this is debug log ...", "打印日志信息");
        log.info("{} this is info log ...", "打印日志信息");
        log.warn("{} this is warn log ...", "打印日志信息");
        log.error("{} this is error log ...", "打印日志信息");
    }

    @Override
    public void printDebugLogInfo() {
        log.debug("this is debug log ...");
    }

    @Override
    public void printInfoLogInfo() {
        log.info("this is info log ...");
    }

    @Override
    public void printWarnLogInfo() {
        log.info("this is warn log ...");
    }

    @Override
    public void printErrorLogInfo() {
        log.error("this is error log ...");
    }
}
