package com.cattlelabs.cattleapp.data.model

import com.google.gson.annotations.SerializedName

data class BreedDetails(
    @SerializedName("breedId")
    val breedId: String?,

    @SerializedName("breedName")
    val breedName: String?,

    @SerializedName("breedingTrait")
    val breedingTract: String?,

    @SerializedName("location")
    val location: List<String>?,

    @SerializedName("mainUses")
    val mainUses: String?,

    @SerializedName("physicalDesc")
    val physicalDesc: String?,

    @SerializedName("species")
    val species: String?
)