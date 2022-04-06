package com.shallwecode.jwt

import com.auth0.jwt.exceptions.InvalidClaimException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.shallwecode.certificate.jwt.JWTGenerator
import com.shallwecode.certificate.jwt.JWTVerifier
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [JWTVerifier::class])
class JWTVerifierTest(@Autowired var jwtVerifier: JWTVerifier) {

    @Test
    fun `토큰 검증 통과`() {
        //given
        val generator: JWTGenerator = JWTGenerator("shall-we-code", "shall-we-code-secret-key")
        val token = generator.generateToken(1L, arrayOf("user"))

        //when, then
        assertDoesNotThrow { jwtVerifier.verifyIssuerAndSecret(token) }
    }

    @Test
    fun `토큰 검증 실패 - 시크릿 키가 다른 경우`() {
        //given
        val generator: JWTGenerator = JWTGenerator("shall-we-code", "random-key")
        val token = generator.generateToken(1L, arrayOf("user"))

        //when, then
        assertThrows<SignatureVerificationException> { jwtVerifier.verifyIssuerAndSecret(token) }
    }

    @Test
    fun `토큰 검증 실패 - 발행인이 다른 경우`() {
        //given
        val generator: JWTGenerator = JWTGenerator("whoami", "shall-we-code-secret-key")
        val token = generator.generateToken(1L, arrayOf("user"))

        //when, then
        assertThrows<InvalidClaimException> { jwtVerifier.verifyIssuerAndSecret(token) }
    }
}