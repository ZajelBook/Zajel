package com.bernovia.zajel.requests.models


import com.bernovia.zajel.bookList.models.MetadataList
import com.google.gson.annotations.SerializedName

data class RequestsResponseModel<T>(
    @SerializedName("book_activities")
    val bookActivities: T,
    @SerializedName("metadata")
    val metadata: MetadataList
)