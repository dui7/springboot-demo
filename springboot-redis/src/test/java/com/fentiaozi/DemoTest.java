package com.fentiaozi;

import com.fentiaozi.common.redis.RedisUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author ruciya
 * @desc
 * @date 2022/5/18
 */
@SpringBootTest(classes = SpringBootDemoApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class DemoTest {

    private Logger log = LoggerFactory.getLogger(DemoTest.class);

    private static final String TEST_STRING_KEY = "test:string";
    private static final String TEST_SET_KEY = "test:set";
    private static final String TEST_LIST_KEY = "test:list";
    private static final String TEST_ZSET_KEY = "test:zset";
    private static final String TEST_COUNT_KEY = "test:count";
    @Resource
    private RedisUtils redisUtils;

    @Test
    public void set() {
        redisUtils.set(TEST_STRING_KEY, "hello redis");
        String value = (String) redisUtils.get(TEST_STRING_KEY);
        log.info("value:{}", value);
        redisUtils.set(TEST_STRING_KEY, "redis");
        String value2 = (String) redisUtils.get(TEST_STRING_KEY);
        log.info("value2:{}", value2);
    }


    @Test
    public void sset() {
        redisUtils.sSet(TEST_SET_KEY, "aaa");
        redisUtils.sSet(TEST_SET_KEY, "aaa");
        redisUtils.sSet(TEST_SET_KEY, "bbb");
        redisUtils.sSet(TEST_SET_KEY, "ccc");
    }

    @Test
    public void list() {
        //删除key
        redisUtils.delete(TEST_LIST_KEY);

        // 存值
        redisUtils.leftPush(TEST_LIST_KEY, "一二三四五");
        redisUtils.leftPush(TEST_LIST_KEY, "上山打老虎");
        redisUtils.leftPush(TEST_LIST_KEY, "老虎没打着");
        redisUtils.leftPush(TEST_LIST_KEY, "打到小松鼠");
        redisUtils.leftPush(TEST_LIST_KEY, "松鼠有几个");
        redisUtils.leftPush(TEST_LIST_KEY, "让我数一数");

        // 取出所有值
        List<String> list = (List) redisUtils.getLrange(TEST_LIST_KEY, 0, -1);
        for (String word : list) {
            log.info("list value:{}", word);
        }

        // 从左取值
        String leftPopValue = (String) redisUtils.leftPop(TEST_LIST_KEY);
        log.info("list从左取出来的 value：{}", leftPopValue);

        // 从右取值
        String rightPopValue = (String) redisUtils.rightPop(TEST_LIST_KEY);
        log.info("list从右取出来的 value：{}", rightPopValue);

    }

    @Test
    public void zSet() {
        redisUtils.zSSet(TEST_ZSET_KEY, "张三", 100);
        redisUtils.zSSet(TEST_ZSET_KEY, "李四", 80);
        redisUtils.zSSet(TEST_ZSET_KEY, "王二", 60);

        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisUtils.zGetRangeWithScores(TEST_ZSET_KEY, 0, -1);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = typedTuples.iterator();

        while (iterator.hasNext()) {
            ZSetOperations.TypedTuple<Object> next = iterator.next();
            String name = (String) next.getValue();
            Double score = next.getScore();
            log.info("zset 取值 name:{},score:{}", name, score);
        }

    }

    @Test
    public void count() {
        redisUtils.incrValue(TEST_COUNT_KEY);
        Integer num = (Integer) redisUtils.get(TEST_COUNT_KEY);
        log.info("num incrValue 1 after:{}", num);

        redisUtils.incrValue(TEST_COUNT_KEY);
        Integer num2 = (Integer) redisUtils.get(TEST_COUNT_KEY);
        log.info("num2 incrValue 1 after:{}", num2);

        redisUtils.incrValue(TEST_COUNT_KEY);
        Integer num3 = (Integer) redisUtils.get(TEST_COUNT_KEY);
        log.info("num3 incrValue 1 after:{}", num3);

        redisUtils.decrValue(TEST_COUNT_KEY);
        Integer num4 = (Integer) redisUtils.get(TEST_COUNT_KEY);
        log.info("num4 decrValue 1 after:{}", num4);

    }


}
