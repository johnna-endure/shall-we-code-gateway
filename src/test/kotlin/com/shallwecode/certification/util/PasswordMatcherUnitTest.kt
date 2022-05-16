package com.shallwecode.certification.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class PasswordMatcherUnitTest {

    @Test
    fun `match - 비밀번호가 일치하는 경우`() {
        // given
        val encodedPassword = BCryptPasswordEncoder().encode("password")
        val rawPassword = "password"

        // when
        val ret = PasswordMatcher.match(rawPassword, encodedPassword)

        // then
        assertThat(ret).isTrue
    }

    @Test
    fun `match - 비밀번호가 일치하지 않는 경우`() {
        // given
        val encodedPassword = BCryptPasswordEncoder().encode("diffpassword")
        val rawPassword = "password"

        // when
        val ret = PasswordMatcher.match(rawPassword, encodedPassword)

        // then
        assertThat(ret).isFalse
    }
}