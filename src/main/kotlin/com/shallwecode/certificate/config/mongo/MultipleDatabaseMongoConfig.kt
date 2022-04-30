package com.shallwecode.certificate.config.mongo

import com.mongodb.reactivestreams.client.MongoClients
import com.shallwecode.certificate.config.mongo.template.ShallWeCodeMongoTemplateWrapper
import com.shallwecode.certificate.config.mongo.template.TestMongoTemplateWrapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.mongodb.core.ReactiveMongoTemplate

@Configuration
class MultipleDatabaseMongoConfig {
    @Primary
    @Bean
    fun shallWeCodeMongoTemplate(): ShallWeCodeMongoTemplateWrapper {
        return ShallWeCodeMongoTemplateWrapper(ReactiveMongoTemplate(MongoClients.create(), "shallwecode"))
    }

    @Bean
    fun testMongoTemplate(): TestMongoTemplateWrapper {
        return TestMongoTemplateWrapper(ReactiveMongoTemplate(MongoClients.create(), "test"))
    }


}