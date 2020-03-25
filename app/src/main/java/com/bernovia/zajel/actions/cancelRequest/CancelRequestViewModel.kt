package com.bernovia.zajel.actions.cancelRequest

import androidx.lifecycle.LiveData
import com.bernovia.zajel.actions.ActionsResponseBody
import com.bernovia.zajel.helpers.base.BaseViewModelWithBody
import retrofit2.Response


@Suppress("UNCHECKED_CAST") class CancelRequestViewModel(private val repository: CancelRequestRepository) : BaseViewModelWithBody<ActionsResponseBody, Int>() {
    private lateinit var responseBody: LiveData<Response<ActionsResponseBody>>

    override fun getDataFromRetrofit(body: Int): LiveData<Response<ActionsResponseBody>> {
        responseBody = repository.loadData(body) as LiveData<Response<ActionsResponseBody>>

        return responseBody
    }
}