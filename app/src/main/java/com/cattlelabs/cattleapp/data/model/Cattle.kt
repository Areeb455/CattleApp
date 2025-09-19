package com.cattlelabs.cattleapp.data.model

data class Cattle(
    val name: String?,
    val tagNumber: String,
    val dataEntryDate: String?,
    val taggingDate: String?,
    val species: String,
    val sex: String?,
    val dob: String?,
    val breedId: String,
    val breedName: String
)