package com.shallwecode

import mu.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono

class WebFluxTest {


    @Test
    fun testMono_block() {
        val just = Mono.just("hello")

        val hello = just.block()
        assertThat(hello).isEqualTo("hello")
    }

    @Test
    fun testMono_ifEmpty() {
        val hello: String = "hello"
        val just = Mono.just(hello)
        val message = just.filter { hello == "cws" }
            .switchIfEmpty(Mono.just("alter"))
            .block()

        assertThat(message).isEqualTo("alter")
    }

    data class Person(
        val id: String,
        val password: String
    )

    @Test
    fun testMono_condition() {
        val person: Person = Person(
            id = "cws",
            password = "hello2"
        )
        val just = Mono.just(person)
        val message = just.filter { it.id == "cws" }
            .flatMap {
                if (it.password == "hello") {
                    Mono.just("pass")
                } else {
                    Mono.just("not pass")
                }
            }
            .switchIfEmpty(Mono.just("not found"))
            .block()

        assertThat(message).isEqualTo("not pass")
    }

    @Test
    fun logger() {
        val logger = KotlinLogging.logger {}

        logger.debug { "hello" }

    }
}