package com.shallwecode.certification.jwt

import com.shallwecode.certification.jwt.config.JwtProperties
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

//@SpringBootTest(classes = [JWTGenerator::class, JwtProperties::class])
class JwtGeneratorUnitTest {
    lateinit var jwtGenerator: JwtGenerator
    lateinit var jwtProperties: JwtProperties

    @BeforeEach
    fun beforeEach() {
        jwtProperties = JwtProperties()
        jwtProperties.secret = "testSecret"
        jwtProperties.issuer = "shallwecode"
        jwtProperties.expireDurationDay = "7"

        jwtGenerator = JwtGenerator(jwtProperties)
    }


    @Test
    fun `jwtGenerator 객체 생성 테스트`() {
        assertThat(jwtGenerator).isNotNull
    }

    @Test
    fun `액세스 토큰 생성 성공`() {
        // given
        val userId = 1L
        val userPassword = "testpassword"
        val userRoles = arrayOf("user")

        // when
        val token = jwtGenerator.issueAccessToken(userId, userPassword, userRoles)

        // then
        assertThat(token).isNotNull
    }

    @Test
    fun `리프레시 토큰 생성 성공`() {
        // given
        val userId = 1L
        val userPassword = "testpassword"
        val userRoles = arrayOf("user")

        // when
        val token = jwtGenerator.issueRefreshToken(userId, userPassword, userRoles)

        // then
        assertThat(token).isNotNull
    }
}