package com.shallwecode.certification.authentication.controller.request

data class LoginRequest(
    val email: String,
    val password: String
)