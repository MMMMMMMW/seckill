package com.seckill.service.impl;

import com.seckill.dao.GoodsDao;
import com.seckill.entity.Goods;
import com.seckill.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Tiny_W
 * @Date 2020-03-26 10:54
 * @Version 1.0
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public List<Goods> getGoodsList(int start,int length) {
        return goodsDao.findAll(start,length);
    }

    @Override
    public Goods getGoodsById(long goodsId) {
        return goodsDao.findById(goodsId);
    }

    @Override
    public int addGoods(Goods goods) {
        return goodsDao.addGoods(goods);
    }

    @Override
    public int updateGoods(Goods goods) {
        return goodsDao.updateGoods(goods);
    }

    @Override
    public int getGoodsNumber() {
        return goodsDao.findCount();
    }


}
