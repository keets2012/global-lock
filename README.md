# redis-utils

## single redis util

### import
you only need to import the following config.

```java
@Configuration
@Import(RedisCacheConfig.class)
public class RedisConfig {
}
```

### usage
#### config
```yaml
cache:
  core
  pool:
    maxIdle: 15
    maxTotal: 30
    maxWaitMillis: 6000
    minIdle: 1
    numTestsPerEvictionRun: 10
    testOnBorrow: true
    testOnReturn: true
    testWhileIdle: true
    timeBetweenEvictionRunsMillis: 6000
  # enable redis
  redis:
    enabled: true
# if flush all the cache when start up
redis:
  database: 0
  host:
    address: ${REDIS_ADDRESS:127.0.0.1}
    password: ${REDIS_PWD:user}
    port: ${REDIS_PORT:7000}
  setup:
    flushdb: true
```

#### redisTemplate && @Cacheable

```java
@Autowired
RedisTemplate redisTemplate;
```
`redisTemplate` has many operations for redis, you can have a try.

#### distributed redis lock
```java
 @Autowired
 RedisLockConfig redisLockConfig;


 public void test() {
    RedisLock redisLock = redisLockConfig.newLock("mutex");
    try {
        redisLock.lock(acquireTimeout,timeUnit);
        //do sth
    } catch (InterruptedException e) {
         logger.error("failed to acquire redis lock {} !", refreshTokenValue);
    } finally {
        redisLock.unlock();
    }
 }
```

Also, you can use tryLock() and isLock().


## redisCluster usage
### import
you only need to import the following config.

```java
@Configuration
@Import(JedisClusterConfig.class)
public class RedisConfig {
}
```

### config

```yaml
redis:
  cluster:
    enabled: true
    password: pwd
    timeout: 2000
    max-redirects: 8
    nodes: 127.0.0.1:8000,127.0.0.1:8001
```

### others is the same with single, since the operations already have  benn wrapped.
