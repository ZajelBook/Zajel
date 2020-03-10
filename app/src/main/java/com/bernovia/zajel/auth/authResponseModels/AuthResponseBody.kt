package com.bernovia.zajel.auth.authResponseModels


import com.google.gson.annotations.SerializedName

data class AuthResponseBody<T>(
    @SerializedName("data")
    val `data`: T
)