package com.seckill.service.impl;

import com.seckill.dao.GoodsDao;
import com.seckill.dao.OrdersDao;
import com.seckill.dto.SeckillExecution;
import com.seckill.dto.SeckillUrl;
import com.seckill.entity.Goods;
import com.seckill.entity.Orders;
import com.seckill.enums.SeckillEnum;
import com.seckill.exception.SeckillException;
import com.seckill.service.SeckillService;
import org.apache.ibatis.session.SqlSessionException;
import org.junit.jupiter.api.Order;
import org.mockito.internal.matchers.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;


/**
 * @Author Tiny_W
 * @Date 2020-03-26 14:15
 * @Version 1.0
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //随意给出以一个salt值，用于md5加密，为防止被他人破解，salt越乱越好
    private final String salt = "asd23^&&$&#^#sdf@#$@#OOfg-=~asadds~__%$#^ASFA$@..!$#@";

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private OrdersDao ordersDao;

    /**
     * 只有时间到时，才能获取秒杀地址的MD5
     * 防止有人在时间没到时，就通过后台接口提前秒杀
     * 虽然SQL语句保证了时间到达前无法秒杀，但如果没设置MD5，就能提前无限提交秒杀订单，导致时间到达的瞬间就被秒杀掉
     * @param goodsId
     * @return
     */
    @Override
    public SeckillUrl exportSeckillUrl(long goodsId)  {
        Goods goods = goodsDao.findById(goodsId);

        if (goods == null) {
            logger.error(SeckillEnum.GOODS_ID_NULL.getInfo());
            throw new SeckillException(SeckillEnum.GOODS_ID_NULL.getInfo());
        }
        long startTime = goods.getStartTime().getTime();
        long endTime = goods.getEndTime().getTime();
        long nowTime = new Date().getTime();

        if (startTime > nowTime || endTime < nowTime)
            return new SeckillUrl(false, null, goodsId, nowTime, startTime, endTime);

        return new SeckillUrl(true, getMD5(goodsId), goodsId, nowTime, startTime, endTime);
    }


    /**
     * 秒杀流程：
     * 0、所有检查都在SQL语句中，插入/修改成功则表示无问题，返回被影响的行数（正整数）
     * 1、检查秒杀地址是否错误
     * 2、检查同一用户是否重复秒杀
     * 3、检查库存或秒杀时间是否合适
     * 4、前面无问题则COMMIT事务，否则通过@Transactional注解回滚
     *
     * @param goodsId
     * @param userId
     * @param md5
     * @return
     * @throws SeckillException
     */
    @Override
    @Transactional
    public SeckillExecution executeSeckill(long goodsId, long userId,String useName,String userAddress, String md5)
            throws SeckillException {
        if (md5 == null || !md5.equals(getMD5(goodsId)))
            throw new SeckillException(SeckillEnum.URL_ERROR.getInfo());

        Date nowTime = new Date();
        Orders orders = new Orders(null, goodsId, userId, useName,userAddress,(short) 1, nowTime);

        try {
            //已设置了商品id与用户id的唯一约束，当无法插入时，显然重复秒杀了,失败时自动抛出 DuplicateKeyException
            int addOrders = ordersDao.addOrders(orders);
            if (addOrders <= 0)
                throw new SeckillException(SeckillEnum.REPEAT_KILL.getInfo());

            //减少当前商品的库存，失败时显然库存已为0或时间结束，失败时返回的是受影响的行数
            int reduceGoods = goodsDao.reduceNumber(goodsId, nowTime);
            if (reduceGoods <= 0)
                throw new SeckillException(SeckillEnum.END.getInfo());

            return new SeckillExecution(goodsId, SeckillEnum.SUCCESS, orders);
        } catch (SeckillException e) {//秒杀结束、重复秒杀或地址错误
            logger.warn(e.getMessage());
            throw e;
        } catch (Exception e) {//其他未知错误，统一为系统异常
            logger.error("系统异常：" + e.getMessage());
            throw new SeckillException(SeckillEnum.SYSTEM_ERROR.getInfo());
        }
    }


    private String getMD5(long id) {
        String base = id + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
