package com.shallwecode.certification.authentication.config.redis

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RedisConfigPropertiesUnitTest(
    @Autowired val redisConfigProperties: RedisConfigProperties
) {

    @Test
    fun `RedisConfigProperties 빈이 생성되는지 확인`() {
        assertThat(redisConfigProperties).isNotNull
    }

    @Test
    fun `yml 설정으로부터 값이 바인딩되는지 테스트`() {
        assertThat(redisConfigProperties.host).isNotNull
        assertThat(redisConfigProperties.port).isNotNull
    }
}