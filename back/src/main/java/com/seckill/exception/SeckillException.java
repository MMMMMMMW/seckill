package com.seckill.exception;

/**
 * 秒杀异常，抛出处理
 * 注意：@Transactional必须继承RuntimeException后才会回滚
 * @Author Tiny_W
 * @Date 2020-03-26 12:10
 * @Version 1.0
 */
public class SeckillException extends RuntimeException {
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
