package com.seckill.service;

import com.seckill.entity.Goods;

import java.util.List;

/**
 * @Author Tiny_W
 * @Date 2020-04-05 15:40
 * @Version 1.0
 */
public interface RedisService {


    /**
     * 商品列表缓存到Redis中
     * @param start
     * @param length
     * @return
     */
    List<Goods> getGoodsAll(int start,int length);

    List<Goods> setGoodsAll(int start,int length);

    /**
     * 购买成功后，设置key在Redis中作为该用户购买该商品的标志
     * 此后该用户再次购买同一商品后就可以直接返回而无需进入MySQL了
     * @param goodsId
     * @param userId
     */
    void setRepeatBuyKey(Long goodsId,Long userId);

    boolean getRepeatButKey(Long goodsId,Long userId);

    /**
     * 秒杀时，先去Redis中减库存，若库存不足，则直接返回秒杀结束
     * @param goodsId
     */
    void setGoodsNumber(Long goodsId);

    int decreaseGoodsNumber(Long goodsId);

    //秒杀失败时将redis中的库存加回去
    void increaseGoodsNumber(Long goodsId);

    /**
     * 当库存为0或时间结束时，设置一个key表示秒杀结束
     * @param goodsId
     */
    void setGoodsEnd(Long goodsId);

    boolean getGoodsEnd(Long goodsId);
}
