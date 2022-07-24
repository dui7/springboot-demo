package com.fentiaozi.delayqueue.controller;


import com.fentiaozi.delayqueue.constant.RedisDelayQueueEnum;
import com.fentiaozi.delayqueue.service.RedisDelayQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description
 * @author: fentiaozi
 * @date 2022/7/24
 */
@RestController
public class RedisDelayQueueController {

    @Autowired
    private RedisDelayQueueService redisDelayQueueService;

    @GetMapping("/addQueue")
    public void addQueue() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("orderId", "100");
        map1.put("remark", "订单支付超时，自动取消订单");

        Map<String, String> map2 = new HashMap<>();
        map2.put("orderId", "200");
        map2.put("remark", "订单超时未评价，系统默认好评");

        // 添加订单支付超时，自动取消订单延迟队列。为了测试效果，延迟10秒钟
        redisDelayQueueService.addDelayQueue(map1, 10, TimeUnit.SECONDS, RedisDelayQueueEnum.ORDER_PAYMENT_TIMEOUT.getCode());

        // 订单超时未评价，系统默认好评。为了测试效果，延迟20秒钟
        redisDelayQueueService.addDelayQueue(map2, 20, TimeUnit.SECONDS, RedisDelayQueueEnum.ORDER_TIMEOUT_NOT_EVALUATED.getCode());

        Map<String, String> map3 = new HashMap<>();
        map3.put("orderId", "300");
        map3.put("remark", "订单超时未评价，系统默认好评");
        redisDelayQueueService.addDelayQueue(map3, 500, TimeUnit.MILLISECONDS, RedisDelayQueueEnum.ORDER_TIMEOUT_NOT_EVALUATED.getCode());
        redisDelayQueueService.removeDelayQueue(map3, RedisDelayQueueEnum.ORDER_TIMEOUT_NOT_EVALUATED.getCode());
    }

}