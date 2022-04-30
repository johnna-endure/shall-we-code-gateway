package com.shallwecode.certification.authentication.persistence

import com.shallwecode.certificate.authentication.persistence.document.UserAuthentication
import com.shallwecode.certificate.authentication.persistence.repository.UserAuthenticationRepository
import com.shallwecode.config.mongo.MultipleDatabaseMongoConfig
import com.shallwecode.config.mongo.template.TestMongoTemplateWrapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.core.query.Query
import reactor.kotlin.test.test
import reactor.test.StepVerifier
import java.time.LocalDateTime

@DataMongoTest
@Import(MultipleDatabaseMongoConfig::class)
class UserAuthenticationRepositoryUnitTest(
    @Autowired val testMongoTemplateWrapper: TestMongoTemplateWrapper
) {
    private val userAuthenticationRepository = UserAuthenticationRepository(testMongoTemplateWrapper)
    private val mongoTemplate = userAuthenticationRepository.template

    @BeforeEach
    fun beforeEach() {
        mongoTemplate.remove(Query(), "userAuthentication")
            .subscribe()
    }

    @AfterEach
    fun afterEach() {
        mongoTemplate.remove(Query(), "userAuthentication")
            .subscribe()
    }


    @Test
    fun repositoryRLoadTest() {
        assertThat(mongoTemplate).isNotNull
    }

    @Test
    fun `사용자 인증 정보 저장하기`() {
        //given
        val authentication = UserAuthentication(
            userId = 2L,
            email = "cws@gmail.com",
            password = "testpassword",
            roles = listOf<String>("user"),
            createDateTime = LocalDateTime.now()
        )
        //when
        val ret = mongoTemplate.save(authentication)

        //then
        StepVerifier.create(ret)
            .assertNext {
                assertThat(it.userId).isEqualTo(authentication.userId)
                assertThat(it.email).isEqualTo(authentication.email)
                assertThat(it.password).isEqualTo(authentication.password)
                assertThat(it.roles).isEqualTo(authentication.roles)
                assertThat(it.createDateTime).isEqualTo(authentication.createDateTime)
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun `사용자 인증 정보 단건 조회`() {
        //given
        val authentication = UserAuthentication(
            userId = 1L,
            email = "cws@gmail.com",
            password = "testpassword",
            roles = listOf<String>("user"),
            createDateTime = LocalDateTime.now()
        )
        mongoTemplate.save(authentication).subscribe()

        //when, then
        userAuthenticationRepository.findByUserId(authentication.userId)
            .test()
            .assertNext {
                assertThat(it.userId).isEqualTo(authentication.userId)
                assertThat(it.email).isEqualTo(authentication.email)
                assertThat(it.password).isEqualTo(authentication.password)
                assertThat(it.roles).isEqualTo(authentication.roles)
                assertThat(it.createDateTime.isEqual(authentication.createDateTime))
            }
            .verifyComplete()
    }

    @Test
    fun `사용자 인증 정보 단건 삭제`() {

        //given
        val authentication = UserAuthentication(
            userId = 1L,
            email = "cws@gmail.com",
            password = "testpassword",
            roles = listOf<String>("user"),
            createDateTime = LocalDateTime.now()
        )
        mongoTemplate.save(authentication).subscribe()

        //when, then
        userAuthenticationRepository.removeById(authentication.userId)
            .test()
            .assertNext {
                assertThat(it.userId).isEqualTo(authentication.userId)
                assertThat(it.email).isEqualTo(authentication.email)
                assertThat(it.password).isEqualTo(authentication.password)
                assertThat(it.roles).isEqualTo(authentication.roles)
                assertThat(it.createDateTime.isEqual(authentication.createDateTime))
            }
            .verifyComplete()

    }
}