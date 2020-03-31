package com.bernovia.zajel.actions.logout

import androidx.lifecycle.LiveData
import com.bernovia.zajel.actions.ActionsResponseBody
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.helpers.base.BaseRepository

class LogoutRepository(service: ApiServicesCoRoutines) : BaseRepository<ActionsResponseBody>(service) {
    override fun loadData(): LiveData<Any> {
        return fetchData {
            service.logout()
        }
    }
}