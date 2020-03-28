package com.bernovia.zajel.messages.models


import com.google.gson.annotations.SerializedName

data class MessagesListResponseBody<T>(
    @SerializedName("messages")
    val messages: T
)