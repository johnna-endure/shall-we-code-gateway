package com.shallwecode.certification.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.shallwecode.certification.config.jwt.JwtProperties
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime.now
import java.time.ZoneId
import java.util.*

@Component
class JwtGenerator(
    val jwtProperties: JwtProperties
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

        val algorithm = Algorithm.HMAC256("${jwtProperties.secret}${userSecret}${now()}")

        return JWT.create()
            .withIssuer(jwtProperties.issuer)
            .withExpiresAt(expiredAt)
            .withIssuedAt(issuedAt)
            .withClaim("userId", userId)
            .withArrayClaim("role", role)
            .sign(algorithm)
    }

}