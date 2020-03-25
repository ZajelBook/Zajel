package com.bernovia.zajel.actions


import com.google.gson.annotations.SerializedName

data class SendRequestRequestBody(
    @SerializedName("book_id")
    val bookId: Int?
)