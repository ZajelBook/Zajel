package com.bernovia.zajel.splashScreen.data

import androidx.lifecycle.LiveData
import com.bernovia.zajel.api.ApiServicesRx
import com.bernovia.zajel.splashScreen.models.MetaDataResponseBody
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

interface MetaDataRepository {

    fun cleared()
    fun insertMetaDataInLoca()
    fun getMetaDataLiveData(): LiveData<MetaDataResponseBody>


    open class MetaDataRepositoryImpl(
        private val service: ApiServicesRx, private val dao: MetaDataDao) : MetaDataRepository {


        private val compositeDisposable: CompositeDisposable = CompositeDisposable()


        override fun insertMetaDataInLoca() {
            getMetaDat().subscribeOn(Schedulers.io()).flatMapCompletable {
                dao.deleteAllMetaData().andThen(insertMetaData(it))

            }.subscribeBy(onComplete = {}, onError = {}).addTo(compositeDisposable)
        }

        override fun getMetaDataLiveData(): LiveData<MetaDataResponseBody> {
            return dao.getMetaData()

        }


        override fun cleared() {
            compositeDisposable.clear()
        }

        private fun insertMetaData(metaDataResponseBody: MetaDataResponseBody): Completable {
            return dao.insertMetaData(metaDataResponseBody)
        }

        private fun getMetaDat(): Single<MetaDataResponseBody> = service.metaData().map { it }

    }

}