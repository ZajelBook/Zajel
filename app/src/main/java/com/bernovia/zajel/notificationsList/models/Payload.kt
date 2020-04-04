package com.bernovia.zajel.notificationsList.models


import com.google.gson.annotations.SerializedName

data class Payload(
    @SerializedName("subject")
    val subject: String,
    @SerializedName("title")
    val title: String
)