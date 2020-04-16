package com.seckill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Tiny_W
 * @Date 2020-03-26 14:10
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillResult<T> {

    //请求是否成功
    private boolean success;
    private T data;
    private String error;

}
