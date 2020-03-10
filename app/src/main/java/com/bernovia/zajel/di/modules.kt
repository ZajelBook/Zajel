package com.bernovia.zajel.di


import com.bernovia.zajel.BuildConfig
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.api.AuthInterceptor
import com.bernovia.zajel.api.AuthInterceptorWithToken
import com.bernovia.zajel.auth.logIn.data.LoginRepository
import com.bernovia.zajel.auth.logIn.ui.LoginViewModel
import com.bernovia.zajel.auth.signup.data.SignUpRepository
import com.bernovia.zajel.auth.signup.ui.SignUpViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val cacheModule by lazy {

    module {

//        single {
//            AppDatabase.getInstance(get()).searchDao()
//        }


    }
}


val repositoryModule by lazy {
    module {


        single {
            LoginRepository(get(named("interceptor")))
        }
        single {
            SignUpRepository(get(named("interceptor")))
        }

    }
}


val appModule by lazy {
    module {

//        single<BaseSchedulers> {
//            BaseSchedulers.BaseSchedulersImpl()
//        }

    }
}


val viewModelModule by lazy {
    module {
        viewModel {
            LoginViewModel(get())
        }
        viewModel {
            SignUpViewModel(get())
        }
    }
}


val serviceModuleV1 by lazy {
    module {

//        single<NetworkServicesV1> {
//            NetworkServicesV1.Network(get(named("baseV1")), get())
//        }

        single(named("interceptor")) {
            ApiServicesCoRoutines.create(BuildConfig.BASE_URL, AuthInterceptor())
        }
        single(named("interceptor_with_token")) {
            ApiServicesCoRoutines.create(BuildConfig.BASE_URL, AuthInterceptorWithToken())
        }


        single<Retrofit> {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient.Builder()
            okHttpClient.connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
            okHttpClient.interceptors().add(AuthInterceptor())
            okHttpClient.addInterceptor(logging)  // <-- this is the important line!

            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
    }


}
