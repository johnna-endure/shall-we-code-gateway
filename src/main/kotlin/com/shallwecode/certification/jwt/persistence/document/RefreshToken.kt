package com.shallwecode.certification.jwt.persistence.document

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

/**
 * Spring Data Redis 에 사용될 리프레시 토큰 Dpcument 클래스
 */
@RedisHash("refreshToken")
class RefreshToken(
    @Id
    val email: String,
    val refreshToken: String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RefreshToken

        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        return email.hashCode()
    }

    override fun toString(): String {
        return "RefreshToken(email=$email, refreshToken=$refreshToken)"
    }

}