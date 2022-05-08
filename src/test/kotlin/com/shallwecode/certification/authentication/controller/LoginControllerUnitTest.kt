package com.shallwecode.certification.authentication.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.shallwecode.certification.authentication.controller.request.LoginRequest
import com.shallwecode.certification.authentication.controller.response.LoginResult
import com.shallwecode.certification.authentication.exception.NotFoundDataException
import com.shallwecode.certification.authentication.jwt.JwtGenerator
import com.shallwecode.certification.authentication.persistence.document.UserAuthentication
import com.shallwecode.certification.authentication.persistence.repository.RefreshTokenRedisRepository
import com.shallwecode.certification.authentication.persistence.repository.UserAuthenticationMongoRepository
import com.shallwecode.certification.common.http.response.HttpResponse
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@WebFluxTest(controllers = [LoginController::class])
class LoginControllerUnitTest(
    @Autowired val webTestClient: WebTestClient
) {

    @MockkBean
    lateinit var authenticationMongoRepository: UserAuthenticationMongoRepository

    @MockkBean
    lateinit var jwtGenerator: JwtGenerator

    @MockkBean
    lateinit var refreshTokenRedisRepository: RefreshTokenRedisRepository

    @Test
    fun `테스트에 필요한 빈 생성 테스트`() {
        assertThat(webTestClient).isNotNull
        assertThat(authenticationMongoRepository).isNotNull
        assertThat(jwtGenerator).isNotNull
        assertThat(refreshTokenRedisRepository).isNotNull
    }

    @Test
    fun `login - 로그인에 성공`() {
        // given
        val email = "test@gmail.com"
        val password = "password"
        val request = LoginRequest(
            email = email,
            password = password
        )

        val passwordEncoder = BCryptPasswordEncoder()
        every { authenticationMongoRepository.findByEmail(email) }
            .returns(
                Mono.just(
                    UserAuthentication(
                        userId = 1L,
                        email = email,
                        password = passwordEncoder.encode("password"),
                        roles = listOf("user"),
                        createDateTime = LocalDateTime.now()
                    )
                )
            )
        every { jwtGenerator.issueAccessToken(any(), any(), any()) } returns "accessToken"
        every { jwtGenerator.issueRefreshToken(any(), any(), any()) } returns "refreshToken"
        every { refreshTokenRedisRepository.save(any(), any()) } returns Mono.just(true)

        // when, then
        val jsonMapper = ObjectMapper()
        val expectedBody = jsonMapper.writeValueAsString(HttpResponse(body = LoginResult(true)))

        webTestClient.post()
            .uri("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .json(expectedBody)
    }


    @Test
    fun `login - 해당 이메일이 존재하지 않는 경우`() {
        // given
        val email = "noexist@gmail.com"
        val password = "password"
        val request = LoginRequest(
            email = email,
            password = password
        )

        every { authenticationMongoRepository.findByEmail(email) }
            .returns(
                Mono.error(NotFoundDataException("not found"))
            )

        // when, then
        webTestClient.post()
            .uri("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isNotFound
            .expectBody()
            .jsonPath("$.message").isEqualTo("not found")
    }

    @Test
    fun `login - 패스워드가 일치하지 않는 경우`() {
        // given
        val email = "test@gmail.com"
        val password = "wrong-password"
        val request = LoginRequest(
            email = email,
            password = password
        )

        val passwordEncoder = BCryptPasswordEncoder()
        every { authenticationMongoRepository.findByEmail(email) }
            .returns(
                Mono.just(
                    UserAuthentication(
                        userId = 1L,
                        email = email,
                        password = passwordEncoder.encode("origin-password"),
                        roles = listOf("user"),
                        createDateTime = LocalDateTime.now()
                    )
                )
            )

        // when, then
        val jsonMapper = ObjectMapper()
        val expectedBody = jsonMapper.writeValueAsString(
            HttpResponse(body = LoginResult(false))
        )

        webTestClient.post()
            .uri("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .json(expectedBody)
    }

    @Test
    fun `login - 레디스 저장소에서 IllegalArgumentException 예외를 던지는 경우`() {
        // given
        val email = "test@gmail.com"
        val password = "password"
        val errorMessage = "bad request"
        val request = LoginRequest(
            email = email,
            password = password
        )

        val passwordEncoder = BCryptPasswordEncoder()
        every { authenticationMongoRepository.findByEmail(email) }
            .returns(
                Mono.just(
                    UserAuthentication(
                        userId = 1L,
                        email = email,
                        password = passwordEncoder.encode("password"),
                        roles = listOf("user"),
                        createDateTime = LocalDateTime.now()
                    )
                )
            )
        every { jwtGenerator.issueAccessToken(any(), any(), any()) } returns "accessToken"
        every { jwtGenerator.issueRefreshToken(any(), any(), any()) } returns "refreshToken"
        every { refreshTokenRedisRepository.save(any(), any()) }
            .throws(IllegalArgumentException(errorMessage))

        // when, then
        webTestClient.post()
            .uri("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isBadRequest
            .expectBody()
            .jsonPath("$.message").isEqualTo(errorMessage)
    }

    @Test
    fun `login - 레디스 저장소에서 알 수 없는 RuntimeException 예외를 던지는 경우`() {
        // given
        val email = "test@gmail.com"
        val password = "password"
        val errorMessage = "what the fuck?"
        val request = LoginRequest(
            email = email,
            password = password
        )

        val passwordEncoder = BCryptPasswordEncoder()
        every { authenticationMongoRepository.findByEmail(email) }
            .returns(
                Mono.just(
                    UserAuthentication(
                        userId = 1L,
                        email = email,
                        password = passwordEncoder.encode("password"),
                        roles = listOf("user"),
                        createDateTime = LocalDateTime.now()
                    )
                )
            )
        every { jwtGenerator.issueAccessToken(any(), any(), any()) } returns "accessToken"
        every { jwtGenerator.issueRefreshToken(any(), any(), any()) } returns "refreshToken"
        every { refreshTokenRedisRepository.save(any(), any()) }
            .throws(RuntimeException(errorMessage))

        // when, then
        webTestClient.post()
            .uri("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody()
            .jsonPath("$.message").isEqualTo(errorMessage)
    }

}