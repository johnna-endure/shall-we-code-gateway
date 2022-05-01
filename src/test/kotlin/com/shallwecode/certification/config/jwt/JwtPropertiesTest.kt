package com.shallwecode.certification.config.jwt

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JwtPropertiesTest(
    @Autowired val jwtProperties: JwtProperties
) {

    @Test
    fun `jwtProperties 빈이 생성됐는지 확인`() {
        assertThat(jwtProperties).isNotNull
    }

    @Test
    fun `issuer, secret 값이 바인딩됐는지 확인`() {
        assertThat(jwtProperties.issuer).isNotNull
        assertThat(jwtProperties.secret).isNotNull
    }

}