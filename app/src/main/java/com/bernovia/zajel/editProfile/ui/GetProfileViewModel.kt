package com.bernovia.zajel.editProfile.ui

import androidx.lifecycle.LiveData
import com.bernovia.zajel.editProfile.data.GetProfileRepository
import com.bernovia.zajel.editProfile.models.GetProfileResponseBody
import com.bernovia.zajel.helpers.base.BaseViewModelWithBody
import retrofit2.Response

@Suppress("UNCHECKED_CAST") class GetProfileViewModel(private val repository: GetProfileRepository) : BaseViewModelWithBody<GetProfileResponseBody, Int>() {
    private lateinit var responseBody: LiveData<Response<GetProfileResponseBody>>

    override fun getDataFromRetrofit(body: Int): LiveData<Response<GetProfileResponseBody>> {
        responseBody = repository.loadData(body) as LiveData<Response<GetProfileResponseBody>>

        return responseBody
    }
}