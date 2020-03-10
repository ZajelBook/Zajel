package com.bernovia.zajel.api


import com.bernovia.zajel.api.APIs.API_AUTH_SIGN_IN
import com.bernovia.zajel.api.APIs.API_AUTH_SIGN_UP
import com.bernovia.zajel.auth.authResponseModels.AuthResponseBody
import com.bernovia.zajel.auth.authResponseModels.AuthResponseData
import com.bernovia.zajel.auth.logIn.models.LoginRequestBody
import com.bernovia.zajel.auth.signup.models.SignUpRequestBody
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface ApiServicesCoRoutines {


    @POST(API_AUTH_SIGN_IN)
    fun login(@Body loginRequestBody: LoginRequestBody): Deferred<Response<AuthResponseBody<AuthResponseData>>>

    @POST(API_AUTH_SIGN_UP)
    fun signUp(@Body signUpRequestBody: SignUpRequestBody): Deferred<Response<AuthResponseBody<AuthResponseData>>>


    companion object {

        fun create(url: String, interceptor: Interceptor): ApiServicesCoRoutines {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient.Builder()
            okHttpClient.connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
            okHttpClient.interceptors().add(interceptor)
            okHttpClient.addInterceptor(logging)  // <-- this is the important line!

            return Retrofit.Builder()
                .baseUrl(url).client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(ApiServicesCoRoutines::class.java)
        }
    }

}