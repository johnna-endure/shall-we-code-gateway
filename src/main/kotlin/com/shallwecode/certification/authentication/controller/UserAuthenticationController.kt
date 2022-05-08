package com.shallwecode.certification.authentication.controller

import com.shallwecode.certification.authentication.controller.request.UserAuthenticationSaveRequest
import com.shallwecode.certification.authentication.controller.response.model.UserAuthenticationModel
import com.shallwecode.certification.authentication.exception.CreateDataException
import com.shallwecode.certification.authentication.exception.DeleteDataException
import com.shallwecode.certification.authentication.exception.NotFoundDataException
import com.shallwecode.certification.authentication.persistence.document.UserAuthentication
import com.shallwecode.certification.authentication.persistence.repository.UserAuthenticationMongoRepository
import com.shallwecode.certification.common.http.response.HttpResponse
import com.shallwecode.certification.common.util.modelmapper.ModelMapper
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

/**
 * UserAuthentication 정보 다루기 위한 CRUD API를 담은 컨트롤러 클래스입니다.
 *
 */
// TODO 적절한 에러 처리 필요
@RequestMapping("/authentication")
@RestController
class UserAuthenticationController(
    private val userAuthenticationMongoRepository: UserAuthenticationMongoRepository
) {

    private val modelMapper = ModelMapper()

    /**
     *  사용자 인증 정보를 저장하는 API
     *  존재하지 않는 경우 새로 저장하고, 존재하는 경우 업데이트 합니다.
     */
    @ResponseStatus(OK)
    @PutMapping("/user")
    fun saveUserAuthentication(@RequestBody request: UserAuthenticationSaveRequest): Mono<HttpResponse<Unit>> {
        val authentication = request.toUserAuthentication()
        return userAuthenticationMongoRepository.upsert(authentication)
            .map {
                HttpResponse<Unit>(message = "saved")
            }.doOnError { it.printStackTrace() }
            .onErrorMap { CreateDataException(it.message) }
    }

    @ResponseStatus(OK)
    @GetMapping("/users/{userId}")
    fun getUserAuthentication(@PathVariable("userId") userId: Long): Mono<HttpResponse<UserAuthenticationModel>> {
        return userAuthenticationMongoRepository.findByUserId(userId)
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
        return userAuthenticationMongoRepository.removeById(userId)
            .map {
                HttpResponse(
                    message = "deleted",
                    body = mapOf("userId" to it.userId)
                )
            }.onErrorMap { DeleteDataException(it.message) }
    }

}