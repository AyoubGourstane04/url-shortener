package com.ayoub.url_shortener.config;

import com.ayoub.url_shortener.dto.ShortenResponseDTO;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisTemplate<String, ShortenResponseDTO> getRedisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, ShortenResponseDTO> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());


        JacksonJsonRedisSerializer<ShortenResponseDTO> serializer = new JacksonJsonRedisSerializer<>(ShortenResponseDTO.class);


        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory factory){
        RedisCacheConfiguration baseConfig = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> ttlConfig = Map.of(
                "urls", baseConfig.entryTtl(Duration.ofMinutes(10)),
                "redirects", baseConfig.entryTtl(Duration.ofMinutes(60))
        );

        Map<String, RedisCacheConfiguration> serializerConfig = Map.of(
                "urls", baseConfig.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new JacksonJsonRedisSerializer<>(ShortenResponseDTO.class))),
                "redirects", baseConfig.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        );



        return RedisCacheManager.builder(factory)
                .cacheDefaults(baseConfig.entryTtl(Duration.ofMinutes(10)))
                .withInitialCacheConfigurations(ttlConfig)
                .withInitialCacheConfigurations(serializerConfig)
                .build();
    }



}
