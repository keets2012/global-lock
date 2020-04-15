package com.blueskykong.common.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisHelper {

    private static JedisPool pool;
    private static RedisHelper redisHelper = null;

    /**
     * 通过静态工厂方法来沟通对象，复用对象，避免每次重新产生新对象
     */
    public static RedisHelper newInstance(String host, int port, String password, int maxIdle, int maxTotal, long maxWaitMillis) {
        if (null != redisHelper)
            return redisHelper;

        synchronized (RedisHelper.class) {
            if (null != redisHelper)
                return redisHelper;
            redisHelper = new RedisHelper(host, port, password, maxIdle, maxTotal, maxWaitMillis);
            return redisHelper;
        }
    }

    private RedisHelper(String host, int port, String password, int maxIdle, int maxTotal, long maxWaitMillis) {
        if (null != pool)
            return;

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);//最大空闲连接数
        config.setMaxTotal(maxTotal);//最大连接数
        config.setTestOnBorrow(true);
        config.setTestOnReturn(false);
        config.setMaxWaitMillis(maxWaitMillis);
        pool = new JedisPool(config, host, port, 10000, password);
    }

    /**
     * 没有特别需求，请不要在外部调用此方法
     */
    public Jedis getConnection() {
        return pool.getResource();
    }

    public void returnConnection(Jedis conn) {
        //自Jedis3.0版本后jedisPool.returnResource()遭弃用,官方重写了Jedis的close方法用以代替
        if (null != conn) {
            conn.close();
        }
    }

    public Pipeline pipeline() {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.pipelined();
        } finally {
            this.returnConnection(conn);
        }
    }

    public Set<String> keys(String pattern) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.keys(pattern);
        } finally {
            this.returnConnection(conn);
        }
    }

    public void set(String key, String value) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            conn.set(key, value);
        } finally {
            this.returnConnection(conn);
        }
    }

    public long setNx(String key, String value) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.setnx(key, value);
        } finally {
            this.returnConnection(conn);
        }
    }

    public String get(String key) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.get(key);
        } finally {
            this.returnConnection(conn);
        }
    }

    public String getSet(String key, String value) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.getSet(key, value);
        } finally {
            this.returnConnection(conn);
        }
    }

    public long del(String key) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.del(key);
        } finally {
            this.returnConnection(conn);
        }
    }

    public long del(String... keys) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.del(keys);
        } finally {
            this.returnConnection(conn);
        }
    }

    /**
     * 若 key 存在，返回 true ，否则返回 false 。
     */
    public boolean exists(String key) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.exists(key);
        } finally {
            this.returnConnection(conn);
        }
    }

    /**
     * 设置成功，返回 1, key 不存在或设置失败，返回 0
     */
    public long pexpire(String key, long milliseconds) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.pexpire(key, milliseconds);
        } finally {
            this.returnConnection(conn);
        }
    }

    public long hset(String key, String field, String value) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.hset(key, field, value);
        } finally {
            this.returnConnection(conn);
        }
    }

    /**
     * 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在。
     * 若域 field 已经存在，该操作无效。
     * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
     */
    public long hsetnx(String key, String field, String value) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.hsetnx(key, field, value);
        } finally {
            this.returnConnection(conn);
        }
    }

    public long hdel(String key, String... fields) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.hdel(key, fields);
        } finally {
            this.returnConnection(conn);
        }
    }

    public Set<String> hkeys(String key) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.hkeys(key);
        } finally {
            this.returnConnection(conn);
        }
    }

    public Map<String, String> hgetAll(String key) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.hgetAll(key);
        } finally {
            this.returnConnection(conn);
        }
    }

    public String hget(String key, String field) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.hget(key, field);
        } finally {
            this.returnConnection(conn);
        }
    }

    public long hincrBy(String key, String field, Long value) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.hincrBy(key, field, value);
        } finally {
            this.returnConnection(conn);
        }
    }

    public Long rpush(String key, String... strings) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.rpush(key, strings);
        } finally {
            this.returnConnection(conn);
        }
    }

    /**
     * 返回元素个数
     */
    public Long lpush(String key, String... strings) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.lpush(key, strings);
        } finally {
            this.returnConnection(conn);
        }
    }

    public String lpop(String key) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.lpop(key);
        } finally {
            this.returnConnection(conn);
        }
    }

    public String rpop(String key) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.rpop(key);
        } finally {
            this.returnConnection(conn);
        }
    }

    public List<String> lrange(String key, long start, long end) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.lrange(key, start, end);
        } finally {
            this.returnConnection(conn);
        }
    }

    public long sadd(String key, String... members) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.sadd(key, members);
        } finally {
            this.returnConnection(conn);
        }
    }

    public Set<String> sinter(String... keys) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.sinter(keys);
        } finally {
            this.returnConnection(conn);
        }
    }

    public long scard(String key) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.scard(key);
        } finally {
            this.returnConnection(conn);
        }
    }

    public String spop(String key) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.spop(key);
        } finally {
            this.returnConnection(conn);
        }
    }

    public long srem(String key, String... members) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.srem(key, members);
        } finally {
            this.returnConnection(conn);
        }
    }

    public Set<String> smembers(String key) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.smembers(key);
        } finally {
            this.returnConnection(conn);
        }
    }

    public Set<String> sunion(String... keys) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.sunion(keys);
        } finally {
            this.returnConnection(conn);
        }
    }

    public boolean sismember(String key, String member) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.sismember(key, member);
        } finally {
            this.returnConnection(conn);
        }
    }

    public long increment(String key, long amount) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.incrBy(key, amount);
        } finally {
            this.returnConnection(conn);
        }
    }

    public double increment(String key, double amount) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.incrByFloat(key, amount);
        } finally {
            this.returnConnection(conn);
        }
    }

    /**
     * 列表长度
     */
    public long llen(String key) {
        Jedis conn = null;
        try {
            conn = this.getConnection();
            return conn.llen(key);
        } finally {
            this.returnConnection(conn);
        }
    }
}

