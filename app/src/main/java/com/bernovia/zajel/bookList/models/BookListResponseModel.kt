package com.bernovia.zajel.bookList.models


import com.google.gson.annotations.SerializedName

data class BookListResponseModel<T>(
    @SerializedName("books")
    val books: T,
    @SerializedName("metadata")
    val metadata: MetadataList
)