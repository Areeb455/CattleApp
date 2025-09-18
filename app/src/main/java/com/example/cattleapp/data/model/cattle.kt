package com.example.cattleapp.data.model

data class Cattle(
    val id: String,
    val name: String,
    val breed: String,
    val nutritionNeeds: String,
    val injuries: String?,
    val yield: String
)