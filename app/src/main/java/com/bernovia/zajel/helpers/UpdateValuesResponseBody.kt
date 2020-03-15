package com.bernovia.zajel.helpers


import com.google.gson.annotations.SerializedName

data class UpdateValuesResponseBody(
    @SerializedName("status")
    val status: String?
)