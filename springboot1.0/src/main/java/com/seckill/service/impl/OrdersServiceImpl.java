package com.seckill.service.impl;

import com.seckill.dao.OrdersDao;
import com.seckill.entity.Orders;
import com.seckill.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Tiny_W
 * @Date 2020-03-26 13:34
 * @Version 1.0
 */
@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersDao ordersDao;

    @Override
    public List<Orders> findAllByUserId(Integer start, Long userId) {
        return ordersDao.findAllByUserId(start,userId);
    }

    @Override
    public List<Orders> findAll(){
        return ordersDao.findAll();
    }
}

