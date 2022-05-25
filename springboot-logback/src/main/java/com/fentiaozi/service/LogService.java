package com.fentiaozi.service;

/**
 * @description
 * @author: fentiaozi
 * @date 2022/5/25
 */
public interface LogService {
    /**
     * 打印log日志
     */
    void printLogInfo();

    /**
     * 打印Debug log日志
     */
    void printDebugLogInfo();

    /**
     * 打印Info log日志
     */
    void printInfoLogInfo();

    /**
     * 打印Warn log日志
     */
    void printWarnLogInfo();

    /**
     * 打印Error log日志
     */
    void printErrorLogInfo();

}
