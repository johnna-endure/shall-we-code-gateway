package com.shallwecode.certification.authentication.controller.util

// TODO 테스트 작성 필요
// 사용하지 않음
object EmailValidator {
    fun validateEmail(email: String): Boolean {
        val regex = """^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*(\.[a-zA-Z])*${'$'}""".toRegex()
        return regex.matches(email)
    }
}
