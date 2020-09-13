package com.seckill.service;

import com.seckill.entity.Orders;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Tiny_W
 * @Date 2020-03-26 13:29
 * @Version 1.0
 */
@Service
public interface OrdersService {

    List<Orders> findAllByUserId(Integer start,Long userId);

    List<Orders> findAll();
}
