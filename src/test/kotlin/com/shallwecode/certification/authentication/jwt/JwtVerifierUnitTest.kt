package com.shallwecode.certification.authentication.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.InvalidClaimException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.shallwecode.certification.jwt.config.JwtProperties
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.sql.Date
import java.time.LocalDate.now


class JwtVerifierUnitTest {

    lateinit var jwtVerifier: JwtVerifier
    lateinit var jwtProperties: JwtProperties

    @BeforeEach
    fun beforeEach() {
        jwtProperties = JwtProperties()
        jwtProperties.issuer = "shallwecode"
        jwtProperties.secret = "testSecret"
        jwtProperties.expireDurationDay = "7"

        jwtVerifier = JwtVerifier(jwtProperties)
    }

    @Test
    fun `verifyToken - 토큰이 만료됐을 경우, TokenExpiredException 예외를 던지는지 테스트 `() {
        // given
        val userId = 12L
        val password = "testpassword"
        val userSecret = "${userId}${password}".hashCode()
        val algorithm = Algorithm.HMAC256("${userSecret}${jwtProperties.secret}")

        val token = JWT.create()
            .withExpiresAt(Date.valueOf(now().minusDays(1)))
            .withIssuer(jwtProperties.issuer)
            .sign(algorithm)

        // when
        assertThrows<TokenExpiredException> { jwtVerifier.verifyAccessToken(token, userId, password) }
    }

    @Test
    fun `verifyToken - 사인 검증에 실패했을 때, SignatureVerificationException 예외를 던지는지 테스트`() {
        // given
        val userId = 12L
        val password = "testpassword"
        val userSecret = "${userId}${password}".hashCode()
        val algorithm = Algorithm.HMAC256("${userSecret}${jwtProperties.secret}addsomestring")

        val token = JWT.create()
            .withExpiresAt(Date.valueOf(now().plusDays(1)))
            .withIssuer(jwtProperties.issuer)
            .sign(algorithm)

        // when
        assertThrows<SignatureVerificationException> { jwtVerifier.verifyAccessToken(token, userId, password) }
    }

    @Test
    fun `verifyToken - 발행인 정보가 빠졌을 경우, InvalidClaimException 예외를 던지는지 테스트`() {
        // given
        val userId = 12L
        val password = "testpassword"
        val userSecret = "${userId}${password}".hashCode()
        val algorithm = Algorithm.HMAC256("${userSecret}${jwtProperties.secret}")

        val token = JWT.create()
            .withExpiresAt(Date.valueOf(now().plusDays(1)))
            .sign(algorithm)

        // when
        assertThrows<InvalidClaimException> { jwtVerifier.verifyAccessToken(token, userId, password) }
    }


    @Test
    fun `verifyToken - 올바른 토큰이 검증에 통과하는지 테스트`() {
        // given
        val userId = 12L
        val password = "testpassword"
        val userSecret = "${userId}${password}".hashCode()
        val algorithm = Algorithm.HMAC256("${userSecret}${jwtProperties.secret}")

        val token = JWT.create()
            .withExpiresAt(Date.valueOf(now().plusDays(1)))
            .withIssuer(jwtProperties.issuer)
            .sign(algorithm)
        // when
        assertDoesNotThrow { jwtVerifier.verifyAccessToken(token, userId, password) }
    }

}