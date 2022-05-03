package com.shallwecode.certification.authentication.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.InvalidClaimException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
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
     * 토큰의 발행자와 만료 여부, JWT 사인을 검증합니다.
     * @throws TokenExpiredException 토큰이 만료됐을 경우
     * @throws SignatureVerificationException 토큰 사인 검증에 실패한 경우
     * @throws InvalidClaimException 발행인 등 클레임 검증에 실패한 경우
     *
     */
    fun verifyAccessToken(token: String, userId: Long, password: String) {
        val userSecret = "${userId}${password}".hashCode()
        val algorithm = Algorithm.HMAC256("${userSecret}${jwtProperties.secret}")

        val verifier: JWTVerifier = JWT.require(algorithm)
            .withIssuer(jwtProperties.issuer)
            .build()

        verifier.verify(token)
    }

    //TODO 리프레시 토큰 검증 메서드 필요
}