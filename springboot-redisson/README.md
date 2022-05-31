# Redisson分布式锁

## 使用
### 引入依赖
```xml
            <!-- redisson  -->
            <dependency>
                <groupId>org.redisson</groupId>
                <artifactId>redisson</artifactId>
                <version>3.9.0</version>
            </dependency>
```

### 加锁代码
```java
        RLock lock = redissonClient.getLock("lock");
        boolean getLock = false;
        try {
            lock.lock();
            // 尝试加锁，最多等待100毫秒，上锁以后110毫秒自动解锁
            if (getLock = lock.tryLock(100, 110, TimeUnit.MILLISECONDS)) {
                // todo 加锁业务逻辑代码 
            }
        } catch (Exception e) {
            log.error("redisson 获取分布式锁异常:{}", e.getMessage());
        } finally {
            if (!getLock) {
                log.warn("redisson 没有获取到锁");
            }
            // 并发高时，可能被其他线程释放掉锁,所以只能由当前获取到锁的线程释放锁
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("释放 redisson锁");
            }
        }

```