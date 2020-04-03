package com.bernovia.zajel.api

import com.bernovia.zajel.api.APIs.API_BOOKS_LIST
import com.bernovia.zajel.api.APIs.API_BOOK_ACTIVITIES
import com.bernovia.zajel.api.APIs.API_CONVERSATION
import com.bernovia.zajel.api.APIs.API_META_DATA
import com.bernovia.zajel.api.APIs.API_MY_BOOKS
import com.bernovia.zajel.bookList.models.Book
import com.bernovia.zajel.bookList.models.BookListResponseModel
import com.bernovia.zajel.helpers.NetworkUtil.handleApiError
import com.bernovia.zajel.helpers.apiCallsHelpers.BaseSchedulers
import com.bernovia.zajel.messages.models.Message
import com.bernovia.zajel.messages.models.MessagesListResponseBody
import com.bernovia.zajel.requests.models.BookActivity
import com.bernovia.zajel.requests.models.RequestsResponseModel
import com.bernovia.zajel.splashScreen.models.MetaDataResponseBody
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServicesRx {
    fun bookList(perPage: Int, page: Int): Single<List<Book>>
    fun metaData(): Single<MetaDataResponseBody>

    fun requestsList(type: String, perPage: Int, page: Int): Single<List<BookActivity?>>

    fun messagesList(perPage: Int, page: Int, conversationId: Int): Single<List<Message?>>

    fun book(bookId: Int): Single<Book>
    fun myBookList(perPage: Int, page: Int): Single<List<Book>>


    class Network(
        private val retrofit: Retrofit, private val schedulers: BaseSchedulers) : ApiServicesRx {
        override fun bookList(perPage: Int, page: Int): Single<List<Book>> {

            return retrofit.create(NetworkCalls::class.java).getBooksList(perPage, page).subscribeOn(schedulers.io()).doOnError { handleApiError(it) }.map {
                it.books
            }
        }

        override fun metaData(): Single<MetaDataResponseBody> {
            return retrofit.create(NetworkCalls::class.java).getMetaData().subscribeOn(schedulers.io()).doOnError { handleApiError(it) }.map {
                it
            }
        }

        override fun requestsList(type: String, perPage: Int, page: Int): Single<List<BookActivity?>> {
            return retrofit.create(NetworkCalls::class.java).getRequests(type, perPage, page).subscribeOn(schedulers.io()).doOnError { handleApiError(it) }.map {
                it.bookActivities
            }
        }

        override fun messagesList(perPage: Int, page: Int, conversationId: Int): Single<List<Message?>> {
            return retrofit.create(NetworkCalls::class.java).getMessages(conversationId, perPage, page).subscribeOn(schedulers.io()).doOnError { handleApiError(it) }.map {
                it.messages
            }
        }

        override fun book(bookId: Int): Single<Book> {
            return retrofit.create(NetworkCalls::class.java).getBook(bookId).subscribeOn(schedulers.io()).doOnError { handleApiError(it) }.map {
                it
            }
        }

        override fun myBookList(perPage: Int, page: Int): Single<List<Book>> {
            return retrofit.create(NetworkCalls::class.java).getMyBooksList(perPage, page).subscribeOn(schedulers.io()).doOnError { handleApiError(it) }.map {
                it.books
            }
        }

        interface NetworkCalls {
            @GET("$API_BOOKS_LIST/{book_id}") fun getBook(@Path("book_id") bookId: Int): Single<Book>

            @GET(API_MY_BOOKS) fun getMyBooksList(@Query("per_page") perPage: Int, @Query("page") page: Int): Single<BookListResponseModel<List<Book>>>

            @GET(API_BOOKS_LIST) fun getBooksList(@Query("per_page") perPage: Int, @Query("page") page: Int): Single<BookListResponseModel<List<Book>>>

            @GET(API_META_DATA) fun getMetaData(): Single<MetaDataResponseBody>

            @GET(API_BOOK_ACTIVITIES) fun getRequests(@Query("type") type: String, @Query("per_page") perPage: Int, @Query("page") page: Int): Single<RequestsResponseModel<List<BookActivity?>>>

            @GET(API_CONVERSATION) fun getMessages(
                @Path("conversation_id") conversationId: Int, @Query("per_page") type: Int, @Query("page") page: Int): Single<MessagesListResponseBody<List<Message?>>>


        }


    }

}