package com.shallwecode.certification.authentication.config.redis

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "redis.config")
class RedisConfigProperties {
    lateinit var host: String;
    lateinit var port: String;
}
