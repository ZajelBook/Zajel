package com.bernovia.zajel.addBook.updateBook

import androidx.lifecycle.LiveData
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.helpers.UpdateValuesResponseBody
import com.bernovia.zajel.helpers.base.BaseRepositoryWithBody
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UpdateBookRepository(service: ApiServicesCoRoutines) : BaseRepositoryWithBody<UpdateValuesResponseBody, Map<String, RequestBody>>(service, true) {
    var bookId: Int = 0
     var image: MultipartBody.Part? = null

    override fun loadData(body: Map<String, RequestBody>): LiveData<Any> {
        return fetchData {
            service.updateBookAsync(bookId, image, body)
        }
    }
}