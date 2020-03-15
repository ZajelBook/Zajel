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
    private val searchRepository: BooksRepository) : ViewModel() {


    private val listing: LiveData<Listing<Book>> by lazy {
        liveData(searchRepository.getListable())
    }


    val boundaryCallback = Transformations.switchMap(listing) { it.getBoundaryCallback() }
    val dataSource = Transformations.switchMap(listing) { it.getDataSource() }
    val networkState = Transformations.switchMap(listing) { it.getNetworkState() }

    fun refreshPage(): LiveData<GenericBoundaryCallback<Book>> {
        return Transformations.switchMap(listing) { it.getBoundaryCallback() }
    }


    /**
     * Cleared all references and petitions boundary callback
     */
    override fun onCleared() {
        boundaryCallback.value?.cleared()
    }
}