package com.shallwecode

import com.shallwecode.certification.config.redis.RedisConfigProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(RedisConfigProperties::class)
@SpringBootApplication
class ShallWeCodeGatewayApplication

fun main(args: Array<String>) {
    runApplication<ShallWeCodeGatewayApplication>(*args)
}
