package com.shallwecode.certification.authentication.advice

import com.shallwecode.certification.common.http.response.HttpResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

// TODO 테스트 작성 필요
@RestControllerAdvice
class StandardExceptionHandlers {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun notFountExceptionHandler(exception: IllegalArgumentException): HttpResponse<String> {
        return HttpResponse(
            message = exception.message ?: "잘못된 요청입니다."
        )
    }
}