package com.seckill.dto;

import com.seckill.entity.Orders;
import com.seckill.enums.SeckillEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Tiny_W
 * @Date 2020-03-26 14:06
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillExecution {
    private long goodsId;

    private SeckillEnum seckillEnum;

    //秒杀成功后的订单
    private Orders orders;
}
