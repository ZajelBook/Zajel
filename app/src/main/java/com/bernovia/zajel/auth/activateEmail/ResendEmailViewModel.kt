package com.bernovia.zajel.auth.activateEmail

import androidx.lifecycle.LiveData
import com.bernovia.zajel.actions.ActionsResponseBody
import com.bernovia.zajel.helpers.base.BaseViewModel
import retrofit2.Response


@Suppress("UNCHECKED_CAST")
class ResendEmailViewModel(private val repository: ResendEmailRepository) :
    BaseViewModel<ActionsResponseBody>() {
    private lateinit var responseBody: LiveData<Response<ActionsResponseBody>>

    override fun getDataFromRetrofit(): LiveData<Response<ActionsResponseBody>> {
        responseBody =
            repository.loadData() as LiveData<Response<ActionsResponseBody>>
        return responseBody
    }
}