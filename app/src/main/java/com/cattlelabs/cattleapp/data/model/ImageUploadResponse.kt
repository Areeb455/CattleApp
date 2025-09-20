package com.cattlelabs.cattleapp.data.model

data class PredictionBody(
    val url: String,
    val predictions: List<Prediction>
)