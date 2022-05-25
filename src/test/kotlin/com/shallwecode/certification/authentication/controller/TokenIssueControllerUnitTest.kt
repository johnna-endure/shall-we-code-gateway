package com.shallwecode.certification.authentication.controller

import com.ninjasquad.springmockk.MockkBean
import com.shallwecode.certification.authentication.persistence.repository.RefreshTokenRedisRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(controllers = [TokenIssueController::class])
class TokenIssueControllerUnitTest(
    @Autowired val webTestClient: WebTestClient
) {

    @MockkBean
    lateinit var repository: RefreshTokenRedisRepository

    @Test
    fun `TokenIssueController 빈 로드 테스트`() {
        assertThat(repository).isNotNull
        assertThat(webTestClient).isNotNull
    }

//     TODO 리프레시 토큰 발급 기능 테스트 추가 필요
}