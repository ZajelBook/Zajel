package com.bernovia.zajel.helpers.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import retrofit2.Response

abstract class BaseViewModelWithBody<T, V> : ViewModel() {

    abstract fun getDataFromRetrofit(body: V): LiveData<Response<T>>
}