package com.seckill.service;

import com.seckill.dto.SeckillUrl;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Goods;
import com.seckill.exception.SeckillException;

import java.util.List;

/**
 * @Author Tiny_W
 * @Date 2020-03-26 13:54
 * @Version 1.0
 */
public interface SeckillService {

    /**
     * 在秒杀开启时输出秒杀接口的地址，否则回到主页
     * @param goodsId
     */
    SeckillUrl exportSeckillUrl(long goodsId);

    /**
     * 执行秒杀操作，失败时抛出我们异常并回滚数据库
     * @param goodsId
     * @param userId
     * @param md5
     * @return
     */
    SeckillExecution executeSeckill(long goodsId, long userId,String userName,String userAddress, String md5)
            throws SeckillException;


}
