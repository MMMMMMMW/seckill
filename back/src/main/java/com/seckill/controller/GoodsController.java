package com.seckill.controller;

import com.seckill.entity.Goods;
import com.seckill.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author Tiny_W
 * @Date 2020-03-29 17:40
 * @Version 1.0
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    private final String name[] = {"Macbook pro 13","Macbook pro 14","Magicbook pro ","小米6","联想"};

    @Autowired
    private GoodsService goodsService;

    /**
     * 随机添加秒杀商品
     * @return
     */
    @GetMapping("/add")
    public String addGoodsRandom(){

        for(int i = 0;i < 10;i++){
            Calendar beforeTime = Calendar.getInstance();
            beforeTime.add(Calendar.MINUTE, i);
            Date startTime = beforeTime.getTime();
            Calendar afterTime = Calendar.getInstance();
            afterTime.add(Calendar.MINUTE, i*10);
            Date endTime = afterTime.getTime();
            Goods goods = new Goods(null,name[(int)(Math.random()*4)],(int)(Math.random()*2000),i,startTime,endTime);
            goodsService.addGoods(goods);
        }
        for(int i = 0;i < 2;i++){
            Calendar beforeTime = Calendar.getInstance();
            beforeTime.add(Calendar.MINUTE, -i);
            Date startTime = beforeTime.getTime();
            Calendar afterTime = Calendar.getInstance();
            afterTime.add(Calendar.MINUTE, i*10000);
            Date endTime = afterTime.getTime();
            Goods goods = new Goods(null,name[(int)(Math.random()*4)],(int)(Math.random()*2000),20,startTime,endTime);
            goodsService.addGoods(goods);
        }
        return "success";
    }
}
