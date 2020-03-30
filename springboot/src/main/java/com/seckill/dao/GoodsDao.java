package com.seckill.dao;

import com.seckill.entity.Goods;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author Tiny_W
 * @Date 2020-03-26 0:34
 * @Version 1.0
 */
@Mapper
public interface GoodsDao {

    @Select("select * from goods limit #{start},#{length}")
    List<Goods> findAll(@Param("start") int start,@Param("length") int length);

    @Select("select * from goods where id = #{id}")
    Goods findById(long id);

    @Insert("insert into " +
            "goods(name,price,number, start_time, end_time) " +
            "values(#{name},#{price},#{number},#{startTime},#{endTime})")
    int addGoods(Goods goods);

    @Update("update goods " +
            "set name = #{name} " +
            ", price = #{price}" +
            ", number = #{number}" +
            ", start_time = #{startTime}" +
            ", end_time = #{endTime}" +
            "where id = #{id}")
    int updateGoods(Goods goods);

    @Update("update goods set number = number-1 " +
            "where id = #{id} " +
            "and start_time <= #{createTime}" +
            "and end_time >= #{createTime}" +
            "and number > 0")
    int reduceNumber(@Param("id") long id,@Param("createTime") Date createTime);

    @Select("select count(*) from goods")
    int findCount();
}
