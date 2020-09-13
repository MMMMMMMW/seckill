# 秒杀系统

## 项目地址
初步完成，还在更新中，已部署在http://47.112.146.78:9898/

## 相关技术
- Springboot
- Vue
- Mybatis
- Redis
- MySQL
- Nginx

## 参考资料
慕课网的免费SSM秒杀系统项目：https://www.imooc.com/learn/587  

## 具体描述
### 1.0版
#### 1、前后端分离
- 由于参考的是`SSM+JSP`的前后端不分离项目，鉴于`SSM+JSP`技术较为落后，本项目有所改动，使用了`Springboot+Vue`，即前后端分离，后端返回`JSON`数据，而前端使用后台的接口获取相关的`JSON`数据，并展示在前端页面中。
- 由于未系统学过前端和`vue`，前端方面是边百度边敲的，所以前端可能较为简陋。
#### 2、避免超卖、重复卖、超时卖
- 为避免重复卖（一个用户多次秒杀）、超卖（库存不足仍生成订单）、超时卖（不在秒杀时间内却卖出）等情况，秒杀时用了`@Transactional`注解，即生成订单和减少库存时使用了事务，且使用行级的加锁，任意情况出问题时进行回滚。

#### 3、MD5接口加密
- 为了避免在秒杀前就获取后端接口，提前重复多次进行秒杀操作，使得秒杀开始的瞬间成功秒杀，秒杀接口采用了MD5加密。
- 只有在秒杀时间内，才能获取MD5加密的值，从而获取秒杀的接口，进行秒杀操作

#### 4、无需登录
- 无需登录和注册，用户提供手机号、姓名和收货地址便可直接下单，前端用`Cookie`保存收货地址后，直接传到后端生成订单。

