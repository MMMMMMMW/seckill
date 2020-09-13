package com.seckill.controller;

import com.seckill.entity.Orders;
import com.seckill.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Tiny_W
 * @Date 2020-03-29 17:25
 * @Version 1.0
 */
@RequestMapping("/orders")
@RestController
public class OrdersController  {
    @Autowired
    private OrdersService ordersService;

    @GetMapping("/{start}/{userId}/list")
    public List<Orders> getOrdersList(@PathVariable("start") Integer start,@PathVariable("userId") Long userId){
        return ordersService.findAllByUserId(start,userId);
    }

    @GetMapping("/list")
    public List<Orders> findAll(){
        return ordersService.findAll();
    }
}
