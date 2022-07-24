package com.fentiaozi.delayqueue.handle;

/**
 * @description 延迟队列执行器
 * @author: fentiaozi
 * @date 2022/7/24
 */
public interface RedisDelayQueueHandle<T> {

    void execute(T t);

}