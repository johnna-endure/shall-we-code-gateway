package com.shallwecode.certificate.config.mongo.template

import org.springframework.data.mongodb.core.ReactiveMongoTemplate

interface MongoTemplateWrapper {
    val template: ReactiveMongoTemplate
}