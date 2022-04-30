package com.shallwecode.config.mongo.template

import org.springframework.data.mongodb.core.ReactiveMongoTemplate

interface MongoTemplateWrapper {
    val template: ReactiveMongoTemplate
}