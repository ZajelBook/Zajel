package com.bernovia.zajel.addBook.data

import androidx.lifecycle.LiveData
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.helpers.UpdateValuesResponseBody
import com.bernovia.zajel.helpers.base.BaseRepositoryWithBody
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddBookRepository(service: ApiServicesCoRoutines) : BaseRepositoryWithBody<UpdateValuesResponseBody, Map<String, RequestBody>>(service, true) {
    private lateinit var image: MultipartBody.Part

    fun setImage(image: MultipartBody.Part) {
        this.image = image
    }

    override fun loadData(body: Map<String, RequestBody>): LiveData<Any> {
        return fetchData {
            service.addBookAsync(image, body)
        }
    }
}