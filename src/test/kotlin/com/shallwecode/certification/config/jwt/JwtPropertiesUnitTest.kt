package com.shallwecode.certification.config.jwt

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JwtPropertiesUnitTest(
    @Autowired val jwtProperties: JwtProperties
) {

    @Test
    fun `jwtProperties 빈이 생성됐는지 확인`() {
        assertThat(jwtProperties).isNotNull
    }

    @Test
    fun `issuer, secret, expireDurationDay 값이 바인딩됐는지 확인`() {
        assertThat(jwtProperties.issuer).isNotNull
        assertThat(jwtProperties.secret).isNotNull
        assertThat(jwtProperties.expireDurationDay).isNotNull
    }

    @Test
    fun `null인 issuer 에 접근할 경우, JwtPropertyInitializeException 발생`() {
        // given
        jwtProperties.issuer = null

        // then
        assertThrows<JwtPropertyInitializeException> { jwtProperties.issuer }
    }

    @Test
    fun `null인 secret 에 접근할 경우, JwtPropertyInitializeException 발생`() {
        // given
        jwtProperties.secret = null

        // then
        assertThrows<JwtPropertyInitializeException> { jwtProperties.secret }
    }

    @Test
    fun `null인 expireDurationDay 에 접근할 경우, JwtPropertyInitializeException 발생`() {
        // given
        jwtProperties.expireDurationDay = null

        // then
        assertThrows<JwtPropertyInitializeException> { jwtProperties.expireDurationDay }
    }

    @Test
    fun `getExpireDurationSeconds - 기간(day)을 초로 변환`() {

        // given
        jwtProperties.expireDurationDay = "2"
        val expectedSeconds: Long = 2 * (60 * 60 * 24)

        //then
        assertThat(jwtProperties.getExpireDurationSeconds()).isEqualTo(expectedSeconds)

    }
}