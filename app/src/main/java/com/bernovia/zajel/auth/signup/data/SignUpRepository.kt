package com.bernovia.zajel.auth.signup.data

import androidx.lifecycle.LiveData
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.auth.authResponseModels.AuthResponseBody
import com.bernovia.zajel.auth.authResponseModels.AuthResponseData
import com.bernovia.zajel.auth.signup.models.SignUpRequestBody
import com.bernovia.zajel.helpers.base.BaseRepositoryWithBody

class SignUpRepository(service: ApiServicesCoRoutines) :
    BaseRepositoryWithBody<AuthResponseBody<AuthResponseData>, SignUpRequestBody>(service, true) {
    override fun loadData(body: SignUpRequestBody): LiveData<Any> {
        return fetchData {
            service.signUp(body)
        }
    }
}