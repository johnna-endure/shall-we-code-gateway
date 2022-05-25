package com.shallwecode.certification.authentication.controller

import com.mongodb.client.result.UpdateResult
import com.ninjasquad.springmockk.MockkBean
import com.shallwecode.certification.authentication.controller.request.UserAuthenticationSaveRequest
import com.shallwecode.certification.authentication.exception.CreateDataException
import com.shallwecode.certification.authentication.exception.DeleteDataException
import com.shallwecode.certification.authentication.exception.NotFoundDataException
import com.shallwecode.certification.authentication.persistence.document.UserAuthentication
import com.shallwecode.certification.authentication.persistence.repository.UserAuthenticationMongoRepository
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.bson.BsonInt64
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Mono
import java.time.LocalDateTime.now
import java.time.LocalDateTime.parse

@WebFluxTest(controllers = [UserAuthenticationInfoController::class])
class UserAuthenticationInfoControllerUnitTest(
    @Autowired val webTestClient: WebTestClient
) {

    @MockkBean
    lateinit var repository: UserAuthenticationMongoRepository

    @Test
    fun beanLoadTest() {
        assertThat(webTestClient).isNotNull
        assertThat(repository).isNotNull
    }

    @Test
    fun `saveUserAuthentication - 사용자 인증 정보 저장 성공`() {
        // given
        val request = UserAuthenticationSaveRequest(
            userId = 1L,
            email = "cws@gmail.com",
            password = "testpassword",
            roles = listOf<String>("user"),
            createDateTime = now()
        )

        every { repository.upsert(any()) }
            .returns(
                Mono.just(
                    UpdateResult.acknowledged(0, 0, BsonInt64(1L))
                )
            )

        // when , then
        webTestClient.put()
            .uri("/authentication/user")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.message").isEqualTo("saved")
    }


    @Test
    fun `saveUserAuthentication - 사용자 인증 정보 저장 실패`() {
        // given
        val request = UserAuthenticationSaveRequest(
            userId = 1L,
            email = "cws@gmail.com",
            password = "testpassword",
            roles = listOf<String>("user"),
            createDateTime = now()
        )
        val errorMessage = "데이터 저장에 실패했습니다."

        every { repository.upsert(any()) }
            .returns(Mono.error(CreateDataException(errorMessage)))

        // when , then
        webTestClient.put()
            .uri("/authentication/user")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody()
            .jsonPath("$.message").isEqualTo(errorMessage)
    }

    @Test
    fun `getUserAuthentication - 사용자 인증 정보 조회 성공`() {
        // given
        val authentication = UserAuthentication(
            userId = 1L,
            email = "cws@gmail.com",
            password = "testpassword",
            roles = listOf<String>("user"),
            createDateTime = now()
        )

        every { repository.findByUserId(any()) } returns Mono.just(authentication)

        // when , then
        webTestClient.get()
            .uri("/authentication/users/{userId}", authentication.userId)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.message").isEqualTo("found")
            .jsonPath("$.body.userId").isEqualTo(authentication.userId)
            .jsonPath("$.body.email").isEqualTo(authentication.email)
            .jsonPath("$.body.roles[0]").isEqualTo(authentication.roles[0])
            .jsonPath("$.body.createDateTime").value<String> {
                parse(it).isEqual(authentication.createDateTime)
            }
    }

    @Test
    fun `getUserAuthentication - 사용자 인증 정보 조회 실패`() {
        // given
        val errorMessage = "test error"

        every { repository.findByUserId(any()) } returns Mono.error(NotFoundDataException(errorMessage))

        // when , then
        webTestClient.get()
            .uri("/authentication/users/{userId}", 1L)
            .exchange()
            .expectStatus().isNotFound
            .expectBody()
            .jsonPath("$.message").isEqualTo(errorMessage)
    }

    @Test
    fun `deleteUserAuthentication - 사용자 인증 정보 삭제 성공`() {
        // given
        val authentication = UserAuthentication(
            userId = 1L,
            email = "cws@gmail.com",
            password = "testpassword",
            roles = listOf<String>("user"),
            createDateTime = now()
        )

        every { repository.removeById(any()) } returns Mono.just(authentication)

        // when , then
        webTestClient.delete()
            .uri("/authentication/users/{userId}", authentication.userId)
            .exchange()
            .expectStatus().isNoContent
            .expectBody()
            .jsonPath("$.message").isEqualTo("deleted")
            .jsonPath("$.body.userId").isEqualTo(authentication.userId)
    }

    @Test
    fun `deleteUserAuthentication - 사용자 인증 정보 삭제 실패`() {
        // given
        val errorMessage = "test error"
        every { repository!!.removeById(any()) } returns Mono.error(DeleteDataException(errorMessage))

        // when , then
        webTestClient.delete()
            .uri("/authentication/users/{userId}", 1L)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody()
            .jsonPath("$.message").isEqualTo(errorMessage)
    }
}