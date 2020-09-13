package com.seckill.enums;


/**
 * @Author Tiny_W
 * @Date 2020-03-26 13:49
 * @Version 1.0
 */
public enum SeckillEnum {


    SUCCESS(1, "秒杀成功"),
    END(0, "秒杀结束"),
    REPEAT_KILL(-1, "重复秒杀"),
    SYSTEM_ERROR(-2, "系统异常"),
    URL_ERROR(-3,"地址错误"),
    GOODS_ID_NULL(-4,"商品不存在"),;

    private int state;
    private String info;

    SeckillEnum(int state, String info) {
        this.state = state;
        this.info = info;
    }

    public int getState() {
        return state;
    }


    public String getInfo() {
        return info;
    }


    public static SeckillEnum stateOf(int index) {
        for (SeckillEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
