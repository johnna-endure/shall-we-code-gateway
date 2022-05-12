package com.shallwecode.filter

import com.auth0.jwt.exceptions.TokenExpiredException
import com.fasterxml.jackson.databind.ObjectMapper
import com.shallwecode.certification.authentication.jwt.JwtVerifier
import mu.KotlinLogging
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

// TODO 테스트 필요
// 어떻게 테스트함?
@Component
class JwtAuthenticationGatewayFilterFactory(
    val jwtVerifier: JwtVerifier
) : AbstractGatewayFilterFactory<Config>(Config::class.java) {

    private final val USER_ID_HEADER = "UserId"
    private final val ROLES_HEADER = "Roles"

    val logger = KotlinLogging.logger {}
    override fun shortcutFieldOrder(): MutableList<String> {
        return mutableListOf("role")
    }

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            val accessToken = exchange.request.headers[HttpHeaders.AUTHORIZATION]?.get(0)
                ?: return@GatewayFilter exchangeRequestWithAnonymousRoleHeader(exchange, chain)
            verifyAccessToken(exchange, chain, accessToken)
        }
    }


    private fun verifyAccessToken(
        exchange: ServerWebExchange,
        chain: GatewayFilterChain,
        accessToken: String
    ): Mono<Void> {
        try {
            val decodedJWT = jwtVerifier.verifyAccessToken(accessToken)
            return chain.filter(
                exchange.mutate()
                    .request {
                        it.header(USER_ID_HEADER, decodedJWT.getClaim("userId").asLong().toString())
                        it.header(ROLES_HEADER, decodedJWT.getClaim("roles").asList(String::class.java).joinToString())
                    }.build()
            )
        } catch (ex: TokenExpiredException) {
            logger.debug { ex.stackTraceToString() }
            return exchangeResponseWithTokenExpiredResult(exchange)
        } catch (ex: Exception) {
            logger.debug { ex.stackTraceToString() }
            return exchangeRequestWithAnonymousRoleHeader(exchange, chain)
        }
    }

    private fun exchangeResponseWithTokenExpiredResult(exchange: ServerWebExchange): Mono<Void> {
        exchange.response.statusCode = HttpStatus.UNAUTHORIZED
        val jsonMapper = ObjectMapper()
        val expiredResultJson = jsonMapper.writeValueAsBytes(TokenExpiredResult())

        return exchange.response.writeWith(
            Mono.just(
                exchange.response
                    .bufferFactory()
                    .wrap(expiredResultJson)
            )
        )
    }

    private fun exchangeRequestWithAnonymousRoleHeader(
        exchange: ServerWebExchange,
        chain: GatewayFilterChain
    ): Mono<Void> {
        return chain.filter(
            exchange.mutate()
                .request {
                    it.header(ROLES_HEADER, "anonymous")
                }.build()
        )
    }
}

class Config {}

data class TokenExpiredResult(val expired: Boolean = true)