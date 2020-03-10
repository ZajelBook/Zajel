package com.bernovia.zajel.helpers.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import retrofit2.Response

abstract class BaseViewModel<T> : ViewModel() {

    abstract fun getDataFromRetrofit(): LiveData<Response<T>>
}