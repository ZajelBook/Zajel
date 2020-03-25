package com.bernovia.zajel.actions.acceptRejectRequest

import androidx.lifecycle.LiveData
import com.bernovia.zajel.actions.ActionsResponseBody
import com.bernovia.zajel.helpers.base.BaseViewModelWithBody
import retrofit2.Response


@Suppress("UNCHECKED_CAST") class AcceptRejectRequestViewModel(private val repository: AcceptRejectRequestRepository) : BaseViewModelWithBody<ActionsResponseBody, Int>() {
    private lateinit var responseBody: LiveData<Response<ActionsResponseBody>>

    fun setType(type: String) {
        repository.type = type
    }
    override fun getDataFromRetrofit(body: Int): LiveData<Response<ActionsResponseBody>> {
        responseBody = repository.loadData(body) as LiveData<Response<ActionsResponseBody>>

        return responseBody
    }
}