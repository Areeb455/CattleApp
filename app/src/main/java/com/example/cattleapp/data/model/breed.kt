package com.example.cattleapp.data.model


data class Breed(
    val id: String,
    val name: String,
    val confidence: Double,
    val region: String? = null
)