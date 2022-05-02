package com.shallwecode.certification.config.redis

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext

@SpringBootTest
class RedisConfigUnitTest(
    @Autowired val applicationContext: ApplicationContext,
    @Autowired val redisConfig: RedisConfig
) {

    @Test
    fun `applicationContext, RedisConfig 초기화 테스트`() {
        assertThat(applicationContext).isNotNull
        assertThat(redisConfig).isNotNull
        assertThat(redisConfig.redisConfigProperties).isNotNull
    }

    @Test
    fun `bean 생성 테스트`() {
        val connectionFactory = applicationContext.getBean("connectionFactory")
        val reactiveRedisTemplate = applicationContext.getBean("reactiveRedisTemplate")

        assertThat(connectionFactory).isNotNull
        assertThat(reactiveRedisTemplate).isNotNull
    }
}