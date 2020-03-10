package com.bernovia.zajel.helpers.base.errorModels


import com.google.gson.annotations.SerializedName

data class Errors(
    @SerializedName("email")
    val email: List<String?>?,
    @SerializedName("full_messages")
    val fullMessages: List<String?>?
)