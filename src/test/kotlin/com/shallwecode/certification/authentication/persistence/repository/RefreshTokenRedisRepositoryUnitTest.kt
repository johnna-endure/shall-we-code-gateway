package com.shallwecode.certification.authentication.persistence.repository

import com.shallwecode.certification.jwt.config.JwtProperties
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ReactiveValueOperations
import org.springframework.data.redis.core.ScanOptions
import reactor.test.StepVerifier

/**
 * 이 클래스의 테스트를 위해서 테스트용 레디스를 켜놔야한다.
 * TODO TestContainer 로 이 부분을 자동화하던가 대안이 필요하다.
 */
@DataRedisTest
class RefreshTokenRedisRepositoryUnitTest(
    @Autowired val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>
) {
    val reactiveRedisValueOperations: ReactiveValueOperations<String, String> = reactiveRedisTemplate.opsForValue()
    lateinit var refreshTokenRedisRepository: RefreshTokenRedisRepository

    @BeforeEach
    fun `RefreshTokenRedisRepository 객체 초기화`() {
        val jwtProperties = JwtProperties()
        jwtProperties.issuer = "shallwecode"
        jwtProperties.secret = "secret"
        jwtProperties.expireDurationDay = "3"
        refreshTokenRedisRepository = RefreshTokenRedisRepository(reactiveRedisTemplate, jwtProperties)

        deleteAll()
    }

    @AfterEach
    fun deleteAll() {
        val scanOption = ScanOptions.scanOptions().count(10).match("*").build()
        reactiveRedisTemplate.scan(scanOption)
            .flatMap {
                reactiveRedisTemplate.opsForValue().delete(it)
            }
            .subscribe()
    }

    @Test
    fun `RefreshTokenRedisRepository 빈 생성 테스트`() {
        assertThat(reactiveRedisTemplate).isNotNull
        assertThat(reactiveRedisValueOperations).isNotNull
        assertThat(refreshTokenRedisRepository).isNotNull
    }

    @Test
    fun `save - 리프레시 토큰을 저장합니다`() {
        // given
        val email = "test@gmail.com"
        val refreshToken = "testToken"

        // when
        refreshTokenRedisRepository.save(email, refreshToken).subscribe()

        // then
        StepVerifier.create(reactiveRedisValueOperations.get(email))
            .assertNext {
                assertThat(it).isEqualTo(refreshToken)
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun `save - email 이 빈 문자열 인수로 주어지는 경우`() {
        // given
        val email = ""
        val refreshToken = "testToken"

        // when, then
        assertThrows<IllegalArgumentException> { refreshTokenRedisRepository.save(email, refreshToken) }
    }

    @Test
    fun `save - refreshToken 이 빈 문자열 인수로 주어지는 경우`() {
        // given
        val email = "test@gmail.com"
        val refreshToken = ""

        // when, then
        assertThrows<IllegalArgumentException> { refreshTokenRedisRepository.save(email, refreshToken) }
    }

}