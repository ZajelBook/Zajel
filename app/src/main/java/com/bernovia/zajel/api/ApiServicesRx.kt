package com.bernovia.zajel.api

import com.bernovia.zajel.api.APIs.API_BOOKS_LIST
import com.bernovia.zajel.bookList.models.Book
import com.bernovia.zajel.bookList.models.BookListResponseModel
import com.bernovia.zajel.helpers.apiCallsHelpers.BaseSchedulers
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServicesRx {
    fun bookList(type: Int, page: Int): Single<List<Book>>


    class Network(
        private val retrofit: Retrofit, private val schedulers: BaseSchedulers) : ApiServicesRx {
        override fun bookList(type: Int, page: Int): Single<List<Book>> {

            return retrofit.create(NetworkCalls::class.java).getBooksList(type, page).subscribeOn(schedulers.io()).map {
                it.books
            }
        }

        interface NetworkCalls {
            @GET(API_BOOKS_LIST) fun getBooksList(@Query("per_page") type: Int, @Query("page") page: Int): Single<BookListResponseModel<List<Book>>>


        }


    }

}