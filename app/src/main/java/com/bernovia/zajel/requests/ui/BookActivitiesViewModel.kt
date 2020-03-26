package com.bernovia.zajel.requests.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bernovia.zajel.helpers.apiCallsHelpers.liveData
import com.bernovia.zajel.helpers.paginationUtils.GenericBoundaryCallback
import com.bernovia.zajel.helpers.paginationUtils.Listing
import com.bernovia.zajel.requests.data.BookActivityRepository
import com.bernovia.zajel.requests.models.BookActivity

class BookActivitiesViewModel(
    private val bookActivityRepository: BookActivityRepository) : ViewModel() {


    private val sentRequestsListing: LiveData<Listing<BookActivity?>> by lazy {
        liveData(bookActivityRepository.getSentRequestsListable())
    }

    private val receivedRequestsListing: LiveData<Listing<BookActivity?>> by lazy {
        liveData(bookActivityRepository.getReceiveRequestsListable())
    }


    val sentRequestsBoundaryCallback = Transformations.switchMap(sentRequestsListing) { it.getBoundaryCallback() }
    val sentRequestsDataSource = Transformations.switchMap(sentRequestsListing) { it.getDataSource() }
    val sentRequestsNetworkState = Transformations.switchMap(sentRequestsListing) { it.getNetworkState() }

    val receivedRequestsBoundaryCallback = Transformations.switchMap(receivedRequestsListing) { it.getBoundaryCallback() }
    val receivedRequestsDataSource = Transformations.switchMap(receivedRequestsListing) { it.getDataSource() }
    val receivedRequestsNetworkState = Transformations.switchMap(receivedRequestsListing) { it.getNetworkState() }


    fun refreshPageSendRequests(): LiveData<GenericBoundaryCallback<BookActivity?>> {
        return Transformations.switchMap(sentRequestsListing) { it.getBoundaryCallback() }
    }

    fun refreshPageReceivedRequests(): LiveData<GenericBoundaryCallback<BookActivity?>> {
        return Transformations.switchMap(receivedRequestsListing) { it.getBoundaryCallback() }
    }


    fun updateBookActivityStatus(bookActivityId: Int, value: String) {
        bookActivityRepository.updateBookActivityStatus(bookActivityId, value)
    }

    fun deleteBookActivity(item: BookActivity) {
        bookActivityRepository.deleteBookActivity(item)
    }


    override fun onCleared() {
        sentRequestsBoundaryCallback.value?.cleared()
        receivedRequestsBoundaryCallback.value?.cleared()
    }
}