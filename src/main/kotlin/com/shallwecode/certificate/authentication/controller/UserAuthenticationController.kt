package com.shallwecode.certificate.authentication.controller

import com.shallwecode.certificate.authentication.controller.model.UserAuthenticationModel
import com.shallwecode.certificate.authentication.controller.request.UserAuthenticationRequest
import com.shallwecode.certificate.authentication.exception.CreateDataException
import com.shallwecode.certificate.authentication.exception.DeleteDataException
import com.shallwecode.certificate.authentication.exception.NotFoundDataException
import com.shallwecode.certificate.authentication.persistence.document.UserAuthentication
import com.shallwecode.certificate.authentication.persistence.repository.UserAuthenticationRepository
import com.shallwecode.certificate.common.http.response.HttpResponse
import com.shallwecode.certificate.common.util.modelmapper.ModelMapper
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

// TODO 적절한 에러 처리 필요
@RequestMapping("/authentication")
@RestController
class UserAuthenticationController(
    private val userAuthenticationRepository: UserAuthenticationRepository
) {

    private val modelMapper = ModelMapper()

    /**
     *  사용자 인증 정보를 저장하는 API
     *  존재하지 않는 경우 새로 저장하고, 존재하는 경우 업데이트 합니다.
     */
    @ResponseStatus(OK)
    @PutMapping("/user")
    fun saveUserAuthentication(@RequestBody request: UserAuthenticationRequest): Mono<HttpResponse<Unit>> {
        val authentication = request.toUserAuthentication()
        return userAuthenticationRepository.upsert(authentication)
            .map {
                HttpResponse<Unit>(message = "saved")
            }.doOnError { it.printStackTrace() }
            .onErrorMap { CreateDataException(it.message) }
    }

    @ResponseStatus(OK)
    @GetMapping("/users/{userId}")
    fun getUserAuthentication(@PathVariable("userId") userId: Long): Mono<HttpResponse<UserAuthenticationModel>> {
        return userAuthenticationRepository.findByUserId(userId)
            .map {
                HttpResponse(
                    message = "found",
                    body = modelMapper.mapper<UserAuthentication, UserAuthenticationModel>(it)
                )
            }.onErrorMap { NotFoundDataException(it.message) }
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/users/{userId}")
    fun deleteUserAuthentication(@PathVariable("userId") userId: Long): Mono<HttpResponse<Map<String, Long>>> {
        return userAuthenticationRepository.removeById(userId)
            .map {
                HttpResponse(
                    message = "deleted",
                    body = mapOf("userId" to it.userId)
                )
            }.onErrorMap { DeleteDataException(it.message) }
    }
}