package com.shallwecode.certificate.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime.now
import java.time.ZoneId
import java.util.*

@Component
class JWTGenerator(
    @Value("\${jwt.issuer}") private val issuer: String,
    @Value("\${jwt.secret}") private val secret: String
) {

    /**
     * 토큰을 발행합니다.
     * @param userId 사용자 아이디
     * @param role 사용자 권한
     * @param userSecret 사용자 정보를 이용한 시크릿 값(아이디 + 패스워드의 해시값)
     */
    fun issueToken(userId: Long, role: Array<String>, userSecret: Int): String {
        val zoneId = ZoneId.of("Asia/Seoul")
        val issuedAt = Date.from(
            LocalDate.now()
                .atStartOfDay(zoneId)
                .toInstant()
        )
        val expiredAt = Date.from(
            LocalDate.now().plusDays(7)
                .atStartOfDay(zoneId)
                .toInstant()
        )

        val algorithm = Algorithm.HMAC256("${secret}${userSecret}${now()}")

        return JWT.create()
            .withIssuer(issuer)
            .withExpiresAt(expiredAt)
            .withIssuedAt(issuedAt)
            .withClaim("userId", userId)
            .withArrayClaim("role", role)
            .sign(algorithm)
    }

    /**
     * 토큰 생성시 issuer, secret 값이 존재하는지 검증합니다
     * 존재하지 않을 경우 JWTCreateException 예외를 던집니다.
     */

}