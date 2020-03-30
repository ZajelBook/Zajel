package com.bernovia.zajel.messages.data

import androidx.paging.DataSource
import androidx.room.*
import com.bernovia.zajel.messages.models.Message
import io.reactivex.Completable


@Dao
interface MessagesDao {

    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertAllMessages(list: List<Message?>): Completable

    @Query("SELECT * FROM message WHERE conversation_id_message ==:conversationId") fun allMessagesPaginated(conversationId: Int): DataSource.Factory<Int, Message>

    @Query("DELETE FROM message WHERE conversation_id_message ==:conversationId") fun deleteAllMessagesById(conversationId: Int): Completable


}