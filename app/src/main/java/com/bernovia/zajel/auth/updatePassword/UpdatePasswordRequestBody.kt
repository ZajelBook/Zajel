package com.bernovia.zajel.auth.updatePassword


import com.google.gson.annotations.SerializedName

data class UpdatePasswordRequestBody(
    @SerializedName("password")
    val password: String,
    @SerializedName("password_confirmation")
    val passwordConfirmation: String
)