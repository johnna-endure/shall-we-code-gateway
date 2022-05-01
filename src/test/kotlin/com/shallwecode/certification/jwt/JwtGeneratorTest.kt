package com.shallwecode.certification.jwt

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

//@SpringBootTest(classes = [JWTGenerator::class, JwtProperties::class])
class JwtGeneratorTest(
    val jwtGenerator: JwtGenerator
) {


    @Test
    fun beanLoadTest() {
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

    }


}