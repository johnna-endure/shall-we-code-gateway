package com.shallwecode.certification.authentication.advice

import com.shallwecode.certification.authentication.exception.CreateDataException
import com.shallwecode.certification.authentication.exception.DeleteDataException
import com.shallwecode.certification.authentication.exception.MongoDBException
import com.shallwecode.certification.authentication.exception.NotFoundDataException
import com.shallwecode.certification.common.http.response.HttpResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import reactor.core.publisher.Mono

@RestControllerAdvice
class MongoDBExceptionHandlers {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundDataException::class)
    fun notFountExceptionHandler(exception: NotFoundDataException): Mono<HttpResponse<String>> {
        return Mono.just(
            HttpResponse(
                message = exception.message ?: "해당 데이터를 찾을 수 없습니다."
            )
        )
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DeleteDataException::class, CreateDataException::class)
    fun postRequestExceptionHandler(exception: MongoDBException): Mono<HttpResponse<String>> {
        return Mono.just(
            HttpResponse(
                message = exception.message ?: "몽고디비 에러 발생."
            )
        )
    }
}