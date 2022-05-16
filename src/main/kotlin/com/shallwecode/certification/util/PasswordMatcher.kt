package com.shallwecode.certification.util

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

object PasswordMatcher {
    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    fun match(rawPassword: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}