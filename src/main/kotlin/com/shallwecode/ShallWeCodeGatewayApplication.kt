package com.shallwecode

import com.shallwecode.certification.config.jwt.JwtProperties
import com.shallwecode.certification.config.redis.RedisConfigProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(value = [RedisConfigProperties::class, JwtProperties::class])
@SpringBootApplication
class ShallWeCodeGatewayApplication

fun main(args: Array<String>) {
    runApplication<ShallWeCodeGatewayApplication>(*args)
}