package com.shallwecode.certification.jwt.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

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

class JwtPropertyInitializeException(message: String) : RuntimeException(message)
