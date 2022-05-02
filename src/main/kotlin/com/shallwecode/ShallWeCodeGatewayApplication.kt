package com.shallwecode

import com.shallwecode.certification.authentication.config.redis.RedisConfigProperties
import com.shallwecode.certification.jwt.config.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(value = [RedisConfigProperties::class, JwtProperties::class])
@SpringBootApplication
class ShallWeCodeGatewayApplication

fun main(args: Array<String>) {
    runApplication<ShallWeCodeGatewayApplication>(*args)
}