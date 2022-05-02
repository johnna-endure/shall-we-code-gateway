package com.shallwecode.certification.authentication.controller.request

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LoginRequestsUnitTest {

    @Test
    fun `LoginRequest 생성 테스트`() {
        val email = "test@gmail.com"
        val password = "testpassword"

        val request = LoginRequest(
            email = email,
            password = password
        )

        assertThat(request.email).isEqualTo(email)
        assertThat(request.password).isEqualTo(password)
    }

}