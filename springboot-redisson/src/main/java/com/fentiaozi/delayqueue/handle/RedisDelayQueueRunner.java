package com.fentiaozi.delayqueue.handle;

import com.fentiaozi.delayqueue.constant.RedisDelayQueueEnum;
import com.fentiaozi.delayqueue.handle.RedisDelayQueueHandle;
import com.fentiaozi.delayqueue.service.RedisDelayQueueService;
import com.fentiaozi.delayqueue.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @description 启动延迟队列
 * @author: fentiaozi
 * @date 2022/7/24
 */
@Slf4j
@Component
public class RedisDelayQueueRunner {

    @Resource
    private RedisDelayQueueService redisDelayQueueService;

    @PostConstruct
    public void run() {
        RedisDelayQueueEnum[] queueEnums = RedisDelayQueueEnum.values();
        for (RedisDelayQueueEnum queueEnum : queueEnums) {
            new Thread(() -> {
                try {
                    while (true) {
                        Object value = redisDelayQueueService.getDelayQueue(queueEnum.getCode());
                        RedisDelayQueueHandle redisDelayQueueHandle = SpringUtil.getBean(queueEnum.getBeanId());
                        redisDelayQueueHandle.execute(value);
                    }
                } catch (InterruptedException e) {
                    log.error("(Redis延迟队列异常中断) {}", e.getMessage());
                }
            }).start();
        }
        log.info("(Redis延迟队列启动成功)");
    }
}