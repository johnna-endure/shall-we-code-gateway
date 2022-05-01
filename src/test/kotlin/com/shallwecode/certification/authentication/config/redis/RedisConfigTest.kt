package com.shallwecode.certification.authentication.config.redis

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RedisConfigTest(
    @Autowired val redisConfig: RedisConfig
) {

    @Test
    fun `redisConfig 빈이 등록됐는지 확인`() {
        assertThat(redisConfig).isNotNull
    }

}