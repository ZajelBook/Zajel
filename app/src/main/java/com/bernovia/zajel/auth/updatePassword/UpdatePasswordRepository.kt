package com.bernovia.zajel.auth.updatePassword

import androidx.lifecycle.LiveData
import com.bernovia.zajel.actions.ActionsResponseBody
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.helpers.base.BaseRepositoryWithBody

class UpdatePasswordRepository(service: ApiServicesCoRoutines) : BaseRepositoryWithBody<ActionsResponseBody, UpdatePasswordRequestBody>(service, true) {
    override fun loadData(body: UpdatePasswordRequestBody): LiveData<Any> {
        return fetchData {
            service.updatePasswordAsync(body)
        }
    }
}