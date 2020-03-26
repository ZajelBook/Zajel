package com.bernovia.zajel.bookList.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.bernovia.zajel.bookList.models.Book
import io.reactivex.Completable


@Dao interface BookDao {


    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertAllBooksList(list: List<Book>): Completable

    @Query("SELECT * FROM book") fun allBooksPaginated(): DataSource.Factory<Int, Book>

    @Query("DELETE FROM book") fun deleteAllBooksList(): Completable


    @Query("SELECT * FROM book WHERE id_book= :bookId") fun getBookById(bookId: Int): LiveData<Book>


    @Query("UPDATE book SET requested_book = :value WHERE id_book = :bookId") fun updateRequested(bookId: Int, value: Boolean): Completable

}