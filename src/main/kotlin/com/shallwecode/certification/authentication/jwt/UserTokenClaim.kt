package com.shallwecode.certification.authentication.jwt

data class UserTokenClaim(
    val userId: Long,
    val roles: List<String>
)