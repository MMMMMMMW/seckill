package com.seckill.service;

import com.seckill.entity.Goods;

import java.util.List;

/**
 * @Author Tiny_W
 * @Date 2020-04-05 15:40
 * @Version 1.0
 */
public interface RedisService {

    List<Goods> getGoodsAll(int start,int length);

    List<Goods> setGoodsAll(int start,int length);
}
