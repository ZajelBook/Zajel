package com.bernovia.zajel.di


import com.bernovia.zajel.AppDatabase
import com.bernovia.zajel.BuildConfig
import com.bernovia.zajel.actions.acceptRejectRequest.AcceptRejectRequestRepository
import com.bernovia.zajel.actions.acceptRejectRequest.AcceptRejectRequestViewModel
import com.bernovia.zajel.actions.cancelRequest.CancelRequestRepository
import com.bernovia.zajel.actions.cancelRequest.CancelRequestViewModel
import com.bernovia.zajel.actions.logout.LogoutRepository
import com.bernovia.zajel.actions.logout.LogoutViewModel
import com.bernovia.zajel.actions.sendRequest.SendRequestRepository
import com.bernovia.zajel.actions.sendRequest.SendRequestViewModel
import com.bernovia.zajel.addBook.data.AddBookRepository
import com.bernovia.zajel.addBook.data.AddBookViewModel
import com.bernovia.zajel.addBook.updateBook.UpdateBookRepository
import com.bernovia.zajel.addBook.updateBook.UpdateBookViewModel
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.api.ApiServicesRx
import com.bernovia.zajel.api.AuthInterceptor
import com.bernovia.zajel.api.AuthInterceptorWithToken
import com.bernovia.zajel.auth.activateEmail.ActivateEmailRepository
import com.bernovia.zajel.auth.activateEmail.ActivateEmailViewModel
import com.bernovia.zajel.auth.activateEmail.ResendEmailRepository
import com.bernovia.zajel.auth.activateEmail.ResendEmailViewModel
import com.bernovia.zajel.auth.logIn.data.LoginRepository
import com.bernovia.zajel.auth.logIn.ui.LoginViewModel
import com.bernovia.zajel.auth.signup.data.SignUpRepository
import com.bernovia.zajel.auth.signup.ui.SignUpViewModel
import com.bernovia.zajel.bookList.data.BooksRepository
import com.bernovia.zajel.bookList.ui.BooksListViewModel
import com.bernovia.zajel.editProfile.data.EditProfileRepository
import com.bernovia.zajel.editProfile.data.GetProfileRepository
import com.bernovia.zajel.editProfile.ui.EditProfileViewModel
import com.bernovia.zajel.editProfile.ui.GetProfileViewModel
import com.bernovia.zajel.helpers.apiCallsHelpers.BaseSchedulers
import com.bernovia.zajel.messages.data.MessagesRepository
import com.bernovia.zajel.messages.sendMessage.SendMessageRepository
import com.bernovia.zajel.messages.sendMessage.SendMessageViewModel
import com.bernovia.zajel.messages.ui.MessagesListViewModel
import com.bernovia.zajel.notificationsList.data.NotificationsListRepository
import com.bernovia.zajel.notificationsList.ui.NotificationsListViewModel
import com.bernovia.zajel.requests.data.BookActivityRepository
import com.bernovia.zajel.requests.ui.BookActivitiesViewModel
import com.bernovia.zajel.splashScreen.data.MetaDataRepository
import com.bernovia.zajel.splashScreen.ui.MetaDataViewModel
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
        single {
            AppDatabase.getInstance(get()).metaDataDao()
        }
        single {
            AppDatabase.getInstance(get()).bookActivitiesDao()
        }
        single {
            AppDatabase.getInstance(get()).messagesDao()
        }
        single {
            AppDatabase.getInstance(get()).notificationsDao()
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
            GetProfileRepository(get(named("interceptor_with_token")))
        }
        single {
            AddBookRepository(get(named("interceptor_with_token")))
        }
        single {
            UpdateBookRepository(get(named("interceptor_with_token")))
        }
        single {
            AcceptRejectRequestRepository(get(named("interceptor_with_token")))
        }
        single {
            CancelRequestRepository(get(named("interceptor_with_token")))
        }
        single {
            SendRequestRepository(get(named("interceptor_with_token")))
        }
        single {
            SendMessageRepository(get(named("interceptor_with_token")))
        }

        single {
            LogoutRepository(get(named("interceptor_with_token")))
        }
        single {
            ActivateEmailRepository(get(named("interceptor_with_token")))
        }
        single {
            ResendEmailRepository(get(named("interceptor_with_token")))
        }


        single<BooksRepository> {
            BooksRepository.BooksRepositoryImpl(get(), get())
        }

        single<MetaDataRepository> {
            MetaDataRepository.MetaDataRepositoryImpl(get(), get())
        }
        single<BookActivityRepository> {
            BookActivityRepository.BookActivityRepositoryImpl(get(), get())
        }

        single<MessagesRepository> {
            MessagesRepository.MessagesRepositoryImpl(get(), get())
        }

        single<NotificationsListRepository> {
            NotificationsListRepository.NotificationsListRepositoryImpl(get(), get())
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
        viewModel {
            UpdateBookViewModel(get())
        }
        viewModel {
            MetaDataViewModel(get())
        }
        viewModel {
            BookActivitiesViewModel(get())
        }
        viewModel {
            AcceptRejectRequestViewModel(get())
        }
        viewModel {
            SendRequestViewModel(get())
        }
        viewModel {
            CancelRequestViewModel(get())
        }
        viewModel {
            MessagesListViewModel(get())
        }
        viewModel {
            SendMessageViewModel(get())
        }
        viewModel {
            LogoutViewModel(get())
        }
        viewModel {
            NotificationsListViewModel(get())
        }
        viewModel {
            GetProfileViewModel(get())
        }
        viewModel {
            ActivateEmailViewModel(get())
        }
        viewModel {
            ResendEmailViewModel(get())
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
            okHttpClient.connectTimeout(40, TimeUnit.SECONDS).readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
            okHttpClient.interceptors().add(AuthInterceptorWithToken())
            okHttpClient.addInterceptor(logging)  // <-- this is the important line!

            Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
        }
    }


}
