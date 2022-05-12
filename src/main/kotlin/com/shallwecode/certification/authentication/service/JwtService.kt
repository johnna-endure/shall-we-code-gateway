package com.shallwecode.certification.authentication.service

import com.shallwecode.certification.authentication.jwt.JwtGenerator
import com.shallwecode.certification.authentication.jwt.JwtVerifier
import com.shallwecode.certification.authentication.persistence.repository.RefreshTokenRedisRepository
import com.shallwecode.certification.jwt.config.JwtProperties

// TODO 단위 테스트 필요
//@Service
class JwtService(
    val jwtProperties: JwtProperties,
    val jwtVerifier: JwtVerifier,
    val jwtGenerator: JwtGenerator,
    val refreshTokenRedisRepository: RefreshTokenRedisRepository
) {

}