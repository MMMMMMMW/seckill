# 秒杀系统

## 项目地址
初步完成，还在更新中，已部署在http://47.112.146.78:9898/

## 参考
慕课网的免费SSM秒杀系统项目：https://www.imooc.com/learn/587  

## 描述
- 由于源教程是`SSM+JSP`的前后端不分离项目，鉴于`SSM+JSP`技术较为落后，本项目有所改动，采用了基于`sprinboot+vue`的前后端分离项目。  
- 由于未系统学过前端，`vue`是边百度边敲的，所以前端可能较为简陋。
- 通过`Redis+MySQL`的形式，让`Redis`负责读数据，`MySQL`负责写数据，减缓了秒杀高并发时`MySQL`的压力

## 所用技术
- springboot
- vue
- Mybatis
- Redis

## 功能
- 无登录功能，前端用Cookie保存收货地址后，直接传到后端生成订单
- 鉴于时间原因，前端无订单查询功能，但已提供了后端接口，可访问
  - http://47.112.146.78:8086/orders/list
- 提供一个随机生成秒杀商品的按钮，便于测试



