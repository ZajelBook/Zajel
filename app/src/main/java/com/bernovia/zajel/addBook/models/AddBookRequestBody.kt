package com.bernovia.zajel.addBook.models


import com.google.gson.annotations.SerializedName

data class AddBookRequestBody(
    @SerializedName("author")
    val author: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("genre_id")
    val genreId: Int?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("page_count")
    val pageCount: Int?,
    @SerializedName("published_at")
    val publishedAt: String?,
    @SerializedName("title")
    val title: String?
)