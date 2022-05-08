package com.shallwecode.certification.authentication.controller.util

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

// TODO 테스트 필요
object PasswordMatcher {
    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()
    fun match(rawPassword: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}