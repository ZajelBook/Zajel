package com.bernovia.zajel.messages.sendMessage

import androidx.lifecycle.LiveData
import com.bernovia.zajel.actions.ActionsResponseBody
import com.bernovia.zajel.helpers.base.BaseViewModelWithBody
import retrofit2.Response


@Suppress("UNCHECKED_CAST") class SendMessageViewModel(private val repository: SendMessageRepository) : BaseViewModelWithBody<ActionsResponseBody, SendMessageRequestBody>() {
    private lateinit var responseBody: LiveData<Response<ActionsResponseBody>>

    fun setConversationId(conversationId: Int) {
        repository.conversationId = conversationId
    }

    override fun getDataFromRetrofit(body: SendMessageRequestBody): LiveData<Response<ActionsResponseBody>> {
        responseBody = repository.loadData(body) as LiveData<Response<ActionsResponseBody>>

        return responseBody
    }
}