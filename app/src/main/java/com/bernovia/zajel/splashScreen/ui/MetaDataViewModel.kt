package com.bernovia.zajel.splashScreen.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bernovia.zajel.splashScreen.data.MetaDataRepository
import com.bernovia.zajel.splashScreen.models.MetaDataResponseBody

class MetaDataViewModel(
    private val metaDataRepository: MetaDataRepository) : ViewModel() {


    fun getUserProfile(): LiveData<MetaDataResponseBody> {
        return metaDataRepository.getMetaDataLiveData()
    }

    fun insertUserProfileInLocal() {
        metaDataRepository.insertMetaDataInLoca()
    }

    /**
     * Cleared all references and petitions boundary callback
     */
    override fun onCleared() {
        metaDataRepository.cleared()
    }

}