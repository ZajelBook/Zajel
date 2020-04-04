package com.bernovia.zajel.notificationsList.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bernovia.zajel.helpers.apiCallsHelpers.liveData
import com.bernovia.zajel.helpers.paginationUtils.GenericBoundaryCallback
import com.bernovia.zajel.helpers.paginationUtils.Listing
import com.bernovia.zajel.notificationsList.data.NotificationsListRepository
import com.bernovia.zajel.notificationsList.models.Notification

class NotificationsListViewModel(
    private val notificationsListRepository: NotificationsListRepository) : ViewModel() {


    private val listing: LiveData<Listing<Notification>> by lazy {
        liveData(notificationsListRepository.getListable())
    }
    val boundaryCallback = Transformations.switchMap(listing) { it.getBoundaryCallback() }
    val dataSource = Transformations.switchMap(listing) { it.getDataSource() }
    val networkState = Transformations.switchMap(listing) { it.getNetworkState() }

    fun refreshPage(): LiveData<GenericBoundaryCallback<Notification>> {
        return Transformations.switchMap(listing) { it.getBoundaryCallback() }
    }

    override fun onCleared() {
        boundaryCallback.value?.cleared()
    }

}