package com.bernovia.zajel.helpers.base.errorModels


import com.google.gson.annotations.SerializedName

data class ResponseErrorBody(
    @SerializedName("errors")
    val errors: List<String>?,
    @SerializedName("success")
    val success: Boolean?
)