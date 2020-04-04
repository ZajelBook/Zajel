package com.bernovia.zajel.requests.models


import androidx.room.*
import com.bernovia.zajel.bookList.models.Book
import com.google.gson.annotations.SerializedName

@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED, RoomWarnings.INDEX_FROM_EMBEDDED_ENTITY_IS_DROPPED)
@Entity(tableName = "book_activity", indices = [Index("id_book_activity")]) data class BookActivity(
    @PrimaryKey @ColumnInfo(name = "id_book_activity") @SerializedName("id") val id: Int,
    @Embedded @SerializedName("book") val book: Book?,
    @ColumnInfo(name = "created_at_book_activity") @SerializedName("created_at") val createdAt: String,
    @Embedded @SerializedName("borrower") val borrower: Borrower?,
    @Embedded @SerializedName("lender") val lender: Lender?,
    @ColumnInfo(name = "conversation_id_book_activity") @SerializedName("conversation_id") val conversationId: Int?,
    @ColumnInfo(name = "status_book_activity") @SerializedName("status") val status: String?)