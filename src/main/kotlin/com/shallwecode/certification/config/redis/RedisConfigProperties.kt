package com.shallwecode.certification.config.redis

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "redis.config")
class RedisConfigProperties {
    lateinit var host: String;
    lateinit var port: String;
}
