package com.shallwecode.certification.config.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.lang.Long.parseLong

@ConfigurationProperties(prefix = "jwt")
@Component
class JwtProperties {
    var issuer: String? = null
        get() {
            return field ?: throw JwtPropertyInitializeException("issuer 값이 초기화되지 못했습니다")
        }
    var secret: String? = null
        get() {
            return field ?: throw JwtPropertyInitializeException("secret 값이 초기화되지 못했습니다")
        }
    var expireDurationDay: String? = null
        get() {
            return field ?: throw JwtPropertyInitializeException("expireDurationDay 값이 초기화되지 못했습니다")
        }
}

fun JwtProperties.getExpireDurationSeconds(): Long {
    return parseLong(this.expireDurationDay) * (60L * 60L * 24L)
}

class JwtPropertyInitializeException(message: String) : RuntimeException(message)

