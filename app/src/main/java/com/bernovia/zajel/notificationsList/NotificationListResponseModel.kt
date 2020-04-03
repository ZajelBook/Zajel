package com.bernovia.zajel.notificationsList


import com.bernovia.zajel.bookList.models.MetadataList
import com.google.gson.annotations.SerializedName

data class NotificationListResponseModel(
    @SerializedName("metadata")
    val metadata: MetadataList,
    @SerializedName("notifications")
    val notifications: List<Notification>
)