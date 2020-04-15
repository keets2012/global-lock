package com.blueskykong.common.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

/**
 * Created by keets on 2018/1/3.
 */

@Configuration
public class PoolConfigs {

    public static final String POOL_MAX_WAIT_MIL = "cache.pool.maxWaitMillis";
    public static final String POOL_MAX_TOTAL = "cache.pool.maxTotal";
    public static final String POOL_MAX_IDLE = "cache.pool.maxIdle";
    public static final String POOL_MIN_IDLE = "cache.pool.minIdle";
    public static final String POOL_TEST_ON_BORROW = "cache.pool.testOnBorrow";
    public static final String POOL_TEST_ON_RETURN = "cache.pool.testOnReturn";
    public static final String POOL_TEST_ON_IDLE = "cache.pool.testWhileIdle";
    public static final String POOL_NUM_TESTS_PER_EVICT = "cache.pool.numTestsPerEvictionRun";
    public static final String POOL_TIME_BET_EVICT_MIL = "cache.pool.timeBetweenEvictionRunsMillis";

    @Resource
    ConfigurableEnvironment environment;

    @Bean
    public JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        if (environment.containsProperty(POOL_MAX_WAIT_MIL)) {
            jedisPoolConfig.setMaxWaitMillis(Long.valueOf(environment.getProperty(POOL_MAX_WAIT_MIL)));
        }
        if (environment.containsProperty(POOL_MAX_TOTAL)) {
            jedisPoolConfig.setMaxTotal(Integer.valueOf(environment.getProperty(POOL_MAX_TOTAL)));
        }
        if (environment.containsProperty(POOL_MAX_IDLE)) {
            jedisPoolConfig.setMaxIdle(Integer.valueOf(environment.getProperty(POOL_MAX_IDLE)));
        }
        if (environment.containsProperty(POOL_MIN_IDLE)) {
            jedisPoolConfig.setMinIdle(Integer.valueOf(environment.getProperty(POOL_MIN_IDLE)));
        }
        if (environment.containsProperty(POOL_TEST_ON_BORROW)) {
            jedisPoolConfig.setTestOnBorrow(Boolean.valueOf(environment.getProperty(POOL_TEST_ON_BORROW)));
        }
        if (environment.containsProperty(POOL_TEST_ON_RETURN)) {
            jedisPoolConfig.setTestOnReturn(Boolean.valueOf(environment.getProperty(POOL_TEST_ON_RETURN)));
        }
        if (environment.containsProperty(POOL_TEST_ON_IDLE)) {
            jedisPoolConfig.setTestWhileIdle(Boolean.valueOf(environment.getProperty(POOL_TEST_ON_IDLE)));
        }
        if (environment.containsProperty(POOL_NUM_TESTS_PER_EVICT)) {
            jedisPoolConfig
                    .setNumTestsPerEvictionRun(Integer.valueOf(environment.getProperty(POOL_NUM_TESTS_PER_EVICT)));
        }
        if (environment.containsProperty(POOL_TIME_BET_EVICT_MIL)) {
            jedisPoolConfig.setTimeBetweenEvictionRunsMillis(
                    Integer.valueOf(environment.getProperty(POOL_TIME_BET_EVICT_MIL)));
        }
        return jedisPoolConfig;
    }
}
