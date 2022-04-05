package com.shallwecode.certificate.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.shallwecode.certificate.jwt.exception.JWTCreateException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@Component
class JWTGenerator(
    @Value("\${jwt.issuer}") var issuer: String? = null,
    @Value("\${jwt.secret}") var secret: String? = null
) {

    fun generateToken(userId: Long, role: Array<String>): String {
        this.issuer ?: throw JWTCreateException("issuer 정보가 없습니다.")
        this.secret ?: throw JWTCreateException("secret 정보가 없습니다.")

        val issuedAt = Date.from(
            LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )

        val expiredAt = Date.from(
            LocalDate.now().plusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )
        val algorithm = Algorithm.HMAC256(secret)

        return JWT.create()
            .withIssuer(issuer)
            .withExpiresAt(expiredAt)
            .withIssuedAt(issuedAt)
            .withClaim("userId", userId)
            .withArrayClaim("role", role)
            .sign(algorithm)
    }
}