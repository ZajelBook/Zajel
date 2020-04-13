package com.bernovia.zajel.auth.activateEmail

import androidx.lifecycle.LiveData
import com.bernovia.zajel.actions.ActionsResponseBody
import com.bernovia.zajel.helpers.base.BaseViewModelWithBody
import retrofit2.Response


@Suppress("UNCHECKED_CAST")
class ActivateEmailViewModel(private val repository: ActivateEmailRepository) :
    BaseViewModelWithBody<ActionsResponseBody, ActivateEmailRequestBody>() {
    private lateinit var responseBody: LiveData<Response<ActionsResponseBody>>

    override fun getDataFromRetrofit(body: ActivateEmailRequestBody): LiveData<Response<ActionsResponseBody>> {
        responseBody =
            repository.loadData(body) as LiveData<Response<ActionsResponseBody>>
        return responseBody
    }
}