package com.bernovia.zajel.helpers.apiCallsHelpers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface BaseSchedulers {

    fun ui(): Scheduler
    fun io(): Scheduler
    fun computation(): Scheduler

    class BaseSchedulersImpl() : BaseSchedulers {
        override fun ui() = AndroidSchedulers.mainThread()
        override fun io() = Schedulers.io()
        override fun computation() = Schedulers.computation()
    }
}