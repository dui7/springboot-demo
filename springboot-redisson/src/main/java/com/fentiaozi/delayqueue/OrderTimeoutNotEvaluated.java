package com.fentiaozi.delayqueue;

import com.fentiaozi.delayqueue.handle.RedisDelayQueueHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description 订单超时未评价
 * @author: fentiaozi
 * @date 2022/7/24
 */
@Component
@Slf4j
public class OrderTimeoutNotEvaluated implements RedisDelayQueueHandle<Map> {
    @Override
    public void execute(Map map) {
        log.info("(收到订单超时未评价延迟消息) {}", map);
        // TODO 订单超时未评价，系统默认好评处理业务...

    }
}
