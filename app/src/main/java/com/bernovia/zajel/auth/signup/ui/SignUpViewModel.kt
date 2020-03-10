package com.bernovia.zajel.auth.signup.ui

import androidx.lifecycle.LiveData
import com.bernovia.zajel.auth.authResponseModels.AuthResponseBody
import com.bernovia.zajel.auth.authResponseModels.AuthResponseData
import com.bernovia.zajel.auth.signup.data.SignUpRepository
import com.bernovia.zajel.auth.signup.models.SignUpRequestBody
import com.bernovia.zajel.helpers.base.BaseViewModelWithBody
import retrofit2.Response

@Suppress("UNCHECKED_CAST")
class SignUpViewModel(private val repository: SignUpRepository) :
    BaseViewModelWithBody<AuthResponseBody<AuthResponseData>, SignUpRequestBody>() {
    private lateinit var responseBody: LiveData<Response<AuthResponseBody<AuthResponseData>>>

    override fun getDataFromRetrofit(body: SignUpRequestBody): LiveData<Response<AuthResponseBody<AuthResponseData>>> {
        responseBody =
            repository.loadData(body) as LiveData<Response<AuthResponseBody<AuthResponseData>>>

        return responseBody
    }
}