package com.cattlelabs.cattleapp.data.model

import com.google.gson.annotations.SerializedName

data class Cattle(
    @SerializedName("name")
    val name: String?,

    @SerializedName("tag_number")
    val tagNumber: String?,

    @SerializedName("data_entry_date")
    val dataEntryDate: String?,

    @SerializedName("tagging_date")
    val taggingDate: String?,

    @SerializedName("species")
    val species: String?,

    @SerializedName("sex")
    val sex: String?,

    @SerializedName("dob")
    val dob: String?,

    @SerializedName("breed_id")
    val breedId: String?,

    @SerializedName("breed_name")
    val breedName: String?
)