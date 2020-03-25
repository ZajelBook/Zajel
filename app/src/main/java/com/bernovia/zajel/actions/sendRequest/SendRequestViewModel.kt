package com.bernovia.zajel.actions.sendRequest

import androidx.lifecycle.LiveData
import com.bernovia.zajel.actions.ActionsResponseBody
import com.bernovia.zajel.actions.SendRequestRequestBody
import com.bernovia.zajel.helpers.base.BaseViewModelWithBody
import retrofit2.Response


@Suppress("UNCHECKED_CAST") class SendRequestViewModel(private val repository: SendRequestRepository) : BaseViewModelWithBody<ActionsResponseBody, SendRequestRequestBody>() {
    private lateinit var responseBody: LiveData<Response<ActionsResponseBody>>
    override fun getDataFromRetrofit(body: SendRequestRequestBody): LiveData<Response<ActionsResponseBody>> {
        responseBody = repository.loadData(body) as LiveData<Response<ActionsResponseBody>>
        return responseBody
    }
}