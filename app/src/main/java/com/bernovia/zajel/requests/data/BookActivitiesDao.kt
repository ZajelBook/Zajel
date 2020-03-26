package com.bernovia.zajel.requests.data

import androidx.paging.DataSource
import androidx.room.*
import com.bernovia.zajel.requests.models.BookActivity
import io.reactivex.Completable

@Dao interface BookActivitiesDao {


    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertAllBookActivitiesList(list: List<BookActivity?>): Completable

    @Query("SELECT * FROM book_activity where id_borrower == :userId") fun getAllSendRequestsPaginated(userId: Int): DataSource.Factory<Int, BookActivity>

    @Query("SELECT * FROM book_activity where id_lender == :userId") fun getAllReceivedRequestsPaginated(userId: Int): DataSource.Factory<Int, BookActivity>


    @Query("DELETE FROM book_activity where id_lender == :userId") fun deleteAllReceivedRequestsList(userId: Int): Completable

    @Query("DELETE FROM book_activity where id_borrower == :userId") fun deleteAllSentRequestsList(userId: Int): Completable


    @Query("UPDATE book_activity SET status_book_activity = :value WHERE id_book_activity = :bookActivityId") fun updateStatus(bookActivityId: Int, value: String): Completable


    @Delete fun deleteBookActivity(item: BookActivity): Completable

}