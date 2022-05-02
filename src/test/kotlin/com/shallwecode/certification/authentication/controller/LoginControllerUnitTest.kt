package com.shallwecode.certification.authentication.controller

import com.ninjasquad.springmockk.MockkBean
import com.shallwecode.certification.authentication.controller.request.LoginRequest
import com.shallwecode.certification.authentication.persistence.document.UserAuthentication
import com.shallwecode.certification.authentication.persistence.repository.UserAuthenticationMongoRepository
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@WebFluxTest(controllers = [LoginController::class])
class LoginControllerUnitTest(
    @Autowired val webTestClient: WebTestClient
) {

    @MockkBean
    lateinit var repository: UserAuthenticationMongoRepository

    @Test
    fun `테스트에 필요한 빈 생성 테스트`() {
        assertThat(webTestClient).isNotNull
        assertThat(repository).isNotNull
    }

    @Test
    fun `login - 로그인에 성공`() {
        // given
        val email = "test@gmail.com"
        val password = "testpassword"
        val request = LoginRequest(
            email = email,
            password = password
        )

        every { repository.findByEmail(email) }
            .returns(
                Mono.just(
                    UserAuthentication(
                        userId = 1L,
                        email = email,
                        password = password,
                        roles = listOf("user"),
                        createDateTime = LocalDateTime.now()
                    )
                )
            )

        // when, then
        webTestClient.post()
            .uri("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isOk
            .expectBody()
    }

}