package com.seckill.exception;

/**
 * 商品异常
 * @Author Tiny_W
 * @Date 2020-03-26 12:52
 * @Version 1.0
 */
public class GoodsException extends RuntimeException {
    public GoodsException(String message) {
        super(message);
    }

    public GoodsException(String message, Throwable cause) {
        super(message, cause);
    }
}
