package com.shallwecode.certificate.authentication.controller.request

import com.shallwecode.certificate.authentication.persistence.document.UserAuthentication
import java.time.LocalDateTime

data class UserAuthenticationRequest(
    val userId: Long,
    val email: String,
    val password: String,
    val roles: List<String>,
    val createDateTime: LocalDateTime
) {
    fun toUserAuthentication(): UserAuthentication {
        return UserAuthentication(
            userId = userId,
            email = email,
            password = password,
            roles = roles,
            createDateTime = createDateTime
        )
    }
}