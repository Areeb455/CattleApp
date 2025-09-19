package com.cattlelabs.cattleapp.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("location")
    val location: String
)