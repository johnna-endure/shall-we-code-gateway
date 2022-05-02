package com.shallwecode.certification.authentication.controller

import com.shallwecode.certification.authentication.service.UserAuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(
    val userAuthenticationService: UserAuthenticationService
) {

    @PostMapping("/login")
    fun login() {

    }

}