package com.bernovia.zajel.messages.data

import androidx.paging.DataSource
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.bernovia.zajel.messages.models.Message
import io.reactivex.Completable

interface MessagesDao {


    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertAllMessages(list: List<Message?>): Completable

    @Query("SELECT * FROM message where conversation_id_message ==:conversationId") fun allMessagesPaginated(conversationId: Int): DataSource.Factory<Int, Message>

    @Query("DELETE FROM message where conversation_id_message ==:conversationId") fun deleteAllMessagesById(conversationId: Int): Completable


}