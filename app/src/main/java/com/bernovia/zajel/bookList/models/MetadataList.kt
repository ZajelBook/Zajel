package com.bernovia.zajel.bookList.models


import com.google.gson.annotations.SerializedName

data class MetadataList(
    @SerializedName("current_page")
    val currentPage: Int?,
    @SerializedName("per_page")
    val perPage: Int?,
    @SerializedName("total_pages")
    val totalPages: Int?
)