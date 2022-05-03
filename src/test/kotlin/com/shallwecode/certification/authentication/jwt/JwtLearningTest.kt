package com.shallwecode.certification.authentication.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class JwtLearningTest {

    @Test
    fun `JWT 생성`() {
        val algorithm = Algorithm.HMAC256("secret")
        val token = JWT.create()
            .withIssuer("shallwecode")
            .sign(algorithm)

        assertThat(token).isNotNull.run { println("create token : $token") }
    }

    @Test
    fun `토큰 검증 - sign 부분이 같은 경우 예외를 던지지 않음`() {
        //given
        val algorithm = Algorithm.HMAC256("secret")
        val token = JWT.create()
            .withAudience("cws")
            .withIssuer("shallwecode")
            .sign(algorithm)


        val verifier: JWTVerifier = JWT.require(algorithm)
            .withIssuer("shallwecode")
            .build() //Reusable verifier instance

        //when
        assertDoesNotThrow { verifier.verify(token) }
            .let {
                println("decodedJWT.token : ${it.token}")
                println("decodedJWT.header : ${it.header}")
                println("decodedJWT.signature : ${it.signature}")
                println("decodedJWT.payload : ${it.payload}")
                println("decodedJWT.issuedAt : ${it.issuedAt}")
                println("decodedJWT.claims : ${it.claims}")
                println("decodedJWT.audience : ${it.audience}")
                println("decodedJWT.expiresAt : ${it.expiresAt}")
                println("decodedJWT.type : ${it.type}")
            }
    }

    @Test
    fun `토큰 검증 - sign 부분이 다를 경우 JWTVerificationException 예외를 던짐`() {
        //given
        val differentAlgorithm = Algorithm.HMAC256("differentSecret")
        val token = JWT.create()
            .withIssuer("shallwecode")
            .sign(differentAlgorithm)


        val algorithm = Algorithm.HMAC256("secret")
        val verifier: JWTVerifier = JWT.require(algorithm)
            .withIssuer("shallwecode")
            .build() //Reusable verifier instance

        //when, then
        assertThrows<JWTVerificationException> { verifier.verify(token) }
    }

    @Test
    fun `payload claim 정보를 포함한 토큰 생성`() {
        val issuer = "shallwecode"
        val zoneId = ZoneId.of("Asia/Seoul")

        val issuedAt = Date.from(
            LocalDate.now()
                .atStartOfDay(zoneId)
                .toInstant()
        )

        val expiredAt = Date.from(
            LocalDate.now().plusDays(1)
                .atStartOfDay(zoneId)
                .toInstant()
        )
        val userId = 12
        val role = arrayOf("admin", "user")
        val algorithm = Algorithm.HMAC256("secret")


        val token = JWT.create()
            .withIssuer(issuer)
            .withExpiresAt(expiredAt)
            .withIssuedAt(issuedAt)
            .withClaim("userId", userId)
            .withArrayClaim("role", role)
            .sign(algorithm)

        val verifier: JWTVerifier = JWT.require(algorithm)
            .withIssuer("shallwecode")
            .build()

        verifier.verify(token)
            .let {
                assertThat(it.issuer).isEqualTo(issuer)
                assertThat(it.expiresAt.toInstant()).isEqualTo(expiredAt.toInstant())
                assertThat(it.issuedAt.toInstant()).isEqualTo(issuedAt.toInstant())
                assertThat(it.claims["userId"]?.asInt()).isEqualTo(userId)
                assertThat(it.claims["role"]?.asArray(String::class.java)).isEqualTo(role)
            }
    }


}