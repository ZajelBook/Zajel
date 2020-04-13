package com.bernovia.zajel.auth.activateEmail

import androidx.lifecycle.LiveData
import com.bernovia.zajel.actions.ActionsResponseBody
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.helpers.base.BaseRepositoryWithBody

class ActivateEmailRepository(service: ApiServicesCoRoutines) :
    BaseRepositoryWithBody<ActionsResponseBody, ActivateEmailRequestBody>(service, true) {
    override fun loadData(body: ActivateEmailRequestBody): LiveData<Any> {
        return fetchData {
            service.activateEmailAsync(body)
        }
    }
}