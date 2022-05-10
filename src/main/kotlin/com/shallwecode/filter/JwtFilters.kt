package com.shallwecode.filter

import org.springframework.context.annotation.Configuration

@Configuration
class JwtFilters {

//    @Bean
//    fun jwtFilter(jwtVerifier: JwtVerifier) {
//    fun jwtFilter(jwtVerifier: JwtVerifier): GatewayFilter {
//        return GlobalFilter { exchange, chain ->
//            chain.filter(exchange)
//                .then(Mono.just(exchange))
//                .map {
//                    val accessToken = it.request.headers.get(HttpHeaders.AUTHORIZATION.toString())
//
//                    it
//                }
//        }

//        OrderedGatewayFilter(GatewayFilter { exchange, chain ->
//            val accessToken = exchange.request.headers[HttpHeaders.AUTHORIZATION]!![0]
//            val decodedJWT = jwtVerifier.verifyAccessToken(accessToken)
//
//            exchange.request.headers.remove(HttpHeaders.AUTHORIZATION)
//            exchange.request.headers.add("userId", userTokenClaim.userId.toString())
//            exchange.request.headers.add("roles", userTokenClaim.roles.joinToString())

//
//        }, -1)
//
//
//    }
}