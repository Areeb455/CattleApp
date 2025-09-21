package com.cattlelabs.cattleapp.data.model

import com.google.gson.annotations.SerializedName

data class CattleResponse(
    @SerializedName("user_id")
    val userId: String?,

    @SerializedName("tag_number")
    val tagNumber: String?,

    @SerializedName("breed_id")
    val breedId: String?,

    @SerializedName("breed_name")
    val breedName: String?
)