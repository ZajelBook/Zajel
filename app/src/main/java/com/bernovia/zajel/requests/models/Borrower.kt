package com.bernovia.zajel.requests.models


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Borrower(
    @ColumnInfo(name = "id_borrower") @SerializedName("id") val id: Int?,
    @ColumnInfo(name = "name_borrower") @SerializedName("name") val name: String?)