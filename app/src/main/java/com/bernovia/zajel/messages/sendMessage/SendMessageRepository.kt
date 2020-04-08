package com.bernovia.zajel.messages.sendMessage

import androidx.lifecycle.LiveData
import com.bernovia.zajel.actions.ActionsResponseBody
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.helpers.base.BaseRepositoryWithBody

class SendMessageRepository(service: ApiServicesCoRoutines) : BaseRepositoryWithBody<ActionsResponseBody, SendMessageRequestBody>(service, false) {
    var conversationId: Int = 0
    override fun loadData(body: SendMessageRequestBody): LiveData<Any> {
        return fetchData {
            service.sendMessageAsync(body, conversationId)
        }
    }
}