package com.shallwecode.certification.authentication.controller

import com.shallwecode.certification.authentication.persistence.repository.UserAuthenticationMongoRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(
    val userAuthenticationMongoRepository: UserAuthenticationMongoRepository
) {

    @PostMapping()
    fun login() {
    }

}