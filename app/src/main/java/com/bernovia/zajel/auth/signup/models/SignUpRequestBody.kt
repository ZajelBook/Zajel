package com.bernovia.zajel.auth.signup.models


import com.google.gson.annotations.SerializedName

data class SignUpRequestBody(
    @SerializedName("birth_date")
    val birthDate: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("fcm_token")
    val fcmToken: String?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("password_confirmation")
    val passwordConfirmation: String?,
    @SerializedName("phone_number")
    val phoneNumber: String?
)