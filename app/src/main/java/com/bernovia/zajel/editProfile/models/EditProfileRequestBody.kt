package com.bernovia.zajel.editProfile.models


import com.google.gson.annotations.SerializedName

data class EditProfileRequestBody(
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("fcm_token")
    val fcmToken: String?

)