package com.bernovia.zajel.splashScreen.models

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class About(
    @ColumnInfo(name = "content_about")  @SerializedName("content") val content: String?)