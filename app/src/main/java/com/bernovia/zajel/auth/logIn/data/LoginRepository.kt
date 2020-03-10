package com.bernovia.zajel.auth.logIn.data

import androidx.lifecycle.LiveData
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.auth.authResponseModels.AuthResponseBody
import com.bernovia.zajel.auth.authResponseModels.AuthResponseData
import com.bernovia.zajel.auth.logIn.models.LoginRequestBody
import com.bernovia.zajel.helpers.base.BaseRepositoryWithBody

class LoginRepository(service: ApiServicesCoRoutines) :
    BaseRepositoryWithBody<AuthResponseBody<AuthResponseData>, LoginRequestBody>(service, true) {
    override fun loadData(body: LoginRequestBody): LiveData<Any> {
        return fetchData {
            service.login(body)
        }
    }
}