package com.shallwecode.certificate.common.http.response

class HttpResponse<T>(
    val message: String? = null,
    val body: T? = null
)