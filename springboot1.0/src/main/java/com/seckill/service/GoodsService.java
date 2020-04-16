package com.seckill.service;

import com.seckill.dao.GoodsDao;
import com.seckill.entity.Goods;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author Tiny_W
 * @Date 2020-03-26 10:40
 * @Version 1.0
 */
public interface GoodsService {

    /**
     * 按页获取秒杀物品
     * @param start
     * @param length
     * @return
     */
    List<Goods> getGoodsList(int start,int length);

    /**
     * 按id查找物品
     * @param goodsId
     * @return
     */
    Goods getGoodsById(long goodsId);

    /**
     * 添加秒杀物品，由于订单与商品id绑定，所以不提供删除功能
     * @param goods
     * @return
     */
    int addGoods(Goods goods);

    /**
     * 修改秒杀物品
     * @param goods
     * @return
     */
    int updateGoods(Goods goods);

    int getGoodsNumber();
}
