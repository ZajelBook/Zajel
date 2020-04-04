package com.bernovia.zajel.editProfile.models


import com.google.gson.annotations.SerializedName

data class GetProfileResponseBody(
    @SerializedName("birth_date")
    val birthDate: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("phone_number")
    val phoneNumber: String?
)