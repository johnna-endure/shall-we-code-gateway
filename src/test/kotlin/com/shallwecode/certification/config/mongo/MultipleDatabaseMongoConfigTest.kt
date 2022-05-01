package com.shallwecode.certification.config.mongo

import com.shallwecode.certification.config.mongo.template.ShallWeCodeMongoTemplateWrapper
import com.shallwecode.certification.config.mongo.template.TestMongoTemplateWrapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.kotlin.test.test

@SpringBootTest
class MultipleDatabaseMongoConfigTest(
    @Autowired
    val shallWeCodeMongoTemplateWrapper: ShallWeCodeMongoTemplateWrapper,
    @Autowired
    val testMongoTemplateWrapper: TestMongoTemplateWrapper,
) {

    @Test
    fun beanLoadTest() {
        assertThat(shallWeCodeMongoTemplateWrapper).isNotNull
        assertThat(testMongoTemplateWrapper).isNotNull
    }

    @Test
    fun `데이터베이스 검증 테스트`() {
        shallWeCodeMongoTemplateWrapper.template.mongoDatabase
            .test()
            .assertNext {
                assertThat(it.name).isEqualTo("shallwecode")
            }
            .verifyComplete()


        testMongoTemplateWrapper.template.mongoDatabase
            .test()
            .assertNext {
                assertThat(it.name).isEqualTo("test")
            }
            .verifyComplete()

    }
}