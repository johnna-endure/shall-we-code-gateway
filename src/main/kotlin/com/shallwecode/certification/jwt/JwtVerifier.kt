package com.shallwecode.certification.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtVerifier(
    @Value("\${jwt.issuer}") private val issuer: String
) {

    /**
     * 발행자와 JWT 사인을 검증합니다.
     * 예외가 발생하는 경우 JWTVerificationException 하위 예외를 던집니다.
     * 하위 예외 : SignatureVerificationException, InvalidClaimException
     */
    fun verifyIssuerAndSecret(token: String, secret: String) {
        val algorithm = Algorithm.HMAC256(secret)

        val verifier: JWTVerifier = JWT.require(algorithm)
            .withIssuer(issuer)
            .build()

        verifier.verify(token)
    }
}