package com.shallwecode.certificate.jwt.exception

/**
 * 토큰을 생성하는 경우, 토큰에 전달할 issuer 정보를 찾을 수 없을 때 던져지는 예외
 */
class JWTCreateException(message: String): RuntimeException(message){
}