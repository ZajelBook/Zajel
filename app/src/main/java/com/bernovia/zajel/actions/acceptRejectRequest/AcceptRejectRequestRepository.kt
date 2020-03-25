package com.bernovia.zajel.actions.acceptRejectRequest

import androidx.lifecycle.LiveData
import com.bernovia.zajel.actions.ActionsResponseBody
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.helpers.base.BaseRepositoryWithBody

class AcceptRejectRequestRepository(service: ApiServicesCoRoutines) : BaseRepositoryWithBody<ActionsResponseBody, Int>(service, true) {
    lateinit var type: String
    override fun loadData(body: Int): LiveData<Any> {
        return fetchData {
            service.acceptRejectRequestAsync(body, type)
        }
    }
}