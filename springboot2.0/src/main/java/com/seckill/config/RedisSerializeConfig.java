package com.seckill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Redis存储序列化的字符后，前面会有例如"/x1"的字符，用这个来去掉这些字符
 * @Author Tiny_W
 * @Date 2020-04-16 22:25
 * @Version 1.0
 */
@Component
public class RedisSerializeConfig {

    @Autowired
    protected RedisTemplate redisTemplate;

    @PostConstruct
    private void init() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
    }

}