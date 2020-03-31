package com.bernovia.zajel.actions.logout

import androidx.lifecycle.LiveData
import com.bernovia.zajel.actions.ActionsResponseBody
import com.bernovia.zajel.helpers.base.BaseViewModel
import retrofit2.Response

@Suppress("UNCHECKED_CAST") class LogoutViewModel(private val repository: LogoutRepository) : BaseViewModel<ActionsResponseBody>() {
    private lateinit var responseBody: LiveData<Response<ActionsResponseBody>>
    override fun getDataFromRetrofit(): LiveData<Response<ActionsResponseBody>> {
        responseBody = repository.loadData() as LiveData<Response<ActionsResponseBody>>
        return responseBody
    }
}