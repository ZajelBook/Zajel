package com.bernovia.zajel.bookList.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.bernovia.zajel.api.ApiServicesRx
import com.bernovia.zajel.bookList.models.Book
import com.bernovia.zajel.helpers.ZajelUtil.preferenceManager
import com.bernovia.zajel.helpers.apiCallsHelpers.liveData
import com.bernovia.zajel.helpers.paginationUtils.GenericBoundaryCallback
import com.bernovia.zajel.helpers.paginationUtils.Listing
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

interface BooksRepository {


    companion object {
        const val SIZE_PAGE = 30
    }

    fun getListable(): Listing<Book>
    fun getListableMyBooks(): Listing<Book>

    fun updateRequested(bookId: Int, value: Boolean)

    fun getBookById(bookId: Int): LiveData<Book>

    fun getBookAndInsertInLocal(bookId: Int)
    fun cleared()
    fun updateBook(book: Book)


    open class BooksRepositoryImpl(
        private val service: ApiServicesRx, private val dao: BookDao
    ) : BooksRepository {


        private val compositeDisposable: CompositeDisposable = CompositeDisposable()

        val bc: GenericBoundaryCallback<Book> by lazy {
            GenericBoundaryCallback(
                { dao.deleteAllBooksList() },
                { bookList(it) },
                { insertAllBooksList(it) },
                false
            )
        }

        val bcMyBooks: GenericBoundaryCallback<Book> by lazy {
            GenericBoundaryCallback(
                { dao.deleteMyBooksList(preferenceManager.userId) },
                { myBookList(it) },
                { insertAllBooksList(it) },
                false
            )
        }

        override fun getListableMyBooks(): Listing<Book> {
            return object : Listing<Book> {

                override fun getBoundaryCallback(): LiveData<GenericBoundaryCallback<Book>> {
                    return liveData(bcMyBooks)

                }

                override fun getDataSource(): LiveData<PagedList<Book>> {
                    return dao.allMyBooksPaginated(preferenceManager.userId).map { it }
                        .toLiveData(pageSize = SIZE_PAGE, boundaryCallback = bcMyBooks)
                }

            }
        }


        override fun getListable(): Listing<Book> {
            return object : Listing<Book> {


                override fun getBoundaryCallback(): LiveData<GenericBoundaryCallback<Book>> {
                    return liveData(bc)

                }

                override fun getDataSource(): LiveData<PagedList<Book>> {
                    return dao.allBooksPaginated().map { it }
                        .toLiveData(pageSize = SIZE_PAGE, boundaryCallback = bc)
                }

            }
        }

        override fun getBookAndInsertInLocal(bookId: Int) {
            book(bookId).subscribeOn(Schedulers.io()).flatMapCompletable {
                insertBookInLocal(it)
            }.subscribeBy(onComplete = {}, onError = {}).addTo(compositeDisposable)
        }

        override fun cleared() {
            compositeDisposable.clear()
        }

        override fun updateBook(book: Book) {
            dao.updateBook(book).subscribeOn(Schedulers.io()).subscribe()
        }


        override fun updateRequested(bookId: Int, value: Boolean) {
            dao.updateRequested(bookId, value).subscribeOn(Schedulers.io()).subscribe()

        }

        private fun insertAllBooksList(list: List<Book>): Completable {
            return dao.insertAllBooksList(list)
        }

        fun insertBookInLocal(book: Book): Completable {
            return dao.insertBook(book)
        }


        override fun getBookById(bookId: Int): LiveData<Book> {
            return dao.getBookById(bookId)
        }

        fun book(bookId: Int): Single<Book> {
            return service.book(bookId).map { it }
        }


        fun myBookList(page: Int): Single<List<Book>> {
            return service.myBookList(SIZE_PAGE, page).map { it }
        }


        fun bookList(page: Int): Single<List<Book>> {
            return service.bookList(
                SIZE_PAGE, page,
                preferenceManager.latitude.toDouble(), preferenceManager.longitude.toDouble()
            ).map { it }
        }

    }
}