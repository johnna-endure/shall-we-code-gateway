package com.shallwecode.certification.jwt.persistence.repository

import com.shallwecode.certification.jwt.persistence.document.RefreshToken
import org.springframework.data.repository.CrudRepository

interface RefreshTokenRedisRepository : CrudRepository<RefreshToken, Long> {


}