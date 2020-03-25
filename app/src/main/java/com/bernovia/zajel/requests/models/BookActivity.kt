package com.bernovia.zajel.requests.models


import androidx.room.*
import com.bernovia.zajel.bookList.models.Book
import com.google.gson.annotations.SerializedName


@Entity(tableName = "book_activity", indices = [Index("id_book_activity")]) data class BookActivity(
    @PrimaryKey @ColumnInfo(name = "id_book_activity") @SerializedName("id") val id: Int?,
    @Embedded @SerializedName("book") val book: Book?,
    @Embedded @SerializedName("borrower") val borrower: Borrower?,
    @Embedded @SerializedName("lender") val lender: Lender?,
    @ColumnInfo(name = "status_book_activity") @SerializedName("status") val status: String?)