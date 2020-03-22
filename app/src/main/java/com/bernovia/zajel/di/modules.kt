package com.bernovia.zajel.di


import com.bernovia.zajel.AppDatabase
import com.bernovia.zajel.BuildConfig
import com.bernovia.zajel.addBook.data.AddBookRepository
import com.bernovia.zajel.addBook.data.AddBookViewModel
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.api.ApiServicesRx
import com.bernovia.zajel.api.AuthInterceptor
import com.bernovia.zajel.api.AuthInterceptorWithToken
import com.bernovia.zajel.auth.logIn.data.LoginRepository
import com.bernovia.zajel.auth.logIn.ui.LoginViewModel
import com.bernovia.zajel.auth.signup.data.SignUpRepository
import com.bernovia.zajel.auth.signup.ui.SignUpViewModel
import com.bernovia.zajel.bookList.data.BooksRepository
import com.bernovia.zajel.bookList.ui.BooksListViewModel
import com.bernovia.zajel.editProfile.data.EditProfileRepository
import com.bernovia.zajel.editProfile.ui.EditProfileViewModel
import com.bernovia.zajel.helpers.apiCallsHelpers.BaseSchedulers
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

        single {
            AppDatabase.getInstance(get()).bookDao()
        }


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
        single {
            EditProfileRepository(get(named("interceptor_with_token")))
        }
        single {
            AddBookRepository(get(named("interceptor_with_token")))
        }

        single<BooksRepository> {
            BooksRepository.BooksRepositoryImpl(get(), get())
        }

    }
}


val appModule by lazy {
    module {

        single<BaseSchedulers> {
            BaseSchedulers.BaseSchedulersImpl()
        }

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
        viewModel {
            EditProfileViewModel(get())
        }
        viewModel {
            BooksListViewModel(get())
        }
        viewModel {
            AddBookViewModel(get())
        }


    }
}


val serviceModuleV1 by lazy {
    module {

        single<ApiServicesRx> {
            ApiServicesRx.Network(get(), get())
        }

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
            okHttpClient.connectTimeout(40, TimeUnit.SECONDS).readTimeout(40, TimeUnit.SECONDS).writeTimeout(40, TimeUnit.SECONDS)
            okHttpClient.interceptors().add(AuthInterceptorWithToken())
            okHttpClient.addInterceptor(logging)  // <-- this is the important line!

            Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(okHttpClient.build()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
        }
    }


}
