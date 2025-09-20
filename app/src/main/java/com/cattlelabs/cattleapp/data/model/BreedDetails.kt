package com.cattlelabs.cattleapp.data.model

import com.google.gson.annotations.SerializedName

data class BreedDetails(

    @SerializedName("BreedId")
    val breedId: String,

    @SerializedName("BreedName")
    val breedName: String,

    @SerializedName("BreedingTract")
    val breedingTract: String,

    @SerializedName("Location")
    val location: List<String>,

    @SerializedName("MainUses")
    val mainUses: String,

    @SerializedName("PhysicalDesc")
    val physicalDesc: String,

    @SerializedName("Species")
    val species: String,

)