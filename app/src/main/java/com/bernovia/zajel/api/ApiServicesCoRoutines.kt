package com.bernovia.zajel.api


import com.bernovia.zajel.actions.ActionsResponseBody
import com.bernovia.zajel.actions.SendRequestRequestBody
import com.bernovia.zajel.api.APIs.API_AUTH_SIGN_IN
import com.bernovia.zajel.api.APIs.API_AUTH_SIGN_UP
import com.bernovia.zajel.api.APIs.API_BOOKS_LIST
import com.bernovia.zajel.api.APIs.API_BOOK_ACTIVITIES
import com.bernovia.zajel.api.APIs.API_CONVERSATION
import com.bernovia.zajel.api.APIs.API_EDIT_USER_PROFILE
import com.bernovia.zajel.api.APIs.API_LOGOUT
import com.bernovia.zajel.auth.authResponseModels.AuthResponseBody
import com.bernovia.zajel.auth.authResponseModels.AuthResponseData
import com.bernovia.zajel.auth.logIn.models.LoginRequestBody
import com.bernovia.zajel.auth.signup.models.SignUpRequestBody
import com.bernovia.zajel.editProfile.models.EditProfileRequestBody
import com.bernovia.zajel.helpers.UpdateValuesResponseBody
import com.bernovia.zajel.messages.sendMessage.SendMessageRequestBody
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiServicesCoRoutines {


    @POST(API_AUTH_SIGN_IN) fun loginAsync(@Body loginRequestBody: LoginRequestBody): Deferred<Response<AuthResponseBody<AuthResponseData>>>

    @POST(API_AUTH_SIGN_UP) fun signUpAsync(@Body signUpRequestBody: SignUpRequestBody): Deferred<Response<AuthResponseBody<AuthResponseData>>>

    @PUT(API_EDIT_USER_PROFILE) fun editUserProfileAsync(@Path("user_id") userId: Int, @Body editProfileRequestBody: EditProfileRequestBody): Deferred<Response<UpdateValuesResponseBody>>

    @Multipart @POST(API_BOOKS_LIST) fun addBookAsync(@Part image: MultipartBody.Part, @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>): Deferred<Response<UpdateValuesResponseBody>>


    @Multipart @PUT("$API_BOOKS_LIST/{book_id}") fun updateBookAsync(
        @Path("book_id") bookId: Int, @Part image: MultipartBody.Part?, @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>): Deferred<Response<UpdateValuesResponseBody>>


    @POST(API_BOOK_ACTIVITIES) fun sendRequestAsync(@Body sendRequestRequestBody: SendRequestRequestBody): Deferred<Response<ActionsResponseBody>>

    @PUT("$API_BOOK_ACTIVITIES/{book_id}") fun acceptRejectRequestAsync(@Path("book_id") bookId: Int, @Query("type") type: String): Deferred<Response<ActionsResponseBody>>

    @DELETE("$API_BOOK_ACTIVITIES/{book_id}") fun cancelRequestAsync(@Path("book_id") bookId: Int): Deferred<Response<ActionsResponseBody>>

    @POST(API_CONVERSATION) fun sendMessageAsync(@Body sendMessageRequestBody: SendMessageRequestBody, @Path("conversation_id") conversationId: Int): Deferred<Response<ActionsResponseBody>>

    @DELETE(API_LOGOUT) fun logoutAsync(): Deferred<Response<ActionsResponseBody>>


    companion object {

        fun create(url: String, interceptor: Interceptor): ApiServicesCoRoutines {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient.Builder()
            okHttpClient.connectTimeout(40, TimeUnit.SECONDS).readTimeout(40, TimeUnit.SECONDS).writeTimeout(40, TimeUnit.SECONDS)
            okHttpClient.interceptors().add(interceptor)
            okHttpClient.addInterceptor(logging)  // <-- this is the important line!

            return Retrofit.Builder().baseUrl(url).client(okHttpClient.build()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(CoroutineCallAdapterFactory()).build().create(
                ApiServicesCoRoutines::class.java)
        }
    }

}