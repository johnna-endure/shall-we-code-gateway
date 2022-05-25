package com.shallwecode.certification.config.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import java.lang.Integer.parseInt


@Configuration
class RedisConfig(val redisConfigProperties: RedisConfigProperties) {

    @Primary
    @Bean
    fun connectionFactory(): ReactiveRedisConnectionFactory {
        return LettuceConnectionFactory(redisConfigProperties.host, parseInt(redisConfigProperties.port))
    }

    @Bean
    fun reactiveRedisTemplate(connectionFactory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, String> {
        return ReactiveRedisTemplate(
            connectionFactory,
            RedisSerializationContext.string()
        )
    }
}