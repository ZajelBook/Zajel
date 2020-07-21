package com.bernovia.zajel.auth.activateEmail

import androidx.lifecycle.LiveData
import com.bernovia.zajel.actions.ActionsResponseBody
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.helpers.base.BaseRepository

class ResendEmailRepository(service: ApiServicesCoRoutines) :
    BaseRepository<ActionsResponseBody>(service) {
    override fun loadData(): LiveData<Any> {
        return fetchData {
            service.resendEmailAsyn()
        }
    }
}