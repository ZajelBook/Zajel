package com.bernovia.zajel.actions


import com.google.gson.annotations.SerializedName

data class ActionsResponseBody(
    @SerializedName("status")
    val status: String?
)