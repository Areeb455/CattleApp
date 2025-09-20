package com.cattlelabs.cattleapp.data.model

import com.google.gson.annotations.SerializedName

data class CattleRequest(
    @SerializedName("user_id")
    val userId: String?,

    @SerializedName("tag_number")
    val tagNumber: String,

    @SerializedName("species")
    val species: String,

    @SerializedName("breed")
    val breed: String,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("data_entry_date")
    val dataEntryDate: String? = null,

    @SerializedName("tagging_date")
    val taggingDate: String? = null,

    @SerializedName("sex")
    val sex: String? = null,

    @SerializedName("dob")
    val dob: String? = null
)