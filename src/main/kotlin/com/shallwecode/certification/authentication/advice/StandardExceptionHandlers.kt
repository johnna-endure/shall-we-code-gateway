package com.shallwecode.certification.authentication.advice

import com.shallwecode.certification.authentication.exception.NotFoundDataException
import com.shallwecode.certification.common.http.response.HttpResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class StandardExceptionHandlers {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun badRequestExceptionHandler(exception: IllegalArgumentException): HttpResponse<String> {
        return HttpResponse(
            message = exception.message ?: "잘못된 요청입니다."
        )
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundDataException::class)
    fun notFountExceptionHandler(exception: NotFoundDataException): HttpResponse<String> {
        return HttpResponse(
            message = exception.message ?: "데이터를 찾을 수 없습니다."
        )
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun otherExceptionHandler(exception: Exception): HttpResponse<String> {
        return HttpResponse(
            message = exception.message ?: "예상하지 못한 예외가 던져졌습니다."
        )
    }
}