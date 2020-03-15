package com.bernovia.zajel.auth.authResponseModels


import com.google.gson.annotations.SerializedName

data class AuthResponseData(
    @SerializedName("birth_date")
    val birthDate: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("phone_number")
    val phoneNumber: String?
)