package com.shallwecode.certificate.authentication.advice

import com.shallwecode.certificate.authentication.exception.CreateDataException
import com.shallwecode.certificate.authentication.exception.DeleteDataException
import com.shallwecode.certificate.authentication.exception.MongoDBException
import com.shallwecode.certificate.authentication.exception.NotFoundDataException
import com.shallwecode.certificate.common.http.response.HttpResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class MongoDBExceptionHandlers {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundDataException::class)
    fun notFountExceptionHandler(exception: NotFoundDataException): HttpResponse<String> {
        return HttpResponse(
            message = exception.message ?: "해당 데이터를 찾을 수 없습니다."
        )
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DeleteDataException::class, CreateDataException::class)
    fun postRequestExceptionHandler(exception: MongoDBException): HttpResponse<String> {
        return HttpResponse(
            message = exception.message ?: "몽고디비 에러 발생."
        )
    }
}