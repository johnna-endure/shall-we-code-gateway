package com.shallwecode.certification.config.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "jwt")
@Component
class JwtProperties {
    lateinit var issuer: String
    lateinit var secret: String
}