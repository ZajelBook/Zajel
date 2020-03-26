package com.bernovia.zajel.bookList.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.bernovia.zajel.api.ApiServicesRx
import com.bernovia.zajel.bookList.models.Book
import com.bernovia.zajel.helpers.apiCallsHelpers.liveData
import com.bernovia.zajel.helpers.paginationUtils.GenericBoundaryCallback
import com.bernovia.zajel.helpers.paginationUtils.Listing
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

interface BooksRepository {


    companion object {
        const val SIZE_PAGE = 30
    }

    fun getListable(): Listing<Book>
    fun updateRequested(bookId: Int, value: Boolean)

    fun getBookById(bookId: Int): LiveData<Book>

    open class BooksRepositoryImpl(
        private val service: ApiServicesRx, private val dao: BookDao) : BooksRepository {


        val bc: GenericBoundaryCallback<Book> by lazy {
            GenericBoundaryCallback({ dao.deleteAllBooksList() }, { bookList(it) }, { insertAllBooksList(it) })
        }


        override fun getListable(): Listing<Book> {
            return object : Listing<Book> {


                override fun getBoundaryCallback(): LiveData<GenericBoundaryCallback<Book>> {
                    return liveData(bc)

                }

                override fun getDataSource(): LiveData<PagedList<Book>> {
                    return dao.allBooksPaginated().map { it }.toLiveData(pageSize = SIZE_PAGE, boundaryCallback = bc)
                }


            }

        }

       override fun updateRequested(bookId: Int, value: Boolean) {
            dao.updateRequested(bookId, value).subscribeOn(Schedulers.io()).subscribe()

        }

        fun insertAllBooksList(list: List<Book>): Completable {
            return dao.insertAllBooksList(list.map { it })
        }

        override fun getBookById(bookId: Int): LiveData<Book> {
            return dao.getBookById(bookId)
        }


        fun bookList(page: Int): Single<List<Book>> {
            return service.bookList(SIZE_PAGE, page).map { it }
        }

    }
}