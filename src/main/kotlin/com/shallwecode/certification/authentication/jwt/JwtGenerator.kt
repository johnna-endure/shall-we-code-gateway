package com.shallwecode.certification.authentication.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.shallwecode.certification.jwt.config.JwtProperties
import com.shallwecode.common.constant.TimeConstants.KST_TIME_ZONE_ID
import org.springframework.stereotype.Component
import java.lang.Long.parseLong
import java.time.LocalDate
import java.time.LocalDateTime.*
import java.time.ZoneId
import java.util.*

@Component
class JwtGenerator(
    val jwtProperties: JwtProperties
) {

    private var zoneId: ZoneId = ZoneId.of(KST_TIME_ZONE_ID.toString())
    private var issuedAt: Date = Date.from(
        LocalDate.now()
            .atStartOfDay(zoneId)
            .toInstant()
    )
    private var expiredAt: Date = Date.from(
        LocalDate.now().plusDays(parseLong(this.jwtProperties.expireDurationDay))
            .atStartOfDay(zoneId)
            .toInstant()
    )

    /**
     * 토큰을 발행합니다.
     * @param userId 사용자 아이디
     * @param role 사용자 권한
     * @param userSecret 사용자 정보를 이용한 시크릿 값(아이디 + 패스워드의 해시값)
     */
    fun issueAccessToken(userId: Long, password: String, role: Array<String>): String {
        val userSecret = "${userId}${password}".hashCode()
        val algorithm = Algorithm.HMAC256("${jwtProperties.secret}${userSecret}")

        return JWT.create()
            .withIssuer(jwtProperties.issuer)
            .withExpiresAt(expiredAt)
            .withIssuedAt(issuedAt)
            .withClaim("userId", userId)
            .withArrayClaim("role", role)
            .sign(algorithm)
    }

    /**
     * 토큰을 발행합니다.
     * @param userId 사용자 아이디
     * @param role 사용자 권한
     * @param userSecret 사용자 정보를 이용한 시크릿 값(아이디 + 패스워드의 해시값)
     */
    fun issueRefreshToken(userId: Long, password: String, role: Array<String>): String {
        val userSecret = "${userId}${password}${now()}".hashCode()
        val algorithm = Algorithm.HMAC256("${jwtProperties.secret}${userSecret}")

        return JWT.create()
            .withIssuer(jwtProperties.issuer)
            .withExpiresAt(expiredAt)
            .withIssuedAt(issuedAt)
            .withClaim("userId", userId)
            .withArrayClaim("role", role)
            .sign(algorithm)
    }
}