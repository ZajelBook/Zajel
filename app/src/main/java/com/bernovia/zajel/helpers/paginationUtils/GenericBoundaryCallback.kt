package com.bernovia.zajel.helpers.paginationUtils

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.bernovia.zajel.helpers.apiCallsHelpers.NetworkState
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class GenericBoundaryCallback<T>(
    private val removeAllItems: () -> Completable,
    private val getPage: (page: Int) -> Single<List<T>>,
    private val insertAllItems: (items: List<T>) -> Completable,
    private val isMessaging: Boolean) : PagedList.BoundaryCallback<T>() {

    private val helper = PagingRequestHelper(Executors.newSingleThreadExecutor())
    val networkState: MutableLiveData<NetworkState> = helper.createStatusLiveData()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var offsetCount = 1


    fun refreshPage() {
        offsetCount = 1
        networkState.value = NetworkState.LOADING
        getPage(offsetCount).subscribeOn(Schedulers.io()).flatMapCompletable {
            removeAllItems().andThen(insertAllItems(it))
        }.subscribeBy(onComplete = {
            offsetCount += 1
            networkState.postValue(NetworkState.LOADED)
        }, onError = {
//            networkState.value = NetworkState.error(it.message)
        }).addTo(compositeDisposable)
    }

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    @MainThread override fun onZeroItemsLoaded() {
    }

    /**
     *  reached to the end of the list.
     */
    @MainThread override fun onItemAtEndLoaded(itemAtEnd: T) {
        if (isMessaging) {
            messagingCall(offsetCount)
        } else {
//            helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
                getTop(offsetCount)
//            }
        }


    }


    override fun onItemAtFrontLoaded(itemAtFront: T) {
    }

    private fun messagingCall(offset: Int) {
        if (offset != 1) {
            getPage(offset).subscribeOn(Schedulers.io()).flatMapCompletable {
                insertAllItems(it)
            }.subscribeBy(onComplete = {
                offsetCount += 1
            }, onError = {
                networkState.postValue(NetworkState.error(it.message))
            }).addTo(compositeDisposable)
        }
    }

    private fun getTop(offset: Int) {
        if (offset != 1) {
            getPage(offset).subscribeOn(Schedulers.io()).flatMapCompletable {
                insertAllItems(it)
            }.subscribeBy(onComplete = {
//                pagingRequest.recordSuccess()
                offsetCount += 1
            }, onError = {
                networkState.postValue(NetworkState.error(it.message))
//                pagingRequest.recordFailure(it)
            }).addTo(compositeDisposable)
        }
    }


    /** Clear all references **/
    fun cleared() {
        compositeDisposable.clear()
    }

    fun retryPetitions() = helper.retryAllFailed()


}
