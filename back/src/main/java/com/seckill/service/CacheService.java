package com.seckill.service;

/**
 * 本地热点数据缓存，作为一级缓存
 * 先查找本地缓存，再查找Redis缓存，最后查找MySQL
 * @Author Tiny_W
 * @Date 2020-04-20 12:05
 * @Version 1.0
 */
public interface CacheService {

    void setCommonCache(String key,Object value);

    Object getCommonCache(String key);
}
