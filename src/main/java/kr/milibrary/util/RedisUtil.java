package kr.milibrary.util;

import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class RedisUtil<K, V> {
    @Resource(name = "redisTemplate")
    private RedisTemplate<K, V> redisTemplate;
    private ValueOperations<K, V> valueOperations;
    private ListOperations<K, V> listOperations;
    private SetOperations<K, V>setOperations;
    private ZSetOperations<K, V> zSetOperations;
    private HashOperations<K, Object, Object> hashOperations;

    @PostConstruct
    private void setField() {
        valueOperations = redisTemplate.opsForValue();
        listOperations = redisTemplate.opsForList();
        setOperations = redisTemplate.opsForSet();
        zSetOperations = redisTemplate.opsForZSet();
        hashOperations = redisTemplate.opsForHash();
    }

    public ValueOperations<K, V> getValueOperations() {
        return valueOperations;
    }

    public ListOperations<K, V> getListOperations() {
        return listOperations;
    }

    public SetOperations<K, V> getSetOperations() {
        return setOperations;
    }

    public ZSetOperations<K, V> getzSetOperations() {
        return zSetOperations;
    }

    public HashOperations<K, Object, Object> getHashOperations() {
        return hashOperations;
    }
}
