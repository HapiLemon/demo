## 背景

在 CMS 遇到的一个场景是处理大量轨迹事件时，会出现**缓存穿透(Hotspot Invalid)**。当时处理的办法是引入一个锁的依赖，实现单服务内对资源加锁。<br>
因此存在缺陷，多服务节点的时候还是会出现少量的并行查询进入数据库。<br>
大佬说可以直接用 Redis 锁，不需要引入外部依赖

## 体验

Redis 锁的原理可以简化为：原本由内存存储的锁标记移到了 Redis 里，多个服务节点使用的是同一个 Redis，以此来实现跨服务节点的锁<br>
实现 Redis 锁有多重方式，市面上已经存在一个缓存框架 Redisson 就提供了基于 Redis 的分布式锁的实现，当然也可以手搓。

### Redisson

在我看来 Redisson 也就是功能更强大的 Jedis、Lettuce<br>
因此在这个 demo 中先体验了一下 SpringBoot 框架集成 Redisson<br>

#### 集成至 SpringBoot 框架

在容器加载阶段提供一个`CacheManager`Bean 就可以使用`@Cacheable`等一系列注解

```java

@Configuration
public class RedissonConfiguration {

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        return new RedissonSpringCacheManager(redissonClient, "classPath:/cache-ttl.json");
    }

}
```

#### 体验 Redis 锁

此 demo 内仅仅是体验了一番 Redisson 提供的 Redis 锁<br>
代码执行的过程为

1. 获取锁
2. 执行动作
3. 释放锁

### 手搓

使用 lua 脚本实现获取锁和释放锁<br>
执行过程同上文

#### 获取锁

```lua
local result = redis.call('SET', KEYS[1], ARGV[1], 'NX', 'PX', '10000')
if(result == false) then 
    return 'FAIL' 
end 
if(result.ok == 'OK') then 
    return 'SUCCESS'
end
```

此处有坑，参考[对OK的判断](https://panzhongxian.cn/cn/2020/11/redis-lua-script-problems/)

#### 释放锁

```lua
local result = redis.call('get', KEYS[1]) 
if(result == ARGV[1]) then 
    redis.call('del', KEYS[1])
    return 'SUCCESS'
end
if(result == false) then
    return 'FAIL'
end
```

## 参考资料

* [Redis实现分布式锁](https://www.51cto.com/article/703155.html)
* [SpringBoot集成Redisson](https://github.com/redisson/redisson/tree/master/redisson-spring-boot-starter#spring-boot-starter)
* [Spring Cache集成Redisson](https://github.com/redisson/redisson/wiki/14.-%E7%AC%AC%E4%B8%89%E6%96%B9%E6%A1%86%E6%9E%B6%E6%95%B4%E5%90%88#142-spring-cache%E6%95%B4%E5%90%88)
* [Redisson锁的使用](https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8)
* [Redisson客户端执行lua脚本](https://github.com/redisson/redisson/wiki/10.-%E9%A2%9D%E5%A4%96%E5%8A%9F%E8%83%BD#106-%E8%84%9A%E6%9C%AC%E6%89%A7%E8%A1%8C)

## 待深入探索

1. Redisson 在 Redis 中的存储结构
2. Redisson 各个锁的区别
3. 多级缓存