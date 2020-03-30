package com.bernovia.zajel.messages.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "message", indices = [Index("id_message")]) data class Message(
    @PrimaryKey @ColumnInfo(name = "id_message") @SerializedName("id") val id: Int?,
    @ColumnInfo(name = "content_message") @SerializedName("content") val content: String?,
    @ColumnInfo(name = "conversation_id_message") @SerializedName("conversation_id") val conversationId: Int?,
    @ColumnInfo(name = "sender_id_message") @SerializedName("sender_id") val senderId: Int,
    @ColumnInfo(name = "sender_name_message") @SerializedName("sender_name") val senderName: String?,
    @ColumnInfo(name = "created_at_message") @SerializedName("created_at") val createdAt: String?,
    @ColumnInfo(name = "sender_type_message") @SerializedName("sender_type") val senderType: String?)