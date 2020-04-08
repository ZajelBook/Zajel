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


    // my books list
    private val listingMyBooks: LiveData<Listing<Book>> by lazy {
        liveData(booksRepository.getListableMyBooks())
    }
    val boundaryCallbackMyBooks = Transformations.switchMap(listingMyBooks) { it.getBoundaryCallback() }
    val dataSourceMyBooks = Transformations.switchMap(listingMyBooks) { it.getDataSource() }
    val networkStateMyBooks = Transformations.switchMap(listingMyBooks) { it.getNetworkState() }
    fun refreshPageMyBooks(): LiveData<GenericBoundaryCallback<Book>> {
        return Transformations.switchMap(listingMyBooks) { it.getBoundaryCallback() }
    }


    // all books list
    private val listing: LiveData<Listing<Book>> by lazy {
        liveData(booksRepository.getListable())
    }
    val boundaryCallback = Transformations.switchMap(listing) { it.getBoundaryCallback() }
    val dataSource = Transformations.switchMap(listing) { it.getDataSource() }
    val networkState = Transformations.switchMap(listing) { it.getNetworkState() }

    fun refreshPage(): LiveData<GenericBoundaryCallback<Book>> {
        return Transformations.switchMap(listing) { it.getBoundaryCallback() }
    }


    // get books details
    fun getBookById(bookId: Int): LiveData<Book> {
        return booksRepository.getBookById(bookId)
    }

    fun getBookAndInsertInLocal(bookId: Int) {
        return booksRepository.getBookAndInsertInLocal(bookId)
    }

    fun updateBook(book: Book) {
        booksRepository.updateBook(book)
    }

    fun updateRequested(bookId: Int, value: Boolean) {
        booksRepository.updateRequested(bookId, value)
    }


    override fun onCleared() {
        booksRepository.cleared()
        boundaryCallback.value?.cleared()
        boundaryCallbackMyBooks.value?.cleared()
    }
}