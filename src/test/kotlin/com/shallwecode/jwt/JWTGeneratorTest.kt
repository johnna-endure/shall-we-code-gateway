package com.shallwecode.jwt

import com.shallwecode.certificate.jwt.JWTGenerator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [JWTGenerator::class])
class JWTGeneratorTest(@Autowired var jwtGenerator: JWTGenerator) {

    @Test
    fun beanLoadTest() {
        assertThat(jwtGenerator).isNotNull
    }

    @Test
    fun `토큰 생성 성공`() {
        //given
        val userId = 1L
        val role = arrayOf("user", "admin")

        //when
        val token = jwtGenerator.generateToken(userId, role)

        //then
        assertThat(token).isNotNull
    }

}