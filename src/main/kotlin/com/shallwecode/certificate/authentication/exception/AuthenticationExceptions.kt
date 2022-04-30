package com.shallwecode.certificate.authentication.exception

sealed class MongoDBException(message: String?) : RuntimeException(message)

class NotFoundDataException(message: String?) : MongoDBException(message)
class CreateDataException(message: String?) : MongoDBException(message)
class DeleteDataException(message: String?) : MongoDBException(message)


