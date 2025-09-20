package com.cattlelabs.cattleapp.data.model

import com.google.gson.annotations.SerializedName

data class Prediction(
    @SerializedName("breed_id")
    val breedId: String?,

    @SerializedName("breed")
    val breed: String?,

    @SerializedName("accuracy")
    val accuracy: Float?,

    @SerializedName("location")
    val location: List<String>

)