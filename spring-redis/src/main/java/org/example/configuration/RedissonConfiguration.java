package org.example.configuration;

import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author keminfeng
 */
@Configuration
public class RedissonConfiguration {

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        // 替换默认的CacheManager 并提供过期时间
        return new RedissonSpringCacheManager(redissonClient, "classPath:/cache-ttl.json");
    }

}
