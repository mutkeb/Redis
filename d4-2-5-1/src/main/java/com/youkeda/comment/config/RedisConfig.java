package com.youkeda.comment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public RedisTemplate redisTemplateInit(){
        //  设置序列化key的工具
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //  设置序列化value的工具
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //  设置hash的key
        redisTemplate.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        //  设置hash的value
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
