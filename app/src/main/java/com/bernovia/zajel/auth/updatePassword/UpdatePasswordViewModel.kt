package com.bernovia.zajel.auth.updatePassword

import androidx.lifecycle.LiveData
import com.bernovia.zajel.actions.ActionsResponseBody
import com.bernovia.zajel.helpers.base.BaseViewModelWithBody
import retrofit2.Response


@Suppress("UNCHECKED_CAST")
class UpdatePasswordViewModel(private val repository: UpdatePasswordRepository) : BaseViewModelWithBody<ActionsResponseBody, UpdatePasswordRequestBody>() {
    private lateinit var responseBody: LiveData<Response<ActionsResponseBody>>

    override fun getDataFromRetrofit(body: UpdatePasswordRequestBody): LiveData<Response<ActionsResponseBody>> {
        responseBody = repository.loadData(body) as LiveData<Response<ActionsResponseBody>>
        return responseBody
    }
}