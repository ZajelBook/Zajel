package com.bernovia.zajel.messages.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bernovia.zajel.helpers.apiCallsHelpers.liveData
import com.bernovia.zajel.helpers.paginationUtils.GenericBoundaryCallback
import com.bernovia.zajel.helpers.paginationUtils.Listing
import com.bernovia.zajel.messages.data.MessagesRepository
import com.bernovia.zajel.messages.models.Message

class MessagesListViewModel(
    private val messagesRepository: MessagesRepository) : ViewModel() {


    private val listing: LiveData<Listing<Message?>> by lazy {
        liveData(messagesRepository.getListable())
    }


    val boundaryCallback = Transformations.switchMap(listing) { it.getBoundaryCallback() }
    val dataSource = Transformations.switchMap(listing) { it.getDataSource() }
    val networkState = Transformations.switchMap(listing) { it.getNetworkState() }

    fun refreshPage(): LiveData<GenericBoundaryCallback<Message?>> {
        return Transformations.switchMap(listing) { it.getBoundaryCallback() }
    }


    fun setConversationId(conversationIdId: Int) {
        messagesRepository.setConversationId(conversationIdId)
    }

    fun insertMessage(message: Message) {
        messagesRepository.insertMessage(message)
    }


    override fun onCleared() {
        boundaryCallback.value?.cleared()
    }
}