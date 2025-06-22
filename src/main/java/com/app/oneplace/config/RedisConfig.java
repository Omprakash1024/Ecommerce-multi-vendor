package com.app.oneplace.config;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.cache.RedisCacheManager;

@Configuration
public class RedisConfig {

        // ✅ Custom ObjectMapper Bean with Type Info
        @Bean
        public ObjectMapper objectMapper() {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new Jdk8Module());
                mapper.registerModule(new JavaTimeModule());
                mapper.activateDefaultTyping(
                                LaissezFaireSubTypeValidator.instance,
                                ObjectMapper.DefaultTyping.NON_FINAL,
                                JsonTypeInfo.As.PROPERTY);
                return mapper;
        }

        // ✅ RedisTemplate Bean using custom ObjectMapper
        @Bean
        public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory,
                        ObjectMapper objectMapper) {
                RedisTemplate<String, Object> template = new RedisTemplate<>();
                template.setConnectionFactory(connectionFactory);

                GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

                template.setKeySerializer(new StringRedisSerializer());
                template.setValueSerializer(serializer);
                template.setHashKeySerializer(new StringRedisSerializer());
                template.setHashValueSerializer(serializer);

                template.afterPropertiesSet();
                return template;
        }

        // ✅ CacheManager Bean using same ObjectMapper
        @Bean
        public CacheManager cacheManager(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
                GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

                RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(30))
                                .disableCachingNullValues()
                                .serializeKeysWith(RedisSerializationContext.SerializationPair
                                                .fromSerializer(new StringRedisSerializer()))
                                .serializeValuesWith(
                                                RedisSerializationContext.SerializationPair.fromSerializer(serializer));

                return RedisCacheManager.builder(connectionFactory)
                                .cacheDefaults(config)
                                .build();
        }
}
