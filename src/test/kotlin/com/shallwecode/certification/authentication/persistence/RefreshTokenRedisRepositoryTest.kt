package com.shallwecode.certification.authentication.persistence

import com.shallwecode.certification.authentication.persistence.repository.RefreshTokenRedisRepository
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest

@DataRedisTest
class RefreshTokenRedisRepositoryTest(
    val refreshTokenRedisRepository: RefreshTokenRedisRepository
) {

    @Test
    fun `리프레시 토큰을 저장합니다`() {
        assert(false)
    }

}