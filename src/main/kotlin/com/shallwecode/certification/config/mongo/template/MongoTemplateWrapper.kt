package com.shallwecode.certification.config.mongo.template

import org.springframework.data.mongodb.core.ReactiveMongoTemplate

interface MongoTemplateWrapper {
    val template: ReactiveMongoTemplate
}