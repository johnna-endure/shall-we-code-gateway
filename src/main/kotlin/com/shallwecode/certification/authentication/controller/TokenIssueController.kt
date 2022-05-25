package com.shallwecode.certification.authentication.controller

import com.shallwecode.certification.authentication.persistence.repository.RefreshTokenRedisRepository
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/token")
@RestController
class TokenIssueController(
    val refreshTokenRedisRepository: RefreshTokenRedisRepository
) {
    // TODO TDD 방식으로 기능 개발 구현할 것
    @PostMapping("/reissue")
    fun reissueToken(@RequestHeader(HttpHeaders.AUTHORIZATION) refreshToken: String) {

    }
}