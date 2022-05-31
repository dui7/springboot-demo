package com.fentiaozi;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author ruciya
 * @desc
 * @date 2022/5/18
 */
@SpringBootTest(classes = SpringBootDemoApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class DemoTest {

    private Logger log = LoggerFactory.getLogger(DemoTest.class);

    private int money = 1000;

    @Resource
    private RedissonClient redissonClient;

    @Test
    public void testLock() throws InterruptedException {
        log.info("开始");
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                takeMoneyLock(1000);  //加锁取钱
//                takeMoney(1000); //不加锁取钱
            }).start();
        }

        Thread.sleep(1000);
        log.info("结束,余额剩余 {} 元", money);
    }

    /**
     * 加锁取钱
     *
     * @param takeMoney 取出余额
     */
    public void takeMoneyLock(double takeMoney) {
        String name = Thread.currentThread().getName();
        RLock lock = redissonClient.getLock("money");
        boolean getLock = false;
        try {
            lock.lock();
            // 尝试加锁，最多等待100毫秒，上锁以后110毫秒自动解锁
            if (getLock = lock.tryLock(100, 110, TimeUnit.MILLISECONDS)) {
                if (money >= takeMoney) {
                    log.info("线程{}:取出 {} 元", name, takeMoney);
                    money -= takeMoney;
                    log.info("线程{}:取完余额还剩下{} 元", name, money);
                } else {
                    log.info("线程{}:来取钱，余额不足！", name);
                }

                Thread.sleep(100);
            }
        } catch (Exception e) {
            log.error("线程{}redisson 获取分布式锁异常:{}", name, e.getMessage());
        } finally {
            if (!getLock) {
                log.warn("线程{} redisson 没有获取到锁", name);
            }
            // 并发高时，可能被其他线程释放掉锁，所以只能由当前获取到锁的线程释放锁
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("线程{} 释放 redisson 锁", name);
            }
        }
    }

    /**
     * 取钱
     *
     * @param takeMoney 取钱
     */
    public void takeMoney(double takeMoney) {
        String name = Thread.currentThread().getName();

        if (money >= takeMoney) {
            log.info("线程{}:取出 {} 元", name, takeMoney);
            money -= takeMoney;
            log.info("线程{}:取完余额还剩下{} 元", name, money);
        } else {
            log.info("线程{}:来取钱，余额不足！", name);
        }
    }
}

