package com.blueskykong.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class RedisCacheService {

    public static final Pattern CACHE_CLEAR_PATTERN = Pattern.compile("cache.clear.(\\w+)$");

    @Resource
    ConfigurableEnvironment environment;

    private CacheManager cacheManager;

    private RedisTemplate redisTemplate;

    private final String FLUSHDB_STARTUP = "redis.setup.flushdb";

    @Autowired
    public RedisCacheService(CacheManager cacheManager, RedisTemplate redisTemplate) {
        this.cacheManager = cacheManager;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void clearRedis() {
        boolean flushAllKeysStartup = true;
        if (environment.containsProperty(FLUSHDB_STARTUP)) {
            flushAllKeysStartup = !"FALSE".equals(environment.getProperty(FLUSHDB_STARTUP).toUpperCase());
        }

        if (flushAllKeysStartup) {
            flushDb();
        } else {
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
                    Matcher matcher = CACHE_CLEAR_PATTERN.matcher(key);
                    if (matcher.matches()) {
                        String cacheName = matcher.group(1);
                        String pattern = environment.getProperty(key);
//                        removeCache(cacheName, pattern);
                    }
                }
            }
        }
    }

    public void flushDb() {
        if (this.cacheManager instanceof RedisCacheManager) {
            redisTemplate.getConnectionFactory().getConnection().flushDb();
        }
    }

    public void saveCache(String cacheName, String key, Object value) {
        RedisCacheManager redisCacheManager = (RedisCacheManager) this.cacheManager;
        Cache cache = redisCacheManager.getCache(cacheName);
        cache.put(key, value);
    }

    public Object getCache(String cacheName, String key) {
        RedisCacheManager redisCacheManager = (RedisCacheManager) this.cacheManager;
        return redisCacheManager.getCache(cacheName).get(key).get();
    }
}
