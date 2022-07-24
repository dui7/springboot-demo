package com.fentiaozi.delayqueue.service;

import java.util.concurrent.TimeUnit;

/**
 * @description redis延迟队列工具
 * @author: fentiaozi
 * @date 2022/7/24
 */
public interface RedisDelayQueueService {

    /**
     * 添加延迟队列
     *
     * @param value     队列值
     * @param delay     延迟时间
     * @param timeUnit  时间单位
     * @param queueCode 队列键
     * @param <T>
     */
    <T> void addDelayQueue(T value, long delay, TimeUnit timeUnit, String queueCode);

    /**
     * 获取延迟队列
     *
     * @param queueCode
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    <T> T getDelayQueue(String queueCode) throws InterruptedException;

    /**
     * 移除延迟队列数据
     *
     * @param value     队列值
     * @param queueCode 队列键
     * @param <T>
     */
    <T> void removeDelayQueue(T value, String queueCode);
}