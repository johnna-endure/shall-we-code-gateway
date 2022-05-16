package com.shallwecode.certification.authentication.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.InvalidClaimException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.shallwecode.certification.config.jwt.JwtProperties
import com.shallwecode.common.constant.TimeConstants.KST_TIME_ZONE_ID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.lang.Long.parseLong
import java.time.LocalDate
import java.util.*


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
    fun `verifyAccessToken - 토큰이 만료 됐을 경우, TokenExpiredException 예외를 던지는지 테스트 `() {
        // given
        val algorithm = Algorithm.HMAC256(jwtProperties.secret)
        val expiredAt = Date.from(
            LocalDate.now().minusDays(1)
                .atStartOfDay(KST_TIME_ZONE_ID).toInstant()
        )

        val token = JWT.create()
            .withClaim("userId", 1L)
            .withClaim("roles", listOf("user"))
            .withExpiresAt(expiredAt)
            .withIssuer(jwtProperties.issuer)
            .sign(algorithm)

        // when
        assertThrows<TokenExpiredException> { jwtVerifier.verifyAccessToken(token) }
    }

    @Test
    fun `verifyAccessToken - 사인 검증에 실패했을 때, SignatureVerificationException 예외를 던지는지 테스트`() {
        // given
        val userId = 12L
        val password = "testpassword"
        val userSecret = "${userId}${password}".hashCode()
        val algorithm = Algorithm.HMAC256("${userSecret}${jwtProperties.secret}addsomestring")
        val expiredAt = Date.from(
            LocalDate.now().plusDays(1)
                .atStartOfDay(KST_TIME_ZONE_ID).toInstant()
        )

        val token = JWT.create()
            .withClaim("userId", 1L)
            .withClaim("roles", listOf("user"))
            .withExpiresAt(expiredAt)
            .withIssuer(jwtProperties.issuer)
            .sign(algorithm)

        // when
        assertThrows<SignatureVerificationException> { jwtVerifier.verifyAccessToken(token) }
    }

    @Test
    fun `verifyAccessToken - 발행인 정보가 빠졌을 경우, InvalidClaimException 예외를 던지는지 테스트`() {
        // given
        val algorithm = Algorithm.HMAC256(jwtProperties.secret)
        val expiredAt = Date.from(
            LocalDate.now().plusDays(1)
                .atStartOfDay(KST_TIME_ZONE_ID).toInstant()
        )

        val token = JWT.create()
            .withClaim("userId", 1L)
            .withClaim("roles", listOf("user"))
            .withExpiresAt(expiredAt)
            .sign(algorithm)

        // when
        assertThrows<InvalidClaimException> { jwtVerifier.verifyAccessToken(token) }
    }


    @Test
    fun `verifyAccessToken - 올바른 토큰이 검증에 통과하는지 테스트`() {
        // given
        val algorithm = Algorithm.HMAC256(jwtProperties.secret)
        val expiredAt = Date.from(
            LocalDate.now().plusDays(1)
                .atStartOfDay(KST_TIME_ZONE_ID).toInstant()
        )
        val token = JWT.create()
            .withClaim("userId", 1L)
            .withClaim("roles", listOf("user"))
            .withExpiresAt(expiredAt)
            .withIssuer(jwtProperties.issuer)
            .sign(algorithm)

        // when
        assertDoesNotThrow { jwtVerifier.verifyAccessToken(token) }
    }

    @Test
    fun `verifyAccessToken - 검증에 성공하고 DecodedJWT 를 반환한 경우`() {
        // given
        val algorithm = Algorithm.HMAC256(jwtProperties.secret)
        val addDays = parseLong(jwtProperties.expireDurationDay)
        val expiredAt = Date.from(
            LocalDate.now().plusDays(addDays)
                .atStartOfDay(KST_TIME_ZONE_ID).toInstant()
        )
        val validToken = JWT.create()
            .withClaim("userId", 1L)
            .withClaim("roles", listOf("user"))
            .withExpiresAt(expiredAt)
            .withIssuer(jwtProperties.issuer)
            .sign(algorithm)

        // when
        val decodedJWT = jwtVerifier.verifyAccessToken(validToken)

        // when
        assertThat(decodedJWT.issuer).isEqualTo(jwtProperties.issuer)
        assertThat(decodedJWT.expiresAt).isEqualTo(expiredAt.toInstant())
        assertThat(decodedJWT.claims["userId"]!!.asLong()).isEqualTo(1L)
        assertThat(decodedJWT.claims["roles"]!!.asList(String::class.java)).isEqualTo(listOf("user"))
    }
}