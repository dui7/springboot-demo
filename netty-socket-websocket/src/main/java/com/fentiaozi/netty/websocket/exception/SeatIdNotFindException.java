package com.fentiaozi.netty.websocket.exception;

/**
 * 坐席未找到异常
 *
 * @author visi
 */
public class SeatIdNotFindException extends RuntimeException {
    public SeatIdNotFindException(String msg) {
        super(msg);
    }

    public SeatIdNotFindException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
