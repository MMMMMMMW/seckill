package com.seckill.dao;

import com.seckill.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author Tiny_W
 * @Date 2020-03-26 0:48
 * @Version 1.0
 */
@Mapper
public interface OrdersDao {

    //ignore用于防止自动抛出异常，我们自己通过返回值判断是否成功
    @Insert("insert ignore into orders(goods_id,user_id,user_name,user_address,state,create_time) " +
            "values (#{goodsId},#{userId},#{userName},#{userAddress},#{state},#{createTime})")
    int addOrders(Orders orders);

    @Select("select * from orders where user_id = #{userId} limit #{start},10")
    List<Orders> findAllByUserId(@Param("start") int start,@Param("userId") Long userId);

    @Select("select * from orders")
    List<Orders> findAll();

}
