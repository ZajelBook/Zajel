package com.bernovia.zajel.notificationsList.models


import com.google.gson.annotations.SerializedName

data class Payload(
    @SerializedName("subject")
    val subject: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("conversation_id")
    val conversationId: Int?
)