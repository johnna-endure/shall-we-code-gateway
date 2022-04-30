package com.shallwecode.certification.authentication.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.shallwecode.certificate.authentication.controller.UserAuthenticationController
import com.shallwecode.certificate.authentication.controller.request.UserAuthenticationRequest
import com.shallwecode.certificate.authentication.persistence.document.UserAuthentication
import com.shallwecode.certificate.authentication.persistence.repository.UserAuthenticationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@WebFluxTest(controllers = [UserAuthenticationController::class])
class UserAuthenticationControllerUnitTest(
    @Autowired val webTestClient: WebTestClient
) {

    @MockBean
    var repository: UserAuthenticationRepository? = null
    val jsonMapper = ObjectMapper()

    @Test
    fun beanLoadTest() {
        assertThat(webTestClient).isNotNull
        assertThat(repository).isNotNull
    }

    @Test
    fun `사용자 인증 정보 저장`() {
        // given
        val request = UserAuthenticationRequest(
            id = 1L,
            email = "cws@gmail.com",
            password = "testpassword",
            roles = listOf<String>("user"),
            createDateTime = LocalDateTime.now()
        )

        val authentication = request.toUserAuthentication()

        `when`(repository!!.save(authentication))
            .thenReturn(Mono.just(authentication))


        // when , then
        webTestClient.put()
            .uri("/authentication/user")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.message").isEqualTo("saved")
            .jsonPath("$.body.userId").isEqualTo(authentication.userId)
    }

    @Test
    fun `사용자 인증 정보 조회`() {
        // given
        val authentication = UserAuthentication(
            userId = 1L,
            email = "cws@gmail.com",
            password = "testpassword",
            roles = listOf<String>("user"),
            createDateTime = LocalDateTime.now()
        )

        `when`(repository!!.findByUserId(authentication.userId))
            .thenReturn(Mono.just(authentication))

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
            .jsonPath("$.body.createDateTime").isEqualTo(authentication.createDateTime.toString())
    }

    @Test
    fun `사용자 인증 정보 삭제`() {
        // given
        val authentication = UserAuthentication(
            userId = 1L,
            email = "cws@gmail.com",
            password = "testpassword",
            roles = listOf<String>("user"),
            createDateTime = LocalDateTime.now()
        )

        `when`(repository!!.removeById(authentication.userId))
            .thenReturn(Mono.just(authentication))

        // when , then
        webTestClient.delete()
            .uri("/authentication/users/{userId}", authentication.userId)
            .exchange()
            .expectStatus().isNoContent
            .expectBody()
            .jsonPath("$.message").isEqualTo("deleted")
            .jsonPath("$.body.userId").isEqualTo(authentication.userId)
    }


}