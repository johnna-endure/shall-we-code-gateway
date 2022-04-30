package com.shallwecode.certification.authentication.persistence.repository

import com.mongodb.client.result.UpdateResult
import com.shallwecode.certification.authentication.persistence.document.UserAuthentication
import com.shallwecode.certification.config.mongo.template.MongoTemplateWrapper
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class UserAuthenticationMongoRepository(private val mongoTemplateWrapper: MongoTemplateWrapper) {
    val template: ReactiveMongoTemplate = mongoTemplateWrapper.template

    fun upsert(userAuthentication: UserAuthentication): Mono<UpdateResult> {
        return template.upsert(
            query(where("email").`is`(userAuthentication.email)),
            Update()
                .set("_id", userAuthentication.userId)
                .set("password", userAuthentication.password)
                .set("roles", userAuthentication.roles)
                .set("createDateTime", userAuthentication.createDateTime),
            UserAuthentication::class.java
        )
    }

    fun save(userAuthentication: UserAuthentication): Mono<UserAuthentication> {
        return template.save(userAuthentication)
    }

    fun findByUserId(userId: Long): Mono<UserAuthentication> {
        return template.findById(userId, UserAuthentication::class.java)
    }

    fun findByEmail(email: String): Mono<UserAuthentication> {
        return template.findOne(
            query(where("email").`is`(email)),
            UserAuthentication::class.java
        )
    }

    fun removeById(userId: Long): Mono<UserAuthentication> {
        return template.findAndRemove(
            query(where("userId").`is`(userId)),
            UserAuthentication::class.java
        )
    }
}