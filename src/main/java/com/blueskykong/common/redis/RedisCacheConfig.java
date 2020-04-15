package com.blueskykong.common.redis;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by keets on 2017/8/4.
 */

@Configuration
//@ConditionalOnProperty(value = "redis.cluster.enabled")
@EnableCaching(mode = AdviceMode.ASPECTJ)
@Import(PoolConfigs.class)
public class RedisCacheConfig extends CachingConfigurerSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheConfig.class);

    public static final String REDIS_HOST_ADD = "redis.host.address";
    public static final String REDIS_HOST_PORT = "redis.host.port";
    public static final String REDIS_HOST_PWD = "redis.host.password";
    public static final String REDIS_DATABASE = "redis.database";

    public static final String POOL_MAX_WAIT_MIL = "cache.pool.maxWaitMillis";
    public static final String POOL_MAX_TOTAL = "cache.pool.maxTotal";
    public static final String POOL_MAX_IDLE = "cache.pool.maxIdle";

    public static final Pattern CACHE_TIMEOUT_PATTERN = Pattern.compile("cache.timeout.(\\w+)$");

    @Resource
    ConfigurableEnvironment environment;

    @Bean
    public RedisHelper redisHelper() {
        return RedisHelper.newInstance(environment.getProperty(REDIS_HOST_ADD), Integer.valueOf(environment.getProperty(REDIS_HOST_PORT)),
                environment.getProperty(REDIS_HOST_PWD), Integer.valueOf(environment.getProperty(POOL_MAX_IDLE)), Integer.valueOf(environment.getProperty(POOL_MAX_TOTAL)),
                Long.valueOf(environment.getProperty(POOL_MAX_WAIT_MIL)));
    }

    @Bean
    public RedisLockConfig redisLockConfig() {
        return RedisLockConfig.getInstance(redisHelper());
    }



    @Bean
    public JedisConnectionFactory getJedisConnectionFactory(JedisPoolConfig jedisPoolConfig,
                                                            @Value("${cache.redis.enabled}") boolean enableCache) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
        if (enableCache) {
            jedisConnectionFactory.setHostName(environment.getProperty(REDIS_HOST_ADD));
            jedisConnectionFactory.setPort(Integer.valueOf(environment.getProperty(REDIS_HOST_PORT)));
            jedisConnectionFactory.setPassword(environment.getProperty(REDIS_HOST_PWD));
            jedisConnectionFactory.setUsePool(true);
            jedisConnectionFactory.setDatabase(Integer.valueOf(environment.getProperty(REDIS_DATABASE)));
        }
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(cf);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean
    @Cacheable
    public CacheManager getRedisCacheManager(RedisTemplate redisTemplate,
                                             @Value("${cache.redis.enabled}") boolean enableCache) {
        CacheManager cacheManager;
        if (enableCache) {
            RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
            redisCacheManager.setUsePrefix(true);
            HashMap<String, Long> timeoutMap = new HashMap<>();

            Map<String, Object> propertyMap = new HashMap<>();
            for (Iterator it = environment.getPropertySources().iterator(); it.hasNext(); ) {
                Object propertySource = it.next();
                if (propertySource instanceof PropertiesPropertySource) {
                    propertyMap.putAll(((MapPropertySource) propertySource).getSource());
                }
            }

            for (Object objKey : propertyMap.keySet()) {
                if (objKey instanceof String) {
                    String key = (String) objKey;
                    Matcher matcher = CACHE_TIMEOUT_PATTERN.matcher(key);
                    if (matcher.matches()) {
                        String cacheName = matcher.group(1);
                        Long timeout = Long.valueOf(environment.getProperty(key));
                        timeoutMap.put(cacheName, timeout);
                    }
                }
            }
            redisCacheManager.setExpires(timeoutMap);
            cacheManager = redisCacheManager;
        } else {
            return new NoOpCacheManager();
        }
        return cacheManager;
    }

    @Bean
    RedisCacheService redisCacheService(CacheManager cacheManager, RedisTemplate redisTemplate) {
        return new RedisCacheService(cacheManager, redisTemplate);
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                LOGGER.error("Cache get error", exception);
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                LOGGER.error("Cache put error", exception);
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                LOGGER.error("Cache evict error", exception);
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                LOGGER.error("Cache clear error", exception);
            }
        };
    }
}
