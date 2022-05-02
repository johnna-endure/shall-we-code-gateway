package com.shallwecode.certification.jwt.document

import com.shallwecode.certification.jwt.persistence.document.RefreshToken
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RefreshTokenUnitTest {

    @Test
    fun `RefreshToken 객체 생성 테스트`() {
        val email = "test@gmail.com"
        val token = "testToken"
        val requestToken = RefreshToken(
            email = email,
            refreshToken = token
        )

        assertThat(requestToken.email).isEqualTo(email)
        assertThat(requestToken.refreshToken).isEqualTo(token)
    }

}