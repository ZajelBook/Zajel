package com.bernovia.zajel.api

import com.bernovia.zajel.api.APIs.API_BOOKS_LIST
import com.bernovia.zajel.api.APIs.API_BOOK_ACTIVITIES
import com.bernovia.zajel.api.APIs.API_META_DATA
import com.bernovia.zajel.bookList.models.Book
import com.bernovia.zajel.bookList.models.BookListResponseModel
import com.bernovia.zajel.helpers.apiCallsHelpers.BaseSchedulers
import com.bernovia.zajel.requests.models.BookActivity
import com.bernovia.zajel.requests.models.RequestsResponseModel
import com.bernovia.zajel.splashScreen.models.MetaDataResponseBody
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServicesRx {
    fun bookList(perPage: Int, page: Int): Single<List<Book>>
    fun metaData(): Single<MetaDataResponseBody>

    fun requestsList(type: String, perPage: Int, page: Int): Single<List<BookActivity?>>

    class Network(
        private val retrofit: Retrofit, private val schedulers: BaseSchedulers) : ApiServicesRx {
        override fun bookList(perPage: Int, page: Int): Single<List<Book>> {

            return retrofit.create(NetworkCalls::class.java).getBooksList(perPage, page).subscribeOn(schedulers.io()).map {
                it.books
            }
        }

        override fun metaData(): Single<MetaDataResponseBody> {
            return retrofit.create(NetworkCalls::class.java).getMetaData().subscribeOn(schedulers.io()).map {
                it
            }


        }

        override fun requestsList(type: String, perPage: Int, page: Int): Single<List<BookActivity?>> {
            return retrofit.create(NetworkCalls::class.java).getRequests(type, perPage, page).subscribeOn(schedulers.io()).map {
                it.bookActivities
            }
        }

        interface NetworkCalls {
            @GET(API_BOOKS_LIST) fun getBooksList(@Query("per_page") type: Int, @Query("page") page: Int): Single<BookListResponseModel<List<Book>>>

            @GET(API_META_DATA) fun getMetaData(): Single<MetaDataResponseBody>

            @GET(API_BOOK_ACTIVITIES) fun getRequests(@Query("type") type: String, @Query("per_page") perPage: Int, @Query("page") page: Int): Single<RequestsResponseModel<List<BookActivity?>>>


        }


    }

}