package com.bernovia.zajel.bookList.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bernovia.zajel.bookList.data.BooksRepository
import com.bernovia.zajel.bookList.models.Book
import com.bernovia.zajel.helpers.apiCallsHelpers.liveData
import com.bernovia.zajel.helpers.paginationUtils.GenericBoundaryCallback
import com.bernovia.zajel.helpers.paginationUtils.Listing

class BooksListViewModel(
    private val booksRepository: BooksRepository) : ViewModel() {


    private val listing: LiveData<Listing<Book>> by lazy {
        liveData(booksRepository.getListable())
    }


    val boundaryCallback = Transformations.switchMap(listing) { it.getBoundaryCallback() }
    val dataSource = Transformations.switchMap(listing) { it.getDataSource() }
    val networkState = Transformations.switchMap(listing) { it.getNetworkState() }

    fun refreshPage(): LiveData<GenericBoundaryCallback<Book>> {
        return Transformations.switchMap(listing) { it.getBoundaryCallback() }
    }

    fun getBookById(bookId: Int): LiveData<Book> {
        return booksRepository.getBookById(bookId)
    }

    fun updateRequested(bookId: Int, value: Boolean) {
        booksRepository.updateRequested(bookId, value)
    }


    override fun onCleared() {
        boundaryCallback.value?.cleared()
    }
}