package com.shallwecode.certificate.authentication.persistence

import com.shallwecode.certificate.authentication.persistence.document.UserAuthentication
import com.shallwecode.certificate.authentication.persistence.repository.UserAuthenticationRepository
import com.shallwecode.certificate.config.mongo.MultipleDatabaseMongoConfig
import com.shallwecode.certificate.config.mongo.template.TestMongoTemplateWrapper
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
    fun `upsert - 사용자 인증 정보 저장하기(기존 유저 정보가 없는 경우)`() {
        //given
        val authentication = UserAuthentication(
            userId = 2L,
            email = "cws@gmail.com",
            password = "testpassword",
            roles = listOf<String>("user"),
            createDateTime = LocalDateTime.now()
        )
        //when
        val ret = userAuthenticationRepository.upsert(authentication)

        //then
        StepVerifier.create(ret)
            .assertNext {
                assertThat(it.upsertedId!!.asNumber().longValue()).isEqualTo(authentication.userId)
                assertThat(it.matchedCount).isEqualTo(0)
                assertThat(it.modifiedCount).isEqualTo(0)
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun `upsert - 사용자 인증 정보 저장하기(기존 유저 정보가 있는 경우)`() {
        //given
        val authentication = UserAuthentication(
            userId = 2L,
            email = "cws@gmail.com",
            password = "testpassword",
            roles = listOf<String>("user"),
            createDateTime = LocalDateTime.now()
        )
        mongoTemplate.save(authentication).subscribe()

        //when
        val newAuthentication = UserAuthentication(
            userId = 2L,
            email = "cws@gmail.com",
            password = "newpassword",
            roles = listOf<String>("user"),
            createDateTime = LocalDateTime.now()
        )

        val ret = userAuthenticationRepository.upsert(newAuthentication)

        //then
        StepVerifier.create(ret)
            .assertNext {
                assertThat(it.upsertedId).isNull()
                assertThat(it.matchedCount).isEqualTo(1)
                assertThat(it.modifiedCount).isEqualTo(1)
                assertThat(it.wasAcknowledged()).isTrue()
            }
            .expectComplete()
            .verify()
    }


    @Test
    fun `save - 사용자 인증 정보 저장하기`() {
        //given
        val authentication = UserAuthentication(
            userId = 2L,
            email = "cws@gmail.com",
            password = "testpassword",
            roles = listOf<String>("user"),
            createDateTime = LocalDateTime.now()
        )
        //when
        val ret = userAuthenticationRepository.save(authentication)

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
    fun `findByUserId - 사용자 인증 정보 단건 조회`() {
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
    fun `findByEmail - 이메일로 사용자 인증 정보 단건 조회`() {
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
        userAuthenticationRepository.findByEmail(authentication.email)
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
    fun `removeById - 사용자 인증 정보 단건 삭제`() {

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