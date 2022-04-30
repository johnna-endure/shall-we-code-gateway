package com.shallwecode.certification.authentication.persistence.repository

import com.shallwecode.certification.authentication.persistence.document.RefreshToken
import org.springframework.data.repository.CrudRepository

interface RefreshTokenRedisRepository : CrudRepository<RefreshToken, Long> {


}