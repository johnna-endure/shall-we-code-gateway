package com.shallwecode.certification.common.http.response

class HttpResponse<T>(
    val message: String? = null,
    val body: T? = null
)