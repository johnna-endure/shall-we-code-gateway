package com.shallwecode.config.mongo.template

import org.springframework.data.mongodb.core.ReactiveMongoTemplate

class TestMongoTemplateWrapper(
    override val template: ReactiveMongoTemplate
) : MongoTemplateWrapper