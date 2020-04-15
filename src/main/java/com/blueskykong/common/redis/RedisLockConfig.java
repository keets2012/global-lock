package com.blueskykong.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by keets on 2017/12/26.
 * 基于Redis实现的分布式锁
 */
public final class RedisLockConfig {

    private static Logger logger = LoggerFactory.getLogger(RedisLockConfig.class);

    /**
     * redis操作帮助类,可以是其他封装了redis操作的类
     */
    private RedisHelper redisHelper;

    public static final long DEFAULT_TIMEOUT = 30 * 1000;

    public static final long DEFAULT_SLEEP_TIME = 100;

    private RedisLockConfig(RedisHelper redisHelper) {
        this.redisHelper = redisHelper;
    }

    public static RedisLockConfig getInstance(RedisHelper redisHelper) {
        return new RedisLockConfig(redisHelper);
    }

    /**
     * 创建锁
     *
     * @param mutex     互斥量
     * @param timeout   锁的超时时间
     * @param sleepTime 线程自旋尝试获取锁时的休眠时间
     * @param timeUnit  时间单位
     */
    public RedisLock newLock(String mutex, long timeout, long sleepTime, TimeUnit timeUnit) {
//        redisTemplate.
        logger.info("创建分布式锁,互斥量为{}", mutex);
        return new RedisLock(mutex, timeout, sleepTime, timeUnit, redisHelper);
    }

    public RedisLock newLock(String mutex, long timeout, TimeUnit timeUnit) {
        return newLock(mutex, timeout, DEFAULT_SLEEP_TIME, timeUnit);
    }

    public RedisLock newLock(String mutex) {
        return newLock(mutex, DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
    }
}
