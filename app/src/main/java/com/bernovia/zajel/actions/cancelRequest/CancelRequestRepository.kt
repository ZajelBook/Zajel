package com.bernovia.zajel.actions.cancelRequest

import androidx.lifecycle.LiveData
import com.bernovia.zajel.actions.ActionsResponseBody
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.helpers.base.BaseRepositoryWithBody

class CancelRequestRepository(service: ApiServicesCoRoutines) : BaseRepositoryWithBody<ActionsResponseBody, Int>(service, true) {
    override fun loadData(body: Int): LiveData<Any> {
        return fetchData {
            service.cancelRequestAsync(body)
        }
    }
}