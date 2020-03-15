package com.bernovia.zajel.helpers.apiCallsHelpers

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single


/***
 * Create and initialize livedata
 * */
fun <T> liveData(data:T): LiveData<T> {
    val mld = MutableLiveData<T>()
    mld.value = data
    return mld
}


fun <T> Single<T>.toLiveData() :  LiveData<T> {
    return LiveDataReactiveStreams.fromPublisher(this.toFlowable())
}

