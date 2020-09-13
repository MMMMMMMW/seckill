package com.seckill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 秒杀的地址
 * @Author Tiny_W
 * @Date 2020-03-26 13:56
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillUrl {
    private boolean exposed;
    private String md5;
    private long goodsId;
    //时间均转化为用毫秒
    private long nowTime;
    private long startTime;
    private long endTime;
}