#### 5、其他功能
- 鉴于时间原因，前端无实现订单查询功能，但已提供了后端接口，可访问：http://47.112.146.78:8086/orders/list
- 提供了一个随机生成秒杀商品的按钮，便于测试。
#### jmeter压力测试
![jmeter](https://github.com/MMMMMMMW/seckill/blob/master/image/seckill1.0.png?raw=true)

由于没有对高并发时的写数据进行优化，此时并发量只有400/s


### 2.0版
- 由于1.0版只用了Redis进行数据的读取，只缓解了MySQL的读取压力，对秒杀时写数据毫无帮助，所以需要进行优化。

#### 1、库存优化
- 1.0版本中，即使库存不足，仍然会经过`MySQL`进行减库存操作，导致`MySQL`负载过大。
- 这里可以先用`Redis`减库存操作，当库存不足时，就无需经过`MySQL`了，且秒杀结束后可以设置一个`key = end:商品Id`，库存不足后直接判断该`key`即可。

#### 2、重复秒杀优化
- 当一个用户秒杀成功或`MySQL`返回重复秒杀的结果后，设置一个`key`= `success:商品Id:用户Id`到`Redis`中，生存时间10分钟，这样，同一用户下次秒杀同一商品时，假如存在该`key`，则直接返回`重复秒杀`即可。

#### 3、热点数据本地一级缓存
- 虽然`Redis`较快，但终究是设置在其他服务器中，网络会有延迟，所以设置了本地缓存作为一级缓存。  
- 由于本地缓存在JVM中，所有容量较少，使用了`google.guava`，基于LRU，专门用来存储热点数据。  
- 我们首先访问本地一级缓存，如果不存在，再访问Redis二级缓存，还不存在才访问MySQL。

#### 4、缓存穿透
- 原因：恶意用大量不存在于数据库中的非法秒杀ID进行秒杀，导致透过缓存直接访问`MySQL`，让`MySQL`短时间内访问激增。
- 解决方法：
  -  对非法ID进行提前检测，不合法ID直接返回，不访问缓存和`MySQL`。
  -  每次用非法ID访问后，将非法ID存入`Redis`中，其`value`为`null`，并设置生存时间。

#### 5、缓存雪崩
- 原因：缓存中大量`key`失效，使得让`MySQL`短时间内访问激增。
- 解决方法：
  - `key`的生存时间设置随机，防止多个`key`同时失效
  - 多级缓存，例如本地缓存作为一级缓存，`Redis`作为二级缓存等，多级缓存的`key`同时失效的概率相对而言较低。
  - 如果是因为缓存宕机，例如`Redis`，则提前设置可持久化`RDB`和`AOF`，保证`Redis`重启后马上恢复之前的数据，而不是清空

#### 6、读写分离
由于通常情况下，读操作数量远多于写操作数量，所以可以采用主从架构进行读写分离，例如`Redis`中就有集群，其中一个为`master`负责写，另外的则为`slave`负责读，`master`的数据时刻复制到`slave`。

#### 7、Jmeter压力测试
![jmeter](https://github.com/MMMMMMMW/seckill/blob/master/image/seckill2.0.png?raw=true)

经过2.0版本的优化，秒杀系统的吞吐量从400/s直接升到了2000/s，提升到了5倍


### 3.0版
理论上的版本，还在学习，未实现，目前仅根据所学的知识和理解，记录一下理论操作。

#### Nginx

##### 1、反向代理

- 用户先访问代理服务器，再由代理服务器访问正在执行操作的服务器
- 与正向代理的区别：正向代理是用户自己操作的，例如用户自己设置vpn等，而反向代理是服务端设置的，跟用户无关

##### 2、负载均衡

- 与反向代理一同使用

- 假设代理服务器连接五台相同功能的服务器，用户访问10次，那么就均衡各个服务器的访问次数，即每台服务器访问2次
- 分配策略：
  - 轮询（默认策略）：请求按时间顺序分配，服务器宕机自动剔除
  - 优先级weight：设置服务器权重，适合服务器性能不均情况
  - ip_hash：请求按ip的hash分配，每个用户固定访问一个服务器，解决session问题
  - fair（第三方）：按后端服务器的响应时间分配

##### 3、动静分离

- 静态资源：html、css、js、image等长时间不更新的静态资源
- 动态资源：jsp等随时更新的资源
- 为什么静态资源和动态资源分开请求？
  - 静态资源长时间不变，可以设置过期时间，下次访问时直接通过浏览器确认是否过期，若没过期就不用访问了
  - 也可以在发送请求后，看服务器中的静态资源最后更新时间是否与当前缓存的相同，相同则表示未更新，返回304，否则重新下载，返回200
  - 静态资源可以批量防止在离用户相近的服务器中，使用CDN，提高用户的访问速度

##### 4、高可用

- 当只有一个`nginx`时，万一`nginx`宕机了呢？
- 使用多台`nginx`服务器，例如一台主服务器，一台备份服务器，当主服务器宕机时，自动切换到备份服务器中。
- 由于多台`nginx`服务器的ip都不同，所以用户访问的是虚拟ip，切换到其他`nginx`时，同时将虚拟ip绑定到该`nginx`服务器中，这部分通常使用`keepalived`来完成。

##### 5、原理

- 一个`master`进程，多个`worker`进程
- `master`负责管理监控，`worker`负责反向代理和负载均衡
- 用户请求先发送给`master`，再由`worker`进行争抢
- 为什么用一个`master`，多个`worker`？
  - 便于`nignx -s reload` 热部署
  - 多个`worker`独立，一个出问题，不影响服务

#### MQ

##### 1、异步下单

- 2.0版本中，秒杀是同步进行的，即用户进行秒杀后，等待`MySQL`数据库插入订单和减库存后，再返回相关数据作为结果，显然，高并发时压力过大。
- 通过`MQ`，用户秒杀时，将秒杀的相关数据插入消息队列中，然后返回一个表示等待的状态给用户，用户此时看到一个等待的置顶窗口，不能进行操作。
- 插入`Redis`中一个`key`表示用户正在等待，此后用户每隔一段时间访问一遍，如果`key`消失，表示`MySQL`已操作结束，用户访问`MySQL`数据库，看是否有相关订单，如果生成订单，则表示用户秒杀成功，否则表示秒杀失败，返回结果。
##### 2、限流削峰

- 2.0版本中，多个用户同时秒杀，即使通过`Redis`限流，但高并发时仍然会导致很多秒杀操作突破的`Redis`，导致`MySQL`访问量过大。
- 采用消息队列后，无需将所有秒杀操作都放进队列中，例如设置消息队列的最大数量为`1000`，那么消息队列放不下的操作就直接返回`人数过多，请稍后重试`，使得即使再高峰的时刻，也能限制`MySQL`的访问。





### 遇到的一些坑
- Java中的内容序列化存储在Redis中后，字符前面会加上"/x1"之类的内容，使得对Redis中整数Value加减操作会出现错误，因为例如传入Value为1，在Redis中显示的可能就是"/x2/x11"这样的非整数。

