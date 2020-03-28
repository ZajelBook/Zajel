package com.bernovia.zajel.messages.sendMessage


import com.google.gson.annotations.SerializedName

data class SendMessageRequestBody(
    @SerializedName("content")
    val content: String?
)