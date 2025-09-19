package com.cattlelabs.cattleapp.data.model

data class ApiResponse<T>(
    val statusCode: Int,
    val message: String,
    val body: T
)