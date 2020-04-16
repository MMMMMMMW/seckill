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

        //先在Redis中判断是否重复秒杀
        if (redisService.getRepeatButKey(goodsId,userId)) {
            return new SeckillResult<SeckillExecution>(false, null, SeckillEnum.REPEAT_KILL.getInfo());
        }

        //库存减1，在此之前会判断是否有表示秒杀结束的key
        if (redisService.decreaseGoodsNumber(goodsId) < 0){
            redisService.increaseGoodsNumber(goodsId);
            return new SeckillResult<SeckillExecution>(false, null, SeckillEnum.END.getInfo());
        }

        try{
            SeckillExecution execution = seckillService.executeSeckill(goodsId,userId,userName,userAddress,seckillService.exportSeckillUrl(goodsId).getMd5());
            //秒杀成功，将商品与用户的id作为key放入Redis中，之后便无需判断重复秒杀
            redisService.setRepeatBuyKey(goodsId,userId);
            return new SeckillResult<SeckillExecution>(true,execution,null);
        } catch (SeckillException e){
            //本来该用户就已经有该商品的订单时返回错误，此时也需要设置重复秒杀的key
            if (e.getMessage().equals(SeckillEnum.REPEAT_KILL.getInfo()))
                redisService.setRepeatBuyKey(goodsId,userId);
                //秒杀结束时设置key
            else if (e.getMessage().equals(SeckillEnum.END.getInfo()) )
                redisService.setGoodsEnd(goodsId);
            //此时秒杀失败，将Redis之前减的库存再加回去
            redisService.increaseGoodsNumber(goodsId);
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

//    @PostMapping("/test")
//    public void setRedis(@RequestBody Goods goods){
//
//    }
//
//    @GetMapping("/{id}/get")
//    public Goods getRedis(@PathVariable("id") Long id){
//        return (Goods)redisTemplate.opsForValue().get("goods");
//    }


    /**
     * 便于压力测试
     * @param goodsId
     * @param userId
     * @return
     */
//    @PostMapping("/test/{goodsId}/{userId}/execution")
//    public SeckillResult<SeckillExecution> executeSeckill_test(
//            @PathVariable("goodsId") Long goodsId,
//            @PathVariable("userId") Long userId){
//        String userAddress = "压力测试";
//        String userName = "压力测试";
//
//        //事先在Redis中判断是否重复秒杀
//        if (redisService.getRepeatButKey(goodsId,userId)) {
//            System.out.println("重复秒杀判断！！！！！！！！！！！！！！！！！");
//            return new SeckillResult<SeckillExecution>(false, null, SeckillEnum.REPEAT_KILL.getInfo());
//        }
//
//        //库存减1，在此之前会判断是否有表示秒杀结束的key
//        if (redisService.decreaseGoodsNumber(goodsId) < 0){
//            redisService.increaseGoodsNumber(goodsId);
//            return new SeckillResult<SeckillExecution>(false, null, SeckillEnum.END.getInfo());
//        }
//
//        try{
//            SeckillExecution execution = seckillService.executeSeckill(goodsId,userId,userName,userAddress,seckillService.exportSeckillUrl(goodsId).getMd5());
//            //秒杀成功，将商品与用户的id作为key放入Redis中，之后便无需判断重复秒杀
//            redisService.setRepeatBuyKey(goodsId,userId);
//            return new SeckillResult<SeckillExecution>(true,execution,null);
//        } catch (SeckillException e){
//            //本来该用户就已经有该商品的订单时返回错误，此时也需要设置重复秒杀的key
//            if (e.getMessage().equals(SeckillEnum.REPEAT_KILL.getInfo()))
//                redisService.setRepeatBuyKey(goodsId,userId);
//            //秒杀结束时设置key
//            else if (e.getMessage().equals(SeckillEnum.END.getInfo()) )
//                redisService.setGoodsEnd(goodsId);
//            //此时秒杀失败，将Redis之前减的库存再加回去
//            redisService.increaseGoodsNumber(goodsId);
//            return new SeckillResult<SeckillExecution>(false,null,e.getMessage());
//        } catch (Exception e){
//            e.printStackTrace();
//            return new SeckillResult<SeckillExecution>(false,null, SeckillEnum.SYSTEM_ERROR.getInfo());
//        }
//    }
}
