package com.shallwecode.certification.authentication.persistence.repository

import com.shallwecode.certification.jwt.config.JwtProperties
import com.shallwecode.certification.jwt.config.getExpireDurationSeconds
import org.apache.logging.log4j.util.Strings.isBlank
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ReactiveValueOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.Duration

@Repository
class RefreshTokenRedisRepository(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>,
    private val jwtProperties: JwtProperties
) {
    val reactiveValueOperations: ReactiveValueOperations<String, String> = reactiveRedisTemplate.opsForValue()
    fun save(
        email: String,
        refreshToken: String,
        expireDurationSeconds: Long = jwtProperties.getExpireDurationSeconds()
    ): Mono<Boolean> {
        require(!isBlank(email))
        require(!isBlank(refreshToken))

        return reactiveValueOperations.set(email, refreshToken, Duration.ofSeconds(expireDurationSeconds))
    }

}