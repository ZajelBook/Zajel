package com.bernovia.zajel.notificationsList.models


import com.bernovia.zajel.bookList.models.MetadataList
import com.google.gson.annotations.SerializedName

data class NotificationListResponseModel<T>(
    @SerializedName("metadata")
    val metadata: MetadataList,
    @SerializedName("notifications")
    val notifications: T
)