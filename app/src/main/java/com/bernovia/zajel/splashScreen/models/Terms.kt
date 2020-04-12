package com.bernovia.zajel.splashScreen.models

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Terms (
    @ColumnInfo(name = "content_terms")  @SerializedName("content") val content: String?)