package com.bernovia.zajel.bookList.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "book", indices = [Index("id_book")]) data class Book(
    @PrimaryKey @ColumnInfo(name = "id_book") @SerializedName("id") val id: Int,
    @ColumnInfo(name = "approved_book") @SerializedName("approved") val approved: Boolean?,
    @ColumnInfo(name = "author_book") @SerializedName("author") val author: String?,
    @ColumnInfo(name = "description_book") @SerializedName("description") val description: String?,
    @ColumnInfo(name = "image_book") @SerializedName("image") val image: String?,
    @ColumnInfo(name = "language_book") @SerializedName("language") val language: String?,
    @ColumnInfo(name = "page_count_book") @SerializedName("page_count") val pageCount: Int?,
    @ColumnInfo(name = "published_at_book") @SerializedName("published_at") val publishedAt: String?,
    @ColumnInfo(name = "status_book") @SerializedName("status") val status: String?,
    @ColumnInfo(name = "title_book") @SerializedName("title") val title: String?,
    @ColumnInfo(name = "genre_book") @SerializedName("genre") val genre: String?,
    @ColumnInfo(name = "owner_id_book") @SerializedName("owner_id") val userId: Int,
    @ColumnInfo(name = "owner_type_book")  @SerializedName("owner_type") val ownerType: String?,
    @ColumnInfo(name = "requested_book") @SerializedName("requested") val requested: Boolean


    )