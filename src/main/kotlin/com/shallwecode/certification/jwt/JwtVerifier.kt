package com.shallwecode.certification.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.shallwecode.certification.jwt.config.JwtProperties
import org.springframework.stereotype.Component

/**
 * JWT 서명 부분을 검증하는 역할의 클래스입니다.
 */
@Component
class JwtVerifier(
    val jwtProperties: JwtProperties
) {

    /**
     * 발행자와 JWT 사인을 검증합니다.
     * 예외가 발생하는 경우 JWTVerificationException 하위 예외를 던집니다.
     * 하위 예외 : SignatureVerificationException, InvalidClaimException
     */
    fun verifyIssuerAndSecret(token: String, secret: String) {
        val algorithm = Algorithm.HMAC256(secret)

        val verifier: JWTVerifier = JWT.require(algorithm)
            .withIssuer(jwtProperties.issuer)
            .build()

        verifier.verify(token)
    }
}