package com.bernovia.zajel.addBook.updateBook

import androidx.lifecycle.LiveData
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.bookList.models.Book
import com.bernovia.zajel.helpers.base.BaseRepositoryWithBody
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UpdateBookRepository(service: ApiServicesCoRoutines) : BaseRepositoryWithBody<Book, Map<String, RequestBody>>(service, true) {
    var bookId: Int = 0
    var image: MultipartBody.Part? = null

    override fun loadData(body: Map<String, RequestBody>): LiveData<Any> {
        return fetchData {
            service.updateBookAsync(bookId, image, body)
        }
    }
}