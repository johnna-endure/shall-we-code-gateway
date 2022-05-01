package com.shallwecode.certification.jwt

import com.shallwecode.certification.jwt.config.JwtProperties
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

//@SpringBootTest(classes = [JWTGenerator::class, JwtProperties::class])
class JwtGeneratorTest {
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
    fun `토큰 생성 성공`() {
        // given
        val userId = 1L
        val userPassword = "testpassword"
        val userSecret = "${userId}${userPassword}".hashCode()
        val userRoles = arrayOf("user")

        // when
        val token = jwtGenerator.issueToken(userId, userRoles, userSecret)

        // then
        assertThat(token).isNotNull
    }


}