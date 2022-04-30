package com.shallwecode.certificate.config.mongo.template

import org.springframework.data.mongodb.core.ReactiveMongoTemplate

class ShallWeCodeMongoTemplateWrapper(
    override val template: ReactiveMongoTemplate,
) : MongoTemplateWrapper