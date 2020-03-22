package com.bernovia.zajel.addBook.data

import androidx.lifecycle.LiveData
import com.bernovia.zajel.helpers.UpdateValuesResponseBody
import com.bernovia.zajel.helpers.base.BaseViewModelWithBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response


@Suppress("UNCHECKED_CAST") class AddBookViewModel(private val repository: AddBookRepository) : BaseViewModelWithBody<UpdateValuesResponseBody, Map<String, RequestBody>>() {
    private lateinit var responseBody: LiveData<Response<UpdateValuesResponseBody>>

    fun setImage(image: MultipartBody.Part) {
        repository.setImage(image)
    }

    override fun getDataFromRetrofit(body: Map<String, RequestBody>): LiveData<Response<UpdateValuesResponseBody>> {
        responseBody = repository.loadData(body) as LiveData<Response<UpdateValuesResponseBody>>

        return responseBody
    }
}