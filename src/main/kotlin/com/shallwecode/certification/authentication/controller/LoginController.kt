package com.shallwecode.certification.authentication.controller

import com.shallwecode.certification.authentication.controller.request.LoginRequest
import com.shallwecode.certification.authentication.controller.response.LoginResult
import com.shallwecode.certification.authentication.jwt.JwtGenerator
import com.shallwecode.certification.authentication.persistence.repository.RefreshTokenRedisRepository
import com.shallwecode.certification.authentication.persistence.repository.UserAuthenticationMongoRepository
import com.shallwecode.certification.common.http.response.HttpResponse
import com.shallwecode.certification.util.PasswordMatcher
import mu.KotlinLogging
import org.springframework.http.ResponseCookie
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@RequestMapping("/authentication")
@RestController
class LoginController(
    val userAuthenticationMongoRepository: UserAuthenticationMongoRepository,
    val refreshTokenRedisRepository: RefreshTokenRedisRepository,
    val jwtGenerator: JwtGenerator
) {

    val logger = KotlinLogging.logger {}

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
        exchange: ServerWebExchange
    ): Mono<HttpResponse<LoginResult>> {
        return userAuthenticationMongoRepository.findByEmail(request.email)
            .flatMap { authentication ->
                if (PasswordMatcher.match(request.password, authentication.password)) {
                    val accessToken = jwtGenerator.issueAccessToken(
                        authentication.userId,
                        authentication.roles.toTypedArray()
                    )
                    val refreshToken = jwtGenerator.issueRefreshToken(
                        authentication.userId,
                        authentication.roles.toTypedArray()
                    )

                    exchange.response.addCookie(ResponseCookie.from("swc_access_token", accessToken).build())
                    exchange.response.addCookie(ResponseCookie.from("swc_refresh_token", refreshToken).build())

                    saveRefreshTokenAndReturnLoginResult(authentication.email, refreshToken)
                } else {
                    Mono.just(HttpResponse(body = LoginResult(false)))
                }
            }
    }

    private fun saveRefreshTokenAndReturnLoginResult(
        email: String,
        refreshToken: String
    ): Mono<HttpResponse<LoginResult>> {
        return refreshTokenRedisRepository.save(email, refreshToken)
            .map { saved ->
                if (saved) {
                    HttpResponse(body = LoginResult(true))
                } else {
                    logger.error { "[login] redis에 refresh token 저장하기 실패" }
                    HttpResponse(body = LoginResult(false))
                }
            }
    }
}