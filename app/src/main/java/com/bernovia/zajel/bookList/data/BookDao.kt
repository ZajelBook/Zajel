package com.bernovia.zajel.bookList.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.bernovia.zajel.bookList.models.Book
import io.reactivex.Completable


@Dao interface BookDao {


    @Transaction @Insert(onConflict = REPLACE) fun insertAllBooksList(list: List<Book>): Completable
    @Transaction @Insert(onConflict = REPLACE) fun insertBook(book: Book): Completable


    @Query("SELECT * FROM book") fun allBooksPaginated(): DataSource.Factory<Int, Book>

    @Query("DELETE FROM book") fun deleteAllBooksList(): Completable

    @Query("DELETE FROM book where owner_id_book =:ownerId") fun deleteMyBooksList(ownerId: Int): Completable
    @Query("SELECT * FROM book where owner_id_book =:ownerId") fun allMyBooksPaginated(ownerId: Int): DataSource.Factory<Int, Book>


    @Query("SELECT * FROM book WHERE id_book= :bookId") fun getBookById(bookId: Int): LiveData<Book>


    @Update(onConflict = REPLACE) fun updateBook(book: Book): Completable

    @Query("UPDATE book SET requested_book = :value WHERE id_book = :bookId") fun updateRequested(bookId: Int, value: Boolean): Completable

}