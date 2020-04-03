package com.bernovia.zajel.notificationsList


import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("content")
    val content: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("payload")
    val payload: Payload
)