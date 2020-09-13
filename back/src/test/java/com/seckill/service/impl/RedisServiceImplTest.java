package com.seckill.service.impl;

import com.seckill.dao.GoodsDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author Tiny_W
 * @Date 2020-04-16 22:18
 * @Version 1.0
 */
@SpringBootTest
class RedisServiceImplTest {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GoodsDao goodsDao;

    @Test
    void getGoodsAll() {
        redisTemplate.opsForValue().set("test",1);
    }
}