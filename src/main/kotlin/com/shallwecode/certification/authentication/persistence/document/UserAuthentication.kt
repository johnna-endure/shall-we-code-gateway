package com.shallwecode.certification.authentication.persistence.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
class UserAuthentication(
    @Id
    val userId: Long,
    @Indexed
    val email: String,
    val password: String,
    val roles: List<String>,
    val createDateTime: LocalDateTime,
    // TODO 수정날짜도 필요하다
) {

    override fun toString(): String {
        return "UserAuthentication(userId=$userId, email='$email', password='$password', createDateTime=$createDateTime)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserAuthentication

        if (userId != other.userId) return false

        return true
    }

    override fun hashCode(): Int {
        return userId.hashCode()
    }
}