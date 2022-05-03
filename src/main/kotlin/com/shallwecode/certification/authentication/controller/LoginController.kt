package com.shallwecode.certification.authentication.controller

import com.shallwecode.certification.authentication.controller.request.LoginRequest
import com.shallwecode.certification.authentication.jwt.JwtGenerator
import com.shallwecode.certification.authentication.persistence.repository.UserAuthenticationMongoRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class LoginController(
    val userAuthenticationMongoRepository: UserAuthenticationMongoRepository,
//    val refreshTokenRedisRepository: RefreshTokenRedisRepository,
    val jwtGenerator: JwtGenerator,

    ) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest) {
        userAuthenticationMongoRepository.findByEmail(request.email)
            .filter { it.email == request.email }
            .flatMap {
                val accessToken = jwtGenerator.issueAccessToken(it.userId, it.password, it.roles.toTypedArray())
                val refreshToken = jwtGenerator.issueRefreshToken(it.userId, it.password, it.roles.toTypedArray())

                Mono.just(
                    ""
                )
            }
    }

}