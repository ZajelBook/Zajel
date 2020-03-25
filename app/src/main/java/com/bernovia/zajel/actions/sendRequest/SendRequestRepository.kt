package com.bernovia.zajel.actions.sendRequest

import androidx.lifecycle.LiveData
import com.bernovia.zajel.actions.ActionsResponseBody
import com.bernovia.zajel.actions.SendRequestRequestBody
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.helpers.base.BaseRepositoryWithBody

class SendRequestRepository(service: ApiServicesCoRoutines) : BaseRepositoryWithBody<ActionsResponseBody, SendRequestRequestBody>(service, true) {
    override fun loadData(body: SendRequestRequestBody): LiveData<Any> {
        return fetchData {
            service.sendRequestAsync(body)
        }
    }
}