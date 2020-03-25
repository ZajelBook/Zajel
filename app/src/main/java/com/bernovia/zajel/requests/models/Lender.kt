package com.bernovia.zajel.requests.models


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Lender(
    @ColumnInfo(name = "id_lender")  @SerializedName("id") val id: Int?,
    @ColumnInfo(name = "name_lender") @SerializedName("name") val name: String?)