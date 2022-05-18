/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * http://www.visight.cn
 * <p>
 * 版权所有，侵权必究！
 */

package com.fentiaozi.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author fentiaozi
 */
@Component
public class RedisUtils {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 默认过期时长为24小时，单位：秒
     */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24L;
    /**
     * 过期时长为1小时，单位：秒
     */
    public final static long HOUR_ONE_EXPIRE = 60 * 60 * 1L;
    /**
     * 过期时长为6小时，单位：秒
     */
    public final static long HOUR_SIX_EXPIRE = 60 * 60 * 6L;
    /**
     * 过期时长为30分钟，单位：秒
     */
    public final static long MINUTE_THIRTY_EXPIRE = 30 * 60 * 1L;
    /**
     * 过期时长为30秒，单位：秒
     */
    public final static long THIRTY_SECOND_EXPIRE = 30 * 1 * 1L;
    /**
     * 过期时长为5秒，单位：秒
     */
    public final static long FIVE_SECOND_EXPIRE = 5 * 1 * 1L;
    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1L;

    public void set(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(key, value);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    public void set(String key, Object value) {
        set(key, value, DEFAULT_EXPIRE);
    }

    public Object get(String key, long expire) {
        Object value = redisTemplate.opsForValue().get(key);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
        return value;
    }

    public Object get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    public Map<String, Object> hGetAll(String key) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    public void hMSet(String key, Map<String, Object> map) {
        hMSet(key, map, DEFAULT_EXPIRE);
    }

    public void hMSet(String key, Map<String, Object> map, long expire) {
        redisTemplate.opsForHash().putAll(key, map);

        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    public void hSet(String key, String field, Object value) {
        hSet(key, field, value, DEFAULT_EXPIRE);
    }

    public void hSet(String key, String field, Object value, long expire) {
        redisTemplate.opsForHash().put(key, field, value);

        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    public void expire(String key, long expire) {
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    public void hDel(String key, Object... fields) {
        redisTemplate.opsForHash().delete(key, fields);
    }

    public void leftPush(String key, Object value) {
        leftPush(key, value, DEFAULT_EXPIRE);
    }

    public void leftPush(String key, Object value, long expire) {
        redisTemplate.opsForList().leftPush(key, value);

        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 设置指定key偏移量上的位(bit)的值
     *
     * @param key
     * @param offset 位置
     * @param value  值,true为1, false为0
     * @return
     */
    public boolean setBitMap(String key, long offset, boolean value) {
        return redisTemplate.opsForValue().setBit(key, offset, value);
    }

    /**
     * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)
     *
     * @param key
     * @param offset
     * @return
     */
    public Boolean getBitMap(String key, long offset) {
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    /**
     * 获取key
     *
     * @param key
     * @return
     */
    public Set<String> keys(String key) {
        return redisTemplate.keys(key);
    }

    public Object leftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public Object getLrange(String key, Integer start, Integer end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public void lRangeSet(String key, Integer index, Object value, long expire) {
        redisTemplate.opsForList().set(key, index, value);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    public void rightPush(String key, Object value) {
        rightPush(key, value, DEFAULT_EXPIRE);
    }

    public void rightPush(String key, Object value, long expire) {
        redisTemplate.opsForList().rightPush(key, value);

        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    /**
     * 删除集合中值等于value的元素(index=0, 删除所有值等于value的元素; index>0, 从头部开始删除第一个值等于value的元素; index<0, 从尾部开始删除第一个值等于value的元素)
     *
     * @param key
     * @param index
     * @param value
     */
    public void removeListValue(String key, Long index, Object value) {
        redisTemplate.opsForList().remove(key, index, value);
    }

    public Long getLlishlength(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public void sSet(String key, Object values) {
        sSet(key, values, DEFAULT_EXPIRE);
    }

    public void sSet(String key, Object values, long expire) {
        redisTemplate.opsForSet().add(key, values);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    public void sDel(String key, Object... values) {
        redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 获取集合所有元素
     *
     * @param key
     * @return
     */
    public Set<Object> setMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 判断集合是否包含value
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    public Long getSetSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 自增1
     *
     * @param key
     */
    public void incrValue(String key) {
        redisTemplate.opsForValue().increment(key);
    }

    /**
     * 自减1
     *
     * @param key
     */
    public void decrValue(String key) {
        redisTemplate.opsForValue().decrement(key);
    }

    public void zSSet(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    public void zSSet(String key, Object value, double score, long expire) {
        redisTemplate.opsForZSet().add(key, value, score);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    public Set<ZSetOperations.TypedTuple<Object>> zGetRangeWithScores(String key, long start, long end) {

        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

}