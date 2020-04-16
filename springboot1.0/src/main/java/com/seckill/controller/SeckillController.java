package com.seckill.controller;

import com.seckill.dto.SeckillExecution;
import com.seckill.dto.SeckillResult;
import com.seckill.dto.SeckillUrl;
import com.seckill.entity.Goods;
import com.seckill.enums.SeckillEnum;
import com.seckill.exception.SeckillException;
import com.seckill.service.GoodsService;
import com.seckill.service.RedisService;
import com.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author Tiny_W
 * @Date 2020-03-26 16:36
 * @Version 1.0
 */
@RestController
@RequestMapping("/seckill")
public class SeckillController {



    @Autowired
    private SeckillService seckillService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @GetMapping("/{start}/list")
    public List<Goods> getGoodsList(@PathVariable("start") int start){
        return redisService.getGoodsAll(start,10);
    }

    @GetMapping("/{goodsId}/detail")
    public Goods getGoodsDetail(@PathVariable("goodsId") Long goodsId){
        return goodsService.getGoodsById(goodsId);
    }

    //获取
    @GetMapping("/{goodsId}/exposer")
    public SeckillResult<SeckillUrl> getSeckillUrl(@PathVariable("goodsId") Long goodsId){
        SeckillResult<SeckillUrl> result;

        try{
            SeckillUrl seckillUrl = seckillService.exportSeckillUrl(goodsId);
            result = new SeckillResult<>(true,seckillUrl,null);
        }catch (Exception e){
            result = new SeckillResult<>(false,null,e.getMessage());
        }

        return result;
    }

    @PostMapping("/{goodsId}/{md5}/execution")
    public SeckillResult<SeckillExecution> executeSeckill(
            @PathVariable("goodsId") Long goodsId,
            @PathVariable("md5") String md5,
            @CookieValue(value = "userId",required = false) Long userId,
            @CookieValue(value = "userName",required = false) String userName,
            @CookieValue(value = "userAddress",required = false) String userAddress){

        //开发后端时，不太清除cookie为空时，值是什么，就直接把可能出现的情况全加上去了
        if (userId == null || userName == null || userAddress == null
                || userName.equals("") || userId.equals("") || userAddress.equals("")
            || userAddress.equals("null") || userId.equals("null") || userName.equals("null")
                || userAddress.equals("NULL") || userId.equals("NULL") || userName.equals("NULL")){
            return new SeckillResult<SeckillExecution>(false,null,"收货信息未填写完整");
        }

        try{
            SeckillExecution execution = seckillService.executeSeckill(goodsId,userId,userName,userAddress,md5);
            return new SeckillResult<SeckillExecution>(true,execution,null);
        } catch (SeckillException e){
            return new SeckillResult<SeckillExecution>(false,null,e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            return new SeckillResult<SeckillExecution>(false,null, SeckillEnum.SYSTEM_ERROR.getInfo());
        }
    }

    //获取当前时间
    @GetMapping("/time/now")
    public SeckillResult<String> getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date =  sdf.format(new Date());
        return new SeckillResult<>(true,date,null);
    }

    @GetMapping("/goods/number")
    public int getGoodsNumber(){
        return goodsService.getGoodsNumber();
    }

//    @PostMapping("/set")
//    public void setRedis(@RequestBody Goods goods){
//        redisTemplate.opsForValue().set("goods",goods);
//    }
//
//    @GetMapping("/{id}/get")
//    public Goods getRedis(@PathVariable("id") Long id){
//        return (Goods)redisTemplate.opsForValue().get("goods");
//    }
}
