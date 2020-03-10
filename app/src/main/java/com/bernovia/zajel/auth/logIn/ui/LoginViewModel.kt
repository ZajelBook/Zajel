package com.bernovia.zajel.auth.logIn.ui

import androidx.lifecycle.LiveData
import com.bernovia.zajel.auth.authResponseModels.AuthResponseBody
import com.bernovia.zajel.auth.authResponseModels.AuthResponseData
import com.bernovia.zajel.auth.logIn.data.LoginRepository
import com.bernovia.zajel.auth.logIn.models.LoginRequestBody
import com.bernovia.zajel.helpers.base.BaseViewModelWithBody
import retrofit2.Response

@Suppress("UNCHECKED_CAST")
class LoginViewModel(private val repository: LoginRepository) :
    BaseViewModelWithBody<AuthResponseBody<AuthResponseData>, LoginRequestBody>() {
    private lateinit var responseBody: LiveData<Response<AuthResponseBody<AuthResponseData>>>

    override fun getDataFromRetrofit(body: LoginRequestBody): LiveData<Response<AuthResponseBody<AuthResponseData>>> {
        responseBody =
            repository.loadData(body) as LiveData<Response<AuthResponseBody<AuthResponseData>>>

        return responseBody
    }
}