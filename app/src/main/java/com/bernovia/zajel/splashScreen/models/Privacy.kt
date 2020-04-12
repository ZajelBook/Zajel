package com.bernovia.zajel.splashScreen.models

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Privacy(
    @ColumnInfo(name = "content_privacy")  @SerializedName("content") val content: String?)