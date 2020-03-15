package com.bernovia.zajel.editProfile.ui

import androidx.lifecycle.LiveData
import com.bernovia.zajel.editProfile.data.EditProfileRepository
import com.bernovia.zajel.editProfile.models.EditProfileRequestBody
import com.bernovia.zajel.helpers.UpdateValuesResponseBody
import com.bernovia.zajel.helpers.base.BaseViewModelWithBody
import retrofit2.Response


@Suppress("UNCHECKED_CAST") class EditProfileViewModel(private val repository: EditProfileRepository) : BaseViewModelWithBody<UpdateValuesResponseBody, EditProfileRequestBody>() {
    private lateinit var responseBody: LiveData<Response<UpdateValuesResponseBody>>


    fun setUserId(userId: Int) {
        repository.setUserId(userId)
    }
    override fun getDataFromRetrofit(body: EditProfileRequestBody): LiveData<Response<UpdateValuesResponseBody>> {
        responseBody = repository.loadData(body) as LiveData<Response<UpdateValuesResponseBody>>

        return responseBody
    }
}