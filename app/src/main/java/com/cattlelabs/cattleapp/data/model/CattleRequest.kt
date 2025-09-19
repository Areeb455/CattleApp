package com.cattlelabs.cattleapp.data.model

data class CattleRequest(
    val userId: String,
    val tagNumber: String,
    val species: String,
    val breed: String,
    val name: String? = null,
    val dataEntryDate: String? = null,
    val taggingDate: String? = null,
    val sex: String? = null,
    val dob: String? = null
)
