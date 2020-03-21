package com.bernovia.zajel.bookList.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.bernovia.zajel.bookList.models.Book
import io.reactivex.Completable
import io.reactivex.Single


@Dao interface BookDao {

    @Query("SELECT * FROM book") fun booksList(): Single<List<Book>>

    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertAllBooksList(list: List<Book>): Completable

    @Query("SELECT * FROM book  ") fun allBooksPaginated(): DataSource.Factory<Int, Book>

    @Query("DELETE FROM book") fun deleteAllBooksList(): Completable


    @Query("SELECT * FROM book WHERE id_book= :bookId") fun getBookById(bookId: Int): LiveData<Book>


}