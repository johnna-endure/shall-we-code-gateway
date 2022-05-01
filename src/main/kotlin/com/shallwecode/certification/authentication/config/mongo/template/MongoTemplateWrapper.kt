package com.shallwecode.certification.authentication.config.mongo.template

import org.springframework.data.mongodb.core.ReactiveMongoTemplate

interface MongoTemplateWrapper {
    val template: ReactiveMongoTemplate
}