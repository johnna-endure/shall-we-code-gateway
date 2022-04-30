package com.shallwecode.certificate.authentication.controller.model

import java.time.LocalDateTime

data class UserAuthenticationModel(
    val userId: Long,
    val email: String,
    val roles: List<String>,
    val createDateTime: LocalDateTime
)