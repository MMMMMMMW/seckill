package com.seckill.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.seckill.service.CacheService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @Author Tiny_W
 * @Date 2020-04-20 12:12
 * @Version 1.0
 */
@Service
public class CacheServiceImpl implements CacheService {

    private Cache<String,Object> commonCache = null;


    @PostConstruct
    public void init(){
        commonCache = CacheBuilder.newBuilder()
                //缓存初始容量
                .initialCapacity(10)
                //缓存最大可存储key，超过100按LRU策略
                .maximumSize(100)
                //缓存生存时间
                .expireAfterWrite(60, TimeUnit.SECONDS).build();
    }

    @Override
    public void setCommonCache(String key, Object value) {
        commonCache.put(key,value);
    }

    @Override
    public Object getCommonCache(String key) {
//        return null;
        return commonCache.getIfPresent(key);
    }
}
