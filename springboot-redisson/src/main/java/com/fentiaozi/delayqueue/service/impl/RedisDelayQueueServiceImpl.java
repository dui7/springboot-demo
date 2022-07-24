package com.fentiaozi.delayqueue.service.impl;

import com.fentiaozi.delayqueue.service.RedisDelayQueueService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description redis延迟队列工具
 * @author: fentiaozi
 * @date 2022/7/24
 */
@Slf4j
@Service
public class RedisDelayQueueServiceImpl implements RedisDelayQueueService {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 添加延迟队列
     *
     * @param value     队列值
     * @param delay     延迟时间
     * @param timeUnit  时间单位
     * @param queueCode 队列键
     * @param <T>
     */
    @Override
    public <T> void addDelayQueue(T value, long delay, TimeUnit timeUnit, String queueCode) {
        try {
            RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(queueCode);
            RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
            delayedQueue.offer(value, delay, timeUnit);
            log.info("(添加延时队列成功) 队列键：{}，队列值：{}，延迟时间：{}", queueCode, value, timeUnit.toSeconds(delay) + "秒");
        } catch (Exception e) {
            log.error("(添加延时队列失败) {}", e.getMessage());
            throw new RuntimeException("(添加延时队列失败)");
        }
    }

    /**
     * 获取延迟队列
     *
     * @param queueCode
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    @Override
    public <T> T getDelayQueue(String queueCode) throws InterruptedException {
        RBlockingDeque<Map> blockingDeque = redissonClient.getBlockingDeque(queueCode);
        T value = (T) blockingDeque.take();
        return value;
    }

    /**
     * 移除延迟队列数据
     *
     * @param value     队列值
     * @param queueCode 队列键
     * @param <T>
     */
    @Override
    public <T> void removeDelayQueue(T value, String queueCode) {
        try {
            RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(queueCode);
            RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
            delayedQueue.remove(value);
            log.info("(移除延迟队列数据成功) 队列键：{}，队列值：{}", queueCode, value);
        } catch (Exception e) {
            log.error("(移除延迟队列数据失败) {}", e.getMessage());
            throw new RuntimeException("(移除延迟队列数据失败)");
        }
    }
}